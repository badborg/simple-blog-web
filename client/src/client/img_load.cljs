(ns client.img-load
  (:require [goog.dom.classlist :as classlist]
            [goog.dom.dataset :as dataset]
            [clojure.core.async :refer [>! <! go go-loop chan timeout]]
            [client.core.scroll :refer [scrolled-to?]]
            [client.core.state :as cstate]
            [client.core.views :refer [lazy-class-name]]))

(defn find-lazy-load-images
  []
  (some->> (.querySelectorAll js/document (str "img." lazy-class-name))
           (.call js/Array.prototype.slice)
           js->clj))

(defn swap-image-source
  [image]
  (when-let [src (dataset/get image "src")]
    (set! (.-src image) src)
    (classlist/remove image lazy-class-name)
    (dataset/remove image "src")))

(defn load-image
  [in]
  (let [out (chan)]
    (go-loop []
      (when-let [image (<! in)]
        (when (scrolled-to? image)
          (swap-image-source image)
          (>! out image)))
      (recur))
    out))

(defn set-image-loaded
  [in images]
  (go-loop []
    (when-let [image (<! in)]
      (swap! images #(remove #{image} %)))
    (recur)))

(defn create-load-images-handler
  []
  (let [images (atom (find-lazy-load-images))
        to-load-img (chan)]
    (-> to-load-img
        load-image
        (set-image-loaded images))
    (fn []
      (go
        (<! (timeout 100))
        (doseq [image @images]
          (>! to-load-img image))))))

(def events
  ["scroll" "orientationchange" "resize"])

(defn remove-listener
  [listener]
  (let [lsn @listener]
    (doseq [event events]
      (.removeEventListener js/window event lsn)))
  listener)

(defn add-listener
  [listener]
  (let [lsn @listener]
    (lsn)
    (doseq [event events]
      (.addEventListener js/window event lsn)))
  listener)

(defn reset-listener
  [listener]
  (reset! listener (create-load-images-handler))
  listener)

(defn update-listener
  [listener]
  (-> listener
      remove-listener
      reset-listener
      add-listener))

(defn add-update-watchers
  [listener states]
  (doseq [[state-name state] states]
    (add-watch state (keyword (str "img-load-watcher-" (name state-name)))
               #(go
                 (<! (timeout 100))
                 (update-listener listener))))
  listener)

(defn init
  []
  (-> (atom (create-load-images-handler))
      add-listener
      (add-update-watchers {:pagination cstate/pagination
                            :related  cstate/related
                            :search cstate/search})))
