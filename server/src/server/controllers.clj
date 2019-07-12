(ns server.controllers
  (:require [environ.core :refer [env]]
            [server.backend :as backend]
            [server.views.home :as home]
            [server.views.post :as post]
            [server.views.search :as search]
            [server.views.tag :as tag]))

(def scheme
  (env :permalink-scheme))

(defn home
  [{:keys [params] :as req}]
  (let [page (:page params)
        search-phrase (:s params)
        search? (-> search-phrase nil? not)]
    (if search?
      (let [{:keys [results terms]} (-> (backend/search req)
                                        :body)]
        (search/main terms results page (select-keys params [:s])))
      (let [posts (-> (backend/posts req)
                      (get-in [:body :posts]))]
        (home/main posts page)))))

(defn integer-id?
  [id]
  (try
    (-> id
        Integer/parseInt
        integer?)
    (catch Exception e false)))

(defn post
  [{:keys [params] :as req}]
  (let [id (:id params)
        id-ok? (case scheme
                 "date" (integer-id? id)
                 (not (integer-id? id)))]
    (when id-ok?
      (when-let [post (-> (backend/post req)
                          (get-in [:body :post]))]
        (let [related-posts (-> (backend/related-posts req)
                                (get-in [:body :posts]))]
             (post/main post related-posts))))))

(defn tag
  [{:keys [params] :as req}]
  (let [id-ok? (-> (:id params)
                   integer-id?
                   not)]
    (when id-ok?
      (let [page (:page params)
            tag (-> (backend/tag req)
                    (get-in [:body :tag]))
            posts (-> (backend/tag-posts req)
                      (get-in [:body :posts]))]
        (tag/main tag posts page)))))
