(ns styles.core
  (:require [garden.def :refer [defstyles]]
            [styles.colors :as colors]
            [styles.defaults :as defaults]
            [styles.headings :as headings]
            [styles.ads :as ads]
            [styles.base :as base]
            [styles.navigation :as navigation]
            [styles.post :as post]
            [styles.posts :as posts]
            [styles.quick-search :as quick-search]))

(defstyles main
  defaults/main
  base/main
  (colors/setup-cs)
  [:.notice
   (colors/notice)]
  headings/main
  navigation/main
  quick-search/main
  ads/main
  posts/main
  post/main
  )
