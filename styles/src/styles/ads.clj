(ns styles.ads
  (:require [garden.def :refer [defstyles]]
            [styles.helpers :refer [em % px build-media]]
            [styles.posts :as posts]
            [styles.sideables :as sideables]
            [styles.wallables :as wallables]))

(def min-width 300)

(def min-height 250)

(def pad 40)

(def height
  (-> (* 2 posts/post-height)
      (- pad)))

(def margin
  (* 2 posts/post-margin))

(defstyles group1
  [:#ad1 :#ad3
   {:min-width (px min-width)
    :min-height (px min-height)
    :padding-top (px pad)
    :padding-bottom (px pad)}]
  [:#ad2
   {:float "left"
    :height (px height)
    :padding-top (px pad)
    :margin-top (em margin)
    :margin-bottom (em margin)
    :margin-left "auto"
    :margin-right "auto"}
   ]
  (build-media (sideables/media-ranges [:#ad2])))

(defstyles group2
  [:#ad4 :#ad5
   {:float "left"
    :height (px height)
    :padding-top (px pad)
    :margin-top (em margin)
    :margin-bottom (em margin)
    :margin-left "auto"
    :margin-right "auto"}]
  (build-media (wallables/media-ranges [:#ad4 :#ad5])))

(defstyles main
  group1
  group2)
