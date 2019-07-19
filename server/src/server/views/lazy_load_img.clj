(ns server.views.lazy-load-img)

(defn lazy-load-img
  ([image-url]
   (lazy-load-img image-url {}))
  ([image-url opts]
   [:img (-> {:data-src image-url
              :src "/blank.svg?v=0.1.0"
              :class "lazy"}
             (merge opts))]))
