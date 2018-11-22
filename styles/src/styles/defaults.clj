(ns styles.defaults
  (:require [garden.def :refer [defstyles defcssfn]]))

(defcssfn rgba)

(defstyles main
  [:html :body
   {:margin "0"}]
  [:html
   {:-webkit-appearance "none"
    :-webkit-text-size-adjust "100%"
    :-webkit-tap-highlight-color (rgba 0 0 0 0)}]
  [:button :input :select :textarea :optgroup
   {:font-family "inherit"
    :font-size "inherit"
    :line-height "inherit"
    :letter-spacing "inherit"
    :background-color "inherit"
    :padding "0"
    :margin "0"
    :border-radius "0"
    :border-top "0"
    :border-left "0"
    :border-right "0"
    :border-bottom "0"}]
  [:button :select
   {:text-transform "none"}]
  [:textarea
   {:overflow "auto"}]
  [:img
   {:border-style "none"}]
  [:a
   {:text-decoration "none"
    :background-color (rgba 0 0 0 0)}]
  [:b :strong
   {:font-weight "bolder"}]
  ["[contenteditable]:focus"
   {:outline "none"}])
