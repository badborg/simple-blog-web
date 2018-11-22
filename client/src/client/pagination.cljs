(ns client.pagination
  (:require [goog.dom :as dom]
            [client.pagination.views :as views]
            [client.pagination.events :refer [events]]))

(defn init
  []
  (when-let [el (-> js/document
                    (.getElementById "paginate"))]
    (let [next-page-uri (some-> (dom/getElementByClass "next" el)
                                (.getAttribute "href"))]
      (views/mount el
                   (events next-page-uri)))))
