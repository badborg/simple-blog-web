(ns styles.navigation
  (:require [garden.def :refer [defstyles]]
            [styles.base :as base]
            [styles.colors :as colors]
            [styles.helpers :refer [em %]]))

(def height 3)

(def logo-height 2)

(def logo-pad
  (-> (- height logo-height)
      (/ 2)
      float))

(def input-pad
  (-> (- height base/input-height)
      (/ 2)
      float))

(def z-index 10)

(defstyles main
  [:body
   {:padding-top (em height)}]
  [:#navigation
   (-> {:width (% 100)
        :height (em height)
        :overflow "hidden"
        :text-align "left"
        :position "fixed"
        :top 0
        :left 0
        :z-index z-index}
       (colors/navigation))
   [:#home
    (-> {:width (% 33.33)
         :height (em height)
         :line-height (em height)
         :display "inline-block"
         :text-align "center"
         :vertical-align "top"}
        (colors/navigation))
    [:img
     {:height (em logo-height)
      :padding-top (em logo-pad)
      :padding-bottom (em logo-pad)}]]
   [:#search
    {:width (% 66.66)
     :padding-top (em input-pad)
     :padding-bottom (em input-pad)
     :display "inline-block"
     :vertical-align "top"}
    [:input
     {:width (% 70)}]
    ]])
