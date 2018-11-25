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
    {[0 800] [:h1
              {:font-size (em 1.7)}]
     [0 500] [:h1
              {:font-size (em 1.5)}]
     [0 400] [:h1
              {:font-size (em 1.2)}]})
  ;; (at-media
  ;;   {:screen :only
  ;;    :max-width "800px"}
  ;;   [:h1
  ;;    {:font-size "1.7em"}])
  ;; (at-media
  ;;   {:screen :only
  ;;    :max-width "500px"}
  ;;   [:h1
  ;;    {:font-size "1.5em"}])
  ;; (at-media
  ;;   {:screen :only
  ;;    :max-width "400px"}
  ;;   [:h1
  ;;    {:font-size "1.2em"}])
  )
