(ns client.related.views
  (:require [rum.core :as rum]
            [client.core.views :refer [post-item]]))

(defn reactive-posts-data
  [state]
  {:posts (some-> state
                  (rum/cursor-in [:related :posts])
                  rum/react)})

(rum/defc main < rum/reactive
  [{:keys [state] :as events}]
  (let [{:keys [posts]} (reactive-posts-data state)]
    (list
      (map post-item posts))))

(defn mount
  [el events]
  (rum/mount (main events)
             el))
