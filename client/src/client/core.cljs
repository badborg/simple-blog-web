(ns client.core
  (:require [client.pagination :as pagination]
            [client.related :as related]
            [client.search :as search]))

(enable-console-print!)

(defn domready
  [handler]
  (.addEventListener js/window "DOMContentLoaded" handler))

(defn init
  [e]
  (pagination/init)
  (search/init)
  (related/init))

(domready init)
