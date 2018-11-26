(ns styles.sideables
  (:require [styles.helpers :refer [add-sels % mergeable]]))

(def widths [100 50 33.33 25])

(defn media-ranges
  [sels]
  {[0 600] (add-sels sels
                     {:width (% (nth widths 0))})
   [600 800] (add-sels sels
                     {:width (% (nth widths 1))})
   [800 1100] (add-sels sels
                        {:width (% (nth widths 2))})
   [1100 nil] (add-sels sels
                        {:width (% (nth widths 3))})})
