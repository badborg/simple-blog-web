(ns client.core
  (:require [client.img-load :as img-load]
            [client.pagination :as pagination]
            [client.related :as related]
            [client.search :as search]))

(enable-console-print!)

(defn domready
  [handler]
  (.addEventListener js/window "DOMContentLoaded" handler))

(defn init
  [e]
  (img-load/init)
  (pagination/init)
  (search/init)
  (related/init))

(domready init)
