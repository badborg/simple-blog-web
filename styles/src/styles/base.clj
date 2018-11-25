(ns styles.base
  (:require [garden.def :refer [defstyles]]
            [styles.colors :as colors]
            [styles.helpers :refer [em %]]))

(def font-size 1.3)

(def line-height 1.6)

(def spacing 0.1)

(def input-pad 0.2)

(def input-pad-h 1)

(def input-height
  (+ line-height
     (* 2 input-pad)))

(defstyles main
  [:body
   (-> {:line-height line-height
        :font-size (em font-size)
        :letter-spacing (em spacing)
        :font-family "\"Andale Mono\", AndaleMono, monospace;"
        :text-align "center"
        :width (% 100)
        :overflow-x "hidden"}
       (colors/regular))]
  [:button :input :select :textarea :optgroup
   {:font-weight "bold"
    :padding (em input-pad)}]
  [:input
   (-> {:text-align "left"
        :padding-left (em input-pad-h)
        :padding-right (em input-pad-h)}
       (colors/input))]
  [:button
   (-> {:font-weight "bold"})]
  [:a
   (colors/link)])
