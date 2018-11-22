(ns client.core.api
  (:require [ajax.core :refer [GET]]))

(defn ajax-method
  [method]
  (case method
    :get GET))

(def json-options
  {:format :json
   :response-format :json
   :keywords? true})

(defn req
  ([method path ok error]
   (req method path nil ok error))
  ([method path params ok error]
   ((ajax-method method)
    path
    (merge json-options
           {:params params
            :handler ok
            :error-handler error}))))
