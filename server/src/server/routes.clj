(ns server.routes
  (:require [compojure.core :refer [routes defroutes context GET]]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
            [server.backend :as backend]
            [server.controllers :as controllers]
            [server.css :as css]
            [server.logo :as logo]
            [server.permalink :as permalink]
            [server.middleware.pagination :refer [wrap-pagination]]))

(def default-per-page
  (some-> (env :per-page)
          Integer/parseInt))

(defroutes pages
  (GET "/" [] controllers/home)
  (GET (permalink/post-path) [] controllers/post)
  (GET "/tag/:id/" [] controllers/tag)
  (GET (logo/path) [] (logo/response))
  (GET (css/path) [] (css/response)))

(defroutes not-found
  (route/not-found "Not found."))

(defroutes api
  (context "/api" []
           (GET "/posts" [] backend/posts)
           (GET "/posts/:id" [] backend/post)
           (GET "/posts/:id/related" [] backend/related-posts)
           (GET "/tags/:id" [] backend/tag)
           (GET "/tags/:id/posts" [] backend/tag-posts)
           (GET "/search" [] backend/search)))

(def wrap-defaults-options
  {:session false
   :security {:anti-forgery false}})

(defn wraps
  [h]
  (-> h
      (wrap-pagination {:per-page default-per-page})
      (wrap-defaults (merge site-defaults
                            wrap-defaults-options))
      wrap-json-response
      wrap-json-params))

(def handler
  (-> (routes
        pages
        api
        not-found)
      wraps))
