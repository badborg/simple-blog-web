(ns client.related.events
  (:require [goog.dom :as dom]
            [clojure.string :as s]
            [clojure.core.async :refer [>! <! go go-loop chan timeout]]
            [client.core.api :as api]
            [client.core.scroll :refer [scrolled-to?]]
            [client.core.state :as cstate]))

(defn related-posts
  [id in]
  (let [out (chan)]
    (go-loop []
      (when-let [params (<! in)]
        (api/req :get (str "/api/posts/" id "/related")
                 params
                 #(go (when-let [posts (:posts %)]
                        (>! out {:posts posts})))
                 #(go (>! out {:error true}))))
      (recur))
    out))

(defn build-params
  [state in]
  (let [out (chan)]
    (go-loop []
      (let [{:keys [scroll]} (<! in)]
        (when scroll
          (let [post-ids (->> (-> @state :related :posts)
                              (map :id)
                              distinct
                              (s/join ",")
                              not-empty)]
            (>! out {:excluded post-ids}))))
      (recur))
    out))

(defn results
  [in]
  (let [out (chan)]
    (go-loop [data {:posts []}]
      (let [{:keys [posts]} (<! in)
            updated-posts (concat (:posts data) posts)
            updated-data (-> data
                             (assoc :posts updated-posts)
                             (assoc :timestamp (js/Date.)))]
        (>! out updated-data)
        (recur updated-data)))
    out))

(defn events
  [el id]
  (when id
    (let [in (chan)
          state cstate/related
          out (->> in
                   (build-params state)
                   (related-posts id)
                   results)
          done? (atom false)
          mount-el el
          related-el (dom/getElementByClass "related")
          find-last-el #(or (->> mount-el
                                 dom/getLastElementChild)
                            (->> related-el
                                 (dom/getElementByClass "posts")
                                 dom/getLastElementChild))
          last-el (atom (find-last-el))
          scroll-count (atom 0)]
      (cstate/sync-related out)
      (add-watch state :state-watcher
                 (fn [_ _ _ updated-state]
                   (reset! last-el (find-last-el))))
      (-> js/window
          (.addEventListener "scroll"
                             (fn []
                               (when (and (not @done?)
                                          (scrolled-to? @last-el)
                                          (<= @scroll-count
                                              10))
                                 (go (>! in {:scroll true})
                                     (<! (timeout 100))
                                     (reset! done? false))
                                 (reset! last-el nil)
                                 (swap! scroll-count inc)
                                 (reset! done? true)))))
      {:state state})))
