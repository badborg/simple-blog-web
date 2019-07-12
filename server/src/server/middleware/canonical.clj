(ns server.middleware.canonical
  (:require [clojure.string :as s]
            [ring.util.response :refer [redirect]]
            [server.permalink :refer [paginated-url]]))

(defn ends-with-trailing-slash
  [uri]
  (when-not (= "/" uri)
    (when-let [found (re-matches #"(.*[^\/])?\/+$" uri)]
      {:without-slash (or (get found 1)
                          "/")})))

(defn wrap-trailing-slash-redirect
  [handler]
  (fn [{:keys [params uri] :as req}]
    (if-let [found (ends-with-trailing-slash uri)]
      (redirect (paginated-url (:without-slash found)
                               nil
                               params)
                301)
      (handler req))))

(defn ends-with-page-number
  [uri]
  (when-let [found (re-matches #"(.+)?\/page(\/?([0-9]+)?)\/?$" uri)]
    {:without-page-num (or (get found 1)
                           "/")
     :page (some-> (get found 3)
                   Integer/parseInt)}))

(defn wrap-paginate-redirect
  [handler]
  (fn [{:keys [params uri] :as req}]
    (if-let [found (ends-with-page-number uri)]
      (redirect (paginated-url (:without-page-num found)
                               (:page found)
                               params)
                301)
      (handler req))))

(defn wrap-canonical
  [handler]
  (-> handler
      wrap-trailing-slash-redirect
      wrap-paginate-redirect))
