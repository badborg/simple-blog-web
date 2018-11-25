g(ns styles.headings
  (:require [garden.def :refer [defstyles]]
            [garden.stylesheet :refer [at-media]]
            [styles.colors :as colors]
            [styles.helpers :refer [em build-media]]))

(def h1-size 2)

(defstyles main
  [:h1
   (-> {:font-size (em h1-size)
        :margin 0}
       (colors/h1))]
  [:h1.search
   {:font-style "oblique"}]
  (build-media
    {[0 400] [:h1
              {:font-size (em 1.2)}]
     [400 500] [:h1
                {:font-size (em 1.5)}]
     [500 800] [:h1
                {:font-size (em 1.7)}]})
  )
