(ns styles.wallables
  (:require [styles.helpers :refer [add-sels px % mergeable]]))

(defn media-ranges
  [sels]
  {[0 600] (add-sels sels
                     {:width (% 100)})
   [600 900] (add-sels sels
                       {:width (% 50)})
   [900 1200] (add-sels sels
                        {:width (% 33.33)})
   [1200 1600] (add-sels sels
                         {:width (% 25)})
   [1600 nil] (add-sels sels
                        {:width (% 20)})})

(def group-style
  (mergeable {:width (% 100)}))
