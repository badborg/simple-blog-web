(ns styles.colors
  (:require [garden.def :refer [defcssfn]]
            [styles.helpers :refer [mergeable]]))

(defcssfn radial-gradient)

(defcssfn rgba)

(defn radial-bg
  [color bg]
  {:color color
   :background (radial-gradient "closest-side"
                                bg
                                (rgba 255 255 255 0))})

(def regular
  (mergeable {:color "DarkSlateGray"
              :background-color "Snow"}))

(def input
  (mergeable {:color "DarkSlateBlue"
              :background-color "Cornsilk"}))

(def link
  (mergeable {:color "MediumBlue"}))

(def h1
  (mergeable {:color "DimGray"}))

(def main-line
  "0.2em double Wheat")

;; (def sub-line
;;   "0.1em dotted Coral")

;; (def sub-line-strong
;;   "0.2em double Plum")

(def content-line
  "0.1em dotted SkyBlue")

(def content-link
  (mergeable {:color "MidnightBlue"
              :border-bottom "0.1em solid Khaki"}))

(def content-link-bg
  (mergeable (radial-bg "inherit"
                        "Khaki")))

(def colors-list
  ["Chocolate"
   "LightSeaGreen"
   "GoldenRod"
   "MediumPurple"
   "RoyalBlue"
   "Crimson"
   "LightSlateGray"
   "OliveDrab"
   ])

(def c-f
  (mergeable {:color "Snow"}))

(defn c
  [n]
  (->
    {:background-color (nth colors-list n)
     :text-shadow "0 0 4px black"}
    (c-f)))

(defn setup-cs
  ([]
   (setup-cs (fn [n-sel n-style]
               [(keyword n-sel)
                n-style])))
  ([style-fn]
   (for [n (range (count colors-list))]
     (style-fn (str ".c-" n) (c n)))))

(def navigation
  (mergeable {:background-color "Tan"
              :color "Snow"}))

(def notice
  (mergeable {:color "LightSkyBlue"}))

(def not-found
  (mergeable {:color "LightCoral"}))
