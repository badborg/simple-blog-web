(ns server.routes
  (:require [compojure.core :refer [routes defroutes context GET]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]))

(defroutes pages
  (GET "/" [] "Hello"))

(defroutes not-found
  (route/not-found "Not found."))

(def wrap-defaults-options
  {:session false
   :security {:anti-forgery false}})

(defn wraps
  [h]
  (-> h
      (wrap-defaults (merge site-defaults
                            wrap-defaults-options))
      wrap-json-response
      wrap-json-params))

(def handler
  (-> (routes
        pages
        not-found)
      wraps))
