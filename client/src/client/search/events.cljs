(ns client.search.events
  (:require [clojure.core.async :refer [>! <! go go-loop chan timeout]]
            [client.core.api :as api]
            [client.core.state :as cstate]))

(defn search-posts
  [in]
  (let [out (chan)]
    (go-loop []
      (when-let [params (<! in)]
        (api/req :get "/api/search"
                 params
                 #(go (when-let [posts (:results %)]
                        (>! out {:posts posts
                                 :params params})))
                 #(go (>! out {:error true}))))
      (recur))
    out))

(defn build-params
  [in]
  (let [out (chan)]
    (go-loop []
      (when-let [{:keys [search] :as req} (<! in)]
        (>! out {:s search}))
      (recur))
    out))

(defn results
  [in]
  (let [out (chan)]
    (go-loop []
      (when-let [{:keys [posts params]} (<! in)]
        (>! out {:posts posts
                 :search (:s params)
                 :timestamp (js/Date.)}))
      (recur))
    out))

(defn events
  []
  (let [state cstate/search
        in (chan)
        out (-> in
                build-params
                search-posts
                results)]
    (cstate/sync-search out)
    (add-watch state :results-watcher
               #(.scrollTo js/window 0 0))
    {:state state
     :set-search #(go (>! in %))}))
