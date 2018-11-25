(ns styles.sideables
  (:require [styles.helpers :refer [add-sels % mergeable]]))

(defn media-ranges
  [sels]
  {[0 600] (add-sels sels
                     {:width (% 100)})
   [600 800] (add-sels sels
                     {:width (% 50)})
   [800 1100] (add-sels sels
                        {:width (% 33.33)})
   [1100 nil] (add-sels sels
                        {:width (% 25)})})
