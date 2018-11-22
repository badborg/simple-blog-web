(ns client.core.state
  (:require [clojure.core.async :refer [<! >! go-loop]]))

(defn create
  []
  (atom {}))

(defn sync-data
  [group in state]
  (go-loop []
    (when-let [data (<! in)]
      (swap! state assoc group data))
    (recur)))

(defn get-data
  [group state]
  (get @state group))

(defn update-data
  [group state update-fn]
  (swap! state update group update-fn))

(def sync-pagination
  (partial sync-data :pagination))

(def get-pagination
  (partial get-data :pagination))

(def update-pagination
  (partial update-data :pagination))

(def sync-search
  (partial sync-data :search))

(def get-search
  (partial get-data :search))

(def update-search
  (partial update-data :search))

(def sync-related
  (partial sync-data :related))
