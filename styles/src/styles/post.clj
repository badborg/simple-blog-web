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
       :font-size "0.9em"
       :padding "0 1em"
       :margin "1em"})
    [:a
     (colors/c-f)]]
   ])

(defstyles title
  [:h1.post
   {:font-size "1.2em"
    :font-weight "normal"
    :letter-spacing "0"
    :line-height "1.3"
    :padding "0.2em 0"
    :position "absolute"
    :margin "0.3em 0"
    }])

(defstyles post
  [:.post
   {
    ;; :width "60%"
    :float "left"}
   post-content/main
   tags
   [:.image
    [:img
     {:width "100%"}]]
   ])

(defstyles related
  (build-media
    (sideables/media-ranges
      [:.related [:.posts [:.post]]])))

(def post-media-ranges
  {
   ;; [0 800] [:.post
   ;;          {:width (% 100)}]
   ;; [800 900] [:.post
   ;;             {:width (% 60)}]
   ;; [900 1300] [:.post
   ;;             {:width (% 66.66)}]
   ;; [1300 nil] [:.post
   ;;             {:width (% 50)}]

   [0 800] [:.post
            {:width (% 100)}]
   [800 1200] [:.post
               {:width (% 66.66)}]
   [1200 nil] [:.post
               {:width (% 50)}]
   })

(def content-media-ranges
  {[0 450] [:.post
            [:.content
             {:max-width "300px"}]]
   [450 550] [:.post
              [:.content
               {:max-width "400px"}]]
   [550 650] [:.post
              [:.content
               {:max-width "500px"}]]
   [650 800] [:.post
              [:.content
               {:max-width "600px"}]]
   [800 900] [:.post
              [:.content
               {:max-width "400px"}]]
   [900 1050] [:.post
               [:.content
                {:max-width "500px"}]]
   })

(def font-media-ranges
  {[0 400] (list
             [:h1.post
              {:font-size "1em"}]
             [:.post
              [:.content
               {:font-size "0.8em"}]
              [:.tags
               [:.tag
                {:font-size "0.8em"}]]])
   [400 450] [:.post
              [:.content
               {:font-size "0.9em"}]]})

(defstyles main
  title
  post
  related
  (build-media content-media-ranges)
  (build-media post-media-ranges)
  (build-media font-media-ranges)

  #_(at-media
    {:screen :only
     :max-width "1050px"}
    [:.post
     [:.content
      {:max-width "500px"}]])
  #_(at-media
    {:screen :only
     :max-width "900px"}
    [:.post
     [:.content
      {:max-width "400px"}]])
  #_(at-media
    {:screen :only
     :max-width "800px"}
    [:.post
     {:width "100%"}
     [:.content
      {:max-width "600px"}]]
    #_[:.related
     [:.posts
      [:.post :#ad2
       {:width "50%"}]]]
    )
  #_(at-media
    {:screen :only
     :max-width "650px"}
    [:.post
     [:.content
      {:max-width "500px"}]])
  #_(at-media
    {:screen :only
     :max-width "600px"}
    [:.post
     {:width "100%"}]
    #_[:.related
     [:.posts
      [:.post :#ad2
       {:width "100%"}]]])
  #_(at-media
    {:screen :only
     :max-width "550px"}
    [:.post
     [:.content
      {:max-width "400px"}]])
  #_(at-media
    {:screen :only
     :max-width "450px"}
    [:.post
     [:.content
      {:max-width "300px"
       :font-size "0.9em"}]])
  #_(at-media
    {:screen :only
     :max-width "400px"}
    [:h1.post
     {:font-size "1em"}]
    [:.post
     [:.content
      {:font-size "0.8em"}]
     [:.tags
      [:.tag
       {:font-size "0.8em"}]]])
 #_ (at-media
    {:screen :only
     :min-width "1300px"}
    [:.post
     {:width "50%"}]
    #_[:.related
     [:.posts
      [:.post :#ad2
       {:width "25%"}]]]))
