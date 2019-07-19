(ns server.routes
  (:require [compojure.core :refer [routes defroutes context GET]]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
            [server.backend :as backend]
            [server.blank-img :as blank-img]
            [server.controllers :as controllers]
            [server.css :as css]
            [server.logo :as logo]
            [server.permalink :as permalink]
            [server.middleware.canonical :refer [wrap-canonical]]
            [server.middleware.pagination :refer [wrap-pagination]]
            [server.middleware.resources :refer [wrap-resources]]
            [server.views.not-found :as not-found]))

(def default-per-page
  (some-> (env :per-page)
          Integer/parseInt))

(defroutes pages
  (GET "/" [] controllers/home)
  (GET (logo/path) [] (logo/response))
  (GET (css/path) [] (css/response))
  (GET (blank-img/path) [] (blank-img/response))
  (GET (permalink/tag-path) [] controllers/tag)
  (GET (permalink/post-path) [] controllers/post))

(defroutes not-found
  (route/not-found (not-found/main)))

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
      wrap-canonical
      (wrap-defaults (merge site-defaults
                            wrap-defaults-options))
      wrap-resources
      wrap-json-response
      wrap-json-params))

(def handler
  (-> (routes
        pages
        api
        not-found)
      wraps))
