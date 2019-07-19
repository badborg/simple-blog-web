(ns client.core.state
  (:require [clojure.core.async :refer [<! >! go-loop]]))

(defn sync-data
  [state in]
  (go-loop []
    (when-let [data (<! in)]
      (reset! state data))
    (recur)))

(defn update-data
  [state update-fn]
  (swap! state update-fn))

(def pagination
  (atom {}))

(def sync-pagination
  (partial sync-data pagination))

(def update-pagination
  (partial update-data pagination))

(def search
  (atom {}))

(def sync-search
  (partial sync-data search))

(def update-search
  (partial update-data search))

(def related
  (atom {}))

(def sync-related
  (partial sync-data related))
