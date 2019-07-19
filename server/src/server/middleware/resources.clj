(ns server.middleware.resources
  (:require [server.resources :refer [core-script-path
                                      core-style-path]]))

(def hours->secs
  (partial * 3600))

(def cache-control
  "Cache-Control")

(defn add-cache-control
  [{:keys [headers status] :as res}]
  (if (and (#{304 200} status)
           (not (contains? headers cache-control)))
    (assoc-in res [:headers cache-control]
              (str "max-age=" (hours->secs 2400)))
    res))

(defn wrap-resources
  [handler]
  (fn [{:keys [uri] :as req}]
    (if (and (#{:head :get} (:request-method req))
             (#{(core-script-path) (core-style-path)} uri))
      (add-cache-control (handler req))
      (handler req))))
