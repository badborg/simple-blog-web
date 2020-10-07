(ns server.core-test
  (:require [clojure.test :refer :all]
            [clojure.set :refer [intersection]]
            [cheshire.core :as json]
            [environ.core :refer [env]]
            [ring.mock.request :as mock]
            [server.routes :refer [handler]]))

(def test-data env)

(defn read-json-body
  [res]
  (try
    (-> (:body res)
        (json/parse-string true))
    (catch com.fasterxml.jackson.core.JsonParseException _
      nil)))

(defn get-posts
  ([]
   (get-posts nil))
  ([params]
   (cond-> (mock/request :get "/api/posts")
     params (mock/query-string params)
     true (-> handler read-json-body :posts))))

(deftest api-get-posts
  (let [posts (get-posts)
        post (first posts)]
    (is (sequential? posts))
    (is (= #{:id
             :title
             :image_url
             :url}
           (-> post keys set)))))

(deftest api-get-posts-pagination
  (let [page-1-posts (get-posts)
        page-2-posts (get-posts {:page 2})]
    (is (> (-> page-1-posts last :id)
           (-> page-2-posts first :id)))))

(deftest api-get-posts-invalid-page
  (let [posts (get-posts {:page "a"})]
    (is (sequential? posts))))

(defn search-posts
  [params]
  (cond-> (mock/request :get "/api/search")
    params (mock/query-string params)
    true (-> handler read-json-body)))

(deftest api-search
  (let [phrase (:search-phrase test-data)
        {:keys [terms results]} (search-posts {:s phrase})]
    (is (sequential? terms))
    (is (sequential? results))
    (is (= #{:id
             :title
             :image_url
             :url}
           (->> results
                (mapcat keys)
                set)))))

(deftest api-search-pagination
  (let [phrase (:search-phrase test-data)
        page-1-results (:results (search-posts {:s phrase}))
        page-2-results (:results (search-posts {:s phrase
                                                :page 2}))]
    (is (> (-> page-1-results last :id)
           (-> page-2-results first :id)))))

(defn get-post
  [id]
  (-> (mock/request :get (str "/api/posts/" id))
      handler
      read-json-body
      :post))

(deftest api-get-post
  (let [post (get-post (:post-id test-data))]
    (is post)
    (is (-> (count (:images post))
            (> 0)))
    (is (= #{:id
             :title
             :content
             :tags
             :images
             :image_url
             :url
             :structured-data}
           (-> post
               keys
               set)))
    (is (= #{:id
             :name
             :url}
           (->> (:tags post)
                (mapcat keys)
                set)))))

(deftest api-get-post-by-slug
  (let [post (get-post (:post-slug test-data))]
    (is post)))

(deftest api-get-tag
  (let [tag (-> (mock/request :get (str "/api/tags/" (:tag-name test-data)))
                handler
                read-json-body
                :tag)]
    (is tag)
    (is (= #{:id
             :name
             :url}
           (-> tag
               keys
               set)))))

(defn get-tag-posts
  ([tag-name]
   (get-tag-posts tag-name nil))
  ([tag-name query-string]
   (cond-> (mock/request :get (str "/api/tags/" tag-name "/posts"))
     query-string (mock/query-string query-string)
     true (-> handler read-json-body :posts))))

(deftest api-get-tag-posts
  (let [posts (get-tag-posts (:tag-name test-data))]
    (is (sequential? posts))))

(deftest api-get-tag-posts-pagination
  (let [tag-name (:tag-name test-data)
        page-1-posts (get-tag-posts tag-name)
        page-2-posts (get-tag-posts tag-name
                                    {:page 2})]
      (is (empty?
            (intersection (set (map :id page-1-posts))
                          (set (map :id page-2-posts)))))))

(defn get-related-posts
  ([id]
   (get-related-posts id nil))
  ([id query-string]
   (cond-> (mock/request :get (str "/api/posts/" id "/related"))
     query-string (mock/query-string query-string)
     true (-> handler read-json-body :posts))))

(deftest api-get-related-posts
  (let [posts (get-related-posts (:post-id test-data))]
    (is (sequential? posts))
    (is (= 12 (count posts)))))

(deftest api-get-related-posts-with-excluded-ids
  (let [post-id (:post-id test-data)
        posts (get-related-posts post-id
                                 {:excluded (str "1," post-id)})]
    (is (= 12 (count posts)))
    (is (->> posts
             (map :id)
             (not-any? #{1 post-id})))))
