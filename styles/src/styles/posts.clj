(ns styles.posts
  (:require [garden.def :refer [defstyles]]
            [garden.stylesheet :refer [at-media]]
            ;; [styles.ads :as ads]
            [styles.base :as base]
            [styles.colors :as colors]
            [styles.helpers :refer [px % em build-media]]
            [styles.wallables :as wallables]))

;; (def post-min-width 300)

(def post-height 200)

(def post-margin 0.2)

(def title-pads [20 25 30])

(def title-pad-h 1)

(defstyles post-title
  [:.post
   [:.title
    {:position "absolute"
     :bottom (px (nth title-pads 1))
     :height (em base/line-height)
     :overflow "hidden"
     :text-align "center"
     :padding-left (em title-pad-h)
     :padding-right (em title-pad-h)}
    [:a
     (colors/c-f)]]]
  (colors/setup-cs
    (fn [sel style]
      [(keyword (str ".post" sel))
       [:.title style]]))
  [".post:nth-child(3n)"
   [:.title
    {:bottom (px (nth title-pads 1))}]]
  [".post:nth-child(3n+1)"
   [:.title
    {:bottom (px (nth title-pads 0))}]]
  [".post:nth-child(3n+2)"
   [:.title
    {:bottom (px (nth title-pads 2))}]]
  [".post:nth-child(even)"
   [:.title
    {:right 0}]]
  [".post:nth-child(odd)"
   [:.title
    {:left 0}]])

(def post-media-ranges
  {[0 400] [:.post
            [:.title
             {:font-size (em 0.9)}]]
   })

(defstyles post
  [:.post
   {:height (px post-height)
    ;; :width (% 50)
    ;; :min-width (px post-min-width)
    :overflow "hidden"
    :display "inline-block"
    :position "relative"
    :margin-top (em post-margin)
    :margin-bottom (em post-margin)
    :margin-left "auto"
    :margin-right "auto"}
   [:img
    {:width (% 140)
     :to (px -10)
     :left (px -10)
     :border 0}]
   ]
  post-title
  (build-media post-media-ranges)
  (build-media (wallables/media-ranges [:.post])))

(defstyles media
  ;; (at-media
  ;;   {:screen :only
  ;;    :max-width "600px"}
  ;;   [:.post :#ad4 :#ad5
  ;;    {:width "100%"}])
  ;; (at-media
  ;;   {:screen :only
  ;;    :max-width "400px"}
  ;;   [:.post
  ;;    [:.title
  ;;     {:font-size "0.9em"}]])
  ;; (at-media
  ;;   {:screen :only
  ;;    :min-width "1100px"}
  ;;   [:.post :#ad4 :#ad5
  ;;    {:width "33.33%"}])
  ;; (at-media
  ;;   {:screen :only
  ;;    :min-width "1500px"}
  ;;   [:.post :#ad4 :#ad5
  ;;    {:width "25%"}])
  ;; (at-media
  ;;   {:screen :only
  ;;    :min-width "1900px"}
  ;;   [:.post :#ad4 :#ad5
  ;;    {:width "20%"}])
  )

(defstyles main
  [:.posts :#paginate
   ;; :#quick-results
   ;; {:width "100%"}
   (wallables/group-style)
   post
   ;; ads/group2
   ]
  ;; (at-media
  ;;   {:screen :only
  ;;    :max-width "600px"}
  ;;   [:#quick-results
  ;;    [:.close :.more
  ;;     {:font-size "1.5em"
  ;;      :line-height "0.8"
  ;;      :top "2em"}]
  ;;    [:.post
  ;;     {:top "1.1em"}]])
  )
