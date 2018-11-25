(ns styles.post-content
  (:require [garden.def :refer [defstyles]]
            [styles.colors :as colors]
            [styles.helpers :refer [em px]]))

(defstyles headings
  [:h1 :h2 :h3 :h4 :h5 :h6
   {:margin-bottom 0
    :margin-top 0}])

(defstyles lists
  [:ul :ol
   {:padding 0
    :margin-top 0
    :margin-bottom (em 1)
    :border-left colors/main-line}]
  [:li
   {:list-style-position "inside"
    :border-bottom colors/content-line
    :padding-left (em 1)}]
  [:li:first-child
   {:border-top colors/content-line}]
  [:ul
   [:li
    {:list-style-type "circle"}]])

(defstyles links
  [:a
   (colors/content-link)]
  [:a:hover
   (colors/content-link-bg)])

(defstyles main
  [:.content
   {:font-size (em 1)
    :letter-spacing 0
    :line-height 2.6
    :text-align "left"
    ;; :margin "0 auto"
    :margin-top 0
    :margin-bottom 0
    :margin-left "auto"
    :margin-right "auto"
    :padding 0
    :max-width (px 600)}
   headings
   lists
   links])
