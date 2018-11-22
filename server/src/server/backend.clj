(ns server.backend
  (:require [cheshire.core :as json]
            [environ.core :refer [env]]
            [org.httpkit.client :as http]
            [server.album :as album]
            [server.permalink :as permalink]
            [server.sanitize :refer [sanitize]]
            [clojure.string :as str]))

(def backend-url
  (env :backend-url))

(def default-small-per-page
  (some-> (env :small-per-page)
          Integer/parseInt))

(defn backend-path
  [path]
  (str backend-url path))

(defn response
  [res]
  (select-keys res [:status :body]))

(defn success?
  [status]
  (some-> status
          (quot 100)
          (= 2)))

(defn sanitize-tag
  [tag]
  (let [add-tag-url (fn [tag]
                      (assoc tag :url (permalink/tag tag)))]
    (-> tag
        add-tag-url
        (dissoc :slug))))

(defn sanitized-tag-response
  [res]
  (let [{:keys [status body] :as res} (response res)]
    (cond-> res
      (success? status) (-> (update :body json/parse-string true)
                            (update-in [:body :tag] sanitize-tag)))))

(defn add-post-url
  [post]
  (assoc post :url (permalink/post post)))

(defn sanitized-post-response
  [res]
  (let [{:keys [status body] :as res} (response res)
        sanitize-tags (fn [tags]
                        (map sanitize-tag tags))
        sanitize-post (fn [post]
                        (-> post
                            (update :content sanitize)
                            (update :tags sanitize-tags)
                            add-post-url
                            (dissoc :slug
                                    :date
                                    :status
                                    :modified)))]
    (cond-> res
      (success? status) (-> (update :body json/parse-string true)
                            (update-in [:body :post] sanitize-post)))))

(defn album->images
  [res]
  (let [{:keys [status body] :as res} (response res)
        {:keys [image_url album_num album_url]} (:post body)]
    (cond-> res
      (success? status) (-> (assoc-in [:body :post :images]
                                      (album/album->images image_url
                                                           album_url
                                                           album_num))
                            (update-in [:body :post]
                                       dissoc
                                       :album_num
                                       :album_url)))))

(defn sanitize-posts
  [posts]
  (let [sanitize-post (fn [post]
                        (-> post
                            add-post-url
                            (dissoc :slug
                                    :date
                                    :status)))]
    (map sanitize-post posts)))

(defn sanitized-posts-response
  [res]
  (let [{:keys [status body] :as res} (response res)]
    (cond-> res
      (success? status) (-> (update :body json/parse-string true)
                            (update-in [:body :posts] sanitize-posts)))))

(defn sanitized-search-response
  [res]
  (let [{:keys [status body] :as res} (response res)]
    (cond-> res
      (success? status) (-> (update :body json/parse-string true)
                            (update-in [:body :results] sanitize-posts)))))

(defn posts
  [req]
  (let [backend-params (-> (:params req)
                           (select-keys [:limit :offset]))]
    (-> @(http/get (backend-path "/posts")
                   {:query-params backend-params})
        sanitized-posts-response
        response)))

(defn post
  [{:keys [params] :as req}]
  (let [id (:id params)]
    (-> @(http/get (backend-path (str "/posts/" id)))
        sanitized-post-response
        album->images
        response)))

(defn related-posts
  [{:keys [params] :as req}]
  (let [{:keys [id excluded]} params
        backend-params (-> params
                           (select-keys [:excluded])
                           (assoc :limit default-small-per-page))]
    (-> @(http/get (backend-path (str "/posts/" id "/related"))
                   {:query-params backend-params})
        sanitized-posts-response
        response)))

(defn tag
  [{:keys [params] :as req}]
  (let [id (:id params)]
    (-> @(http/get (backend-path (str "/tags/" id)))
        sanitized-tag-response
        response)))

(defn tag-posts
  [{:keys [params] :as req}]
  (let [id (:id params)
        backend-params (select-keys params [:limit :offset])]
    (-> @(http/get (backend-path (str "/tags/" id "/posts"))
                   {:query-params backend-params})
        sanitized-posts-response
        response)))

(defn search
  [{:keys [params] :as req}]
  (let [phrase (:s params)
        backend-params (-> params
                           (select-keys [:limit :offset])
                           (assoc :phrase phrase))]
    (-> @(http/get (backend-path "/search")
                   {:query-params backend-params})
        sanitized-search-response
        response)))
