(ns styles.wallables
  (:require [styles.helpers :refer [add-sels % mergeable]]))

(defn media-ranges
  [sels]
  {[0 600] (add-sels sels
                     {:width (% 100)})
   [600 1100] (add-sels sels
                        {:width (% 50)})
   [1100 1500] (add-sels sels
                         {:width (% 33.33)})
   [1500 1900] (add-sels sels
                         {:width (% 25)})
   [1900 nil] (add-sels sels
                        {:width (% 20)})})

(def group-style
  (mergeable {:width (% 100)}))
