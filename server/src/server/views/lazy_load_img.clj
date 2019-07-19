(ns server.views.lazy-load-img
  (:require [server.blank-img :as blank-img]))

(defn lazy-load-img
  ([image-url]
   (lazy-load-img image-url {}))
  ([image-url opts]
   [:img (-> {:data-src image-url
              :src (blank-img/path-version)
              :class "lazy"}
             (merge opts))]))
