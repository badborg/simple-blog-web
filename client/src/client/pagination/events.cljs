(ns client.pagination.events
  (:require [clojure.core.async :refer [>! <! go go-loop chan]]
            [client.core.api :as api]
            [client.core.scroll :refer [on-scrolled-bottom]]
            [client.core.state :as cstate]
            [client.core.uri :refer [extract-uri]]))

(defn find-tag-path
  [path]
  (when path
    (re-find #"^/tag/([^\/]+)" path)))

(defn find-tag-id
  [path]
  (when-let [found (find-tag-path path)]
    (nth found 1)))

(defn find-api-name
  [path params]
  (let [found-s? (some? (get params :s))]
    (cond
      (and (= path "/") found-s?) :search
      (= path "/") :index
      (find-tag-path path) :tag)))

(defn index-posts
  [in]
  (let [out (chan)]
    (go-loop []
      (when-let [params (<! in)]
        (api/req :get "/api/posts"
                 params
                 #(go (when-let [posts (:posts %)]
                        (>! out {:posts posts
                                 :params params})))
                 #(go (>! out {:error true}))))
      (recur))
    out))

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

(defn tag-posts
  [id in]
  (let [out (chan)]
    (go-loop []
      (when-let [params (<! in)]
        (api/req :get (str "/api/tags/" id "/posts")
                 params
                 #(go (when-let [posts (:posts %)]
                        (>! out {:posts posts
                                 :params params})))
                 #(go (>! out {:error true}))))
      (recur))
    out))

(defn build-params
  [params in]
  (let [out (chan)]
    (go-loop [{:keys [page]
               :or {page 2}
               :as data} params]
      (let [{:keys [scroll]} (<! in)]
        (if scroll
          (do
            (>! out data)
            (recur (update data :page inc)))
          (recur data))))
    out))

(defn results
  [in]
  (let [out (chan)]
    (go-loop [data {:posts []}]
      (let [{:keys [posts params]} (<! in)
            updated-posts (concat (:posts data) posts)
            updated-data (-> data
                             (merge params)
                             (assoc :posts updated-posts)
                             (assoc :timestamp (js/Date.))
                             (cond->
                                 (< (count posts) 24) (assoc :all? true)))]
        (>! out updated-data)
        (recur updated-data)))
    out))

(defn void
  [in]
  (chan))

(defn posts-scrollable?
  [state-data]
  (not (:all? state-data)))

(defn set-posts-waiting
  [state-data]
  (assoc state-data :waiting? true))

(defn parse-int
  [v]
  (cond
    (integer? v) v
    (string? v) (some-> (re-find #"\d+" v)
                        js/parseInt)))

(defn sanitize-params
  [{:keys [s page] :as params}]
  (cond-> params
    page (update :page parse-int)))

(defn events
  [next-page-uri]
  (when next-page-uri
    (let [{:keys [path params]} (extract-uri next-page-uri sanitize-params)
          api-name (find-api-name path params)
          state cstate/pagination
          in (chan)
          retrieve (case api-name
                     :index index-posts
                     :search search-posts
                     :tag (partial tag-posts (find-tag-id path))
                     void)
          out (->> in
                   (build-params params)
                   retrieve
                   results)]
      (cstate/sync-pagination out)
      (on-scrolled-bottom
        (fn []
          (when (posts-scrollable? @state)
            (cstate/update-pagination set-posts-waiting)
            (go (>! in {:scroll true})))))
      {:state state})))
