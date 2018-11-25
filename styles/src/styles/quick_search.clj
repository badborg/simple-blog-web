(ns styles.quick-search
  (:require [garden.def :refer [defstyles]]
            [styles.colors :as colors]
            [styles.helpers :refer [neg em %]]
            [styles.posts :as posts]
            [styles.wallables :as wallables]))

(def font-size 1.2)

(def close-width 2)

(def offset-top 2.1)

(def z-index-close 20)

(def z-index-content 3)

(defstyles controls
  [:.close :.more
   (->
     {:display "inline-block"
      :font-size (em font-size)
      :font-weight "bold"
      :line-height 1
      :padding 0}
     (colors/navigation))]
  [:.close
   {:position "fixed"
    :top (em offset-top)
    :left (% 50)
    :margin-left (neg (em (-> close-width
                              (/ 2)
                              float)))
    :width (em close-width)
    :border-radius (em 2)
    :z-index z-index-close}]
  [:.more
   {:width (% 100)}]
  [:.more :.post
   {:z-index z-index-content}])

(defstyles main
  [:#quick-results
   (->
     (wallables/group-style)
     (colors/navigation))
   posts/post
   controls
   ]
  )
