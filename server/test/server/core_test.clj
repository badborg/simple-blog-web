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

(deftest api-get-posts
  (let [posts-req (mock/request :get "/api/posts")
        get-posts #(-> % read-json-body :posts)
        res-data (atom {})]
    (testing "API Posts")
    (let [res (-> posts-req
                  handler)
          posts (get-posts res)]
      (is (sequential? posts))
      (swap! res-data assoc :last-id (-> posts last :id))
      (testing "API Posts Item")
      (let [post (first posts)]
        (is (= #{:id
                 :title
                 :image_url
                 :url}
               (-> post
                   keys
                   set)))))
    (testing "API Posts pagination")
    (let [posts (-> posts-req
                    (mock/query-string {:page 2})
                    handler
                    get-posts)]
      (is (< (-> posts first :id)
             (-> @res-data :last-id))))
    (testing "API Posts invalid page")
    (let [posts (-> posts-req
                    (mock/query-string {:page "a"})
                    handler
                    get-posts)]
      (is (sequential? posts)))))

(deftest api-search
  (let [search-req (mock/request :get "/api/search")
        phrase (:search-phrase test-data)
        res-data (atom {})]
    (testing "API search posts")
    (let [res (-> search-req
                  (mock/query-string {:s phrase})
                  handler)
          {:keys [terms results]} (read-json-body res)]
      (is (sequential? terms))
      (is (sequential? results))
      (is (= #{:id
               :title
               :image_url
               :url}
             (->> results
                  (mapcat keys)
                  set)))
      (swap! res-data assoc :last-id (-> results last :id)))
    (testing "API search pagination")
    (let [results (-> search-req
                      (mock/query-string {:s phrase
                                          :page 2})
                      handler
                      read-json-body
                      :results)]
      (is (> (:last-id @res-data)
             (-> results first :id))))))

(deftest api-get-post
  (let [post-req #(mock/request :get (str "/api/posts/" %))]
    (testing "API get post from id")
    (let [post (-> (post-req (:post-id test-data))
                   handler
                   read-json-body
                   :post)]
      (is post)
      (is (-> (:images post)
              count
              (> 0)))
      (is (= #{:id
               :title
               :content
               :tags
               :images
               :image_url
               :url}
             (-> post
                 keys
                 set)))
      (is (= #{:id
               :name
               :url}
             (->> (:tags post)
                  (mapcat keys)
                  set))))
    (testing "API get post from slug")
    (let [slug (:post-slug test-data)
          post (-> (post-req slug)
                   handler
                   read-json-body
                   :post)]
      (is post))))

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

(deftest api-get-tag-posts
  (let [tag-posts-req (mock/request :get (str "/api/tags/" (:tag-name test-data) "/posts"))
        res-data (atom {})
        ids-set #(->> % (map :id) set)]
    (testing "API get tag posts")
    (let [posts (-> tag-posts-req
                    handler
                    read-json-body
                    :posts)]
      (is (sequential? posts))
      (swap! res-data assoc :ids (ids-set posts)))
    (testing "API get tag posts pagination")
    (let [posts (-> tag-posts-req
                    (mock/query-string {:page 2})
                    handler
                    read-json-body
                    :posts)]
      (is (-> (ids-set posts)
              (intersection (:id res-data))
              empty?)))))

(deftest api-get-related-posts
  (let [rp-req (mock/request :get (str "/api/posts/" (:post-id test-data) "/related"))]
    (testing "API get related posts")
    (let [posts (-> rp-req
                  handler
                  read-json-body
                  :posts)]
      (is (sequential? posts))
      (is (= 12 (count posts))))
    (testing "API get related posts with excluded ids")
    (let [posts (-> rp-req
                    (mock/query-string {:excluded (str "1," (:post-id test-data))})
                    handler
                    read-json-body
                    :posts)]
      (is (= 12 (count posts)))
      (is (->> posts
               (map :id)
               (not-any? #{1 (:post-id test-data)}))))))
