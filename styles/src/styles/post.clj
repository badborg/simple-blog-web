(ns styles.post
  (:require [garden.def :refer [defstyles]]
            [garden.stylesheet :refer [at-media]]
            [styles.colors :as colors]
            [styles.helpers :refer [em % px build-media]]
            [styles.post-content :as post-content]
            [styles.sideables :as sideables]))

(defstyles tags
  [:.tags
   [:.tag
    (->
      {:display "inline-block"
       :font-size (em 0.9)
       :padding-top 0
       :padding-bottom 0
       :padding-left (em 1)
       :padding-right (em 1)
       :margin (em 1)})
    [:a
     (colors/c-f)]]
   ])

(defstyles title
  [:h1.post
   {:font-size (em 1.2)
    :font-weight "normal"
    :letter-spacing 0
    :line-height 1.3
    :padding-top (em 0.2)
    :padding-bottom (em 0.2)
    :padding-left 0
    :padding-right 0
    :position "absolute"
    :margin-top (em 0.3)
    :margin-bottom (em 0.3)
    :margin-left 0
    :margin-right 0
    }])

(defstyles post
  [:.post
   {:float "left"}
   post-content/main
   tags
   [:.image
    [:img
     {:width (% 100)}]]
   ])

(defstyles related
  (build-media
    (sideables/media-ranges
      [:.related [:.posts [:.post]]])))

(def post-media-ranges
  {[0 800] [:.post
            {:width (% 100)}]
   [800 1100] [:.post
               {:width (% 66.66)}]
   [1100 nil] [:.post
               {:width (% 50)}]})

(def content-media-ranges
  {[0 380] [:.post
            [:.content
             {:max-width (px 300)}]]
   [380 450] [:.post
              [:.content
               {:max-width (px 350)}]]
   [450 550] [:.post
              [:.content
               {:max-width (px 400)}]]
   [550 650] [:.post
              [:.content
               {:max-width (px 500)}]]
   [650 800] [:.post
              [:.content
               {:max-width (px 600)}]]
   [800 1100] [:.post
               [:.content
                {:max-width (px 500)}]]
   [1100 1200] [:.post
                [:.content
                 {:max-width (px 450)}]]
   [1200 1300] [:.post
                [:.content
                 {:max-width (px 500)}]]
   [1300 nil] [:.post
               [:.content
                {:max-width (px 600)}]]
   })

(def font-media-ranges
  {[0 450] (list
             [:h1.post
              {:font-size (em 1)}]
             [:.post
              [:.content
               {:font-size (em 0.8)}]
              [:.tags
               [:.tag
                {:font-size (em 0.8)}]]])
   [450 600] (list
               [:h1.post
                {:font-size (em 1.1)}]
               [:.post
                [:.content
                 {:font-size (em 0.9)}]
                [:.tags
                 [:.tag
                  {:font-size (em 0.9)}]]])})

(defstyles main
  title
  post
  related
  (build-media content-media-ranges)
  (build-media post-media-ranges)
  (build-media font-media-ranges)
  )
