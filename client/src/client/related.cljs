(ns client.related
  (:require [client.related.views :as views]
            [client.related.events :refer [events]]))

(defn init
  []
  (when-let [mount-el (-> js/document
                          (.getElementById "more-related"))]
    (let [post-id (.getAttribute mount-el "data-id")]
      (views/mount mount-el
                   (events mount-el post-id)))))
