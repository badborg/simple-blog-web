(ns server.views.not-found
  (:require [server.views.layout :as layout]))

(def not-found-sym
  "&otimes;")

(defn main
  []
  (layout/main
    "404"
    nil
    [:div#not-found not-found-sym]))
