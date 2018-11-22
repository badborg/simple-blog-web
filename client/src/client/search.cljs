(ns client.search
  (:require [client.search.views :as views]
            [client.search.events :refer [events]]))

(defn init
  []
  (let [input-el (-> js/document
                     (.getElementById "search"))
        results-el (-> js/document
                    (.getElementById "quick-results"))]
    (when (and input-el results-el)
      (views/mount input-el results-el (events)))))
