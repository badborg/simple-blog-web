(ns client.pagination.views
  (:require [goog.string :refer [unescapeEntities]]
            [rum.core :as rum]
            [client.core.views :refer [post-item]]))

(defn reactive-posts-data
  [state]
  {:posts (some-> state
                  (rum/cursor :posts)
                  rum/react)
   :waiting? (some-> state
                     (rum/cursor :waiting?)
                     rum/react)})

(def loading-sym
  (unescapeEntities "&ctdot;"))

(rum/defc main < rum/reactive
  [{:keys [state] :as events}]
  (let [{:keys [posts waiting?]} (reactive-posts-data state)]
    (list
      (map post-item posts)
      (when waiting?
        [:div.loading loading-sym]))))

(defn mount
  [el events]
  (rum/mount (main events)
             el))
