(ns server.views.not-found
  (:require [server.views.layout :as layout]))

(def not-found-sym
  "&otimes;")

(defn main
  []
  (layout/main
    {:title "404"
     :noindex true}
    [:div#not-found not-found-sym]))
