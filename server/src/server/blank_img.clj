(ns server.blank-img
  (:require [hiccup.core :refer [html]]))

(defn path
  []
  "/blank.svg")

(defn content-type
  []
  "image/svg+xml")

(defn content
  []
  (html
    [:svg {:width "1"
           :height "1"
           :xmlns "http://www.w3.org/2000/svg"
           :version "1.1"}
     [:rect {:x "0"
             :y "0"
             :width "1"
             :height "1"
             :fill-opacity "0"}]]))

(def hours->secs
  (partial * 3600))

(defn response
  []
  {:headers {"Content-Type" (content-type)
             "Cache-Control" (str "max-age=" (hours->secs 2400))}
   :body (content)})
