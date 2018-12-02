(ns styles.not-found
  (:require [garden.def :refer [defstyles]]
            [styles.colors :as colors]
            [styles.helpers :refer [em]]))

(defstyles main
  [:#not-found
   (-> {:font-size (em 12)}
       (colors/not-found))])
