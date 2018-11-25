(ns styles.ads
  (:require [garden.def :refer [defstyles]]
            [styles.helpers :refer [em % px build-media]]
            [styles.posts :as posts]
            [styles.sideables :as sideables]
            [styles.wallables :as wallables]))

(def min-width 300)

(def min-height 250)

(def pad 40)

;; (def z-index 2)

(def height
  (-> (* 2 posts/post-height)
      (- pad)))

(def margin
  (* 2 posts/post-margin))

(defstyles group1
  [:#ad1 :#ad3
   {:min-width (px min-width)
    :min-height (px min-height)
    ;; :z-index z-index
    ;; :overflow "hidden"
    }]
  [:#ad2
   {:float "left"
    ;; :min-width (px min-width)
    ;; :min-height (px height)
    :height (px height)
    :padding-top (px pad)
    ;; :margin "0.4em auto"
    :margin-top (em margin)
    :margin-bottom (em margin)
    :margin-left "auto"
    :margin-right "auto"
    ;; :z-index z-index
    ;; :overflow "hidden"
    }
   ]
  (build-media (sideables/media-ranges [:#ad2])))

(defstyles group2
  [:#ad4 :#ad5
   {:float "left"
    ;; :width (% 50)
    :min-width (px min-width)
    :height (px height)
    :padding-top (px pad)
    :margin-top (em margin)
    :margin-bottom (em margin)
    :margin-left "auto"
    :margin-right "auto"
    ;; :z-index z-index
    ;; :overflow "hidden"
    }]
  (build-media (wallables/media-ranges [:#ad4 :#ad5])))

(defstyles main
  group1
  group2)
