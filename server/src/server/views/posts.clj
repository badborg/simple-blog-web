(ns server.views.posts
  (:require [clojure.string :as s]
            [server.views.color-class :refer [color-class]]
            [server.views.lazy-load-img :refer [lazy-load-img]]))

(defn thumb_url
  [image_url]
  (some-> image_url
          (s/replace #"([^\.]+)$"
                     "thumbnail.$1")))

(defn post-item
  [{:keys [title slug image_url url id] :as post}]
  [:div.post
   {:class (color-class id)}
   [:div.title
    [:a {:href url}
     title]]
   [:a {:href url}
    (lazy-load-img image_url)]])

(defn posts-list
  ([posts]
   (posts-list posts nil))
  ([posts processor]
   [:div.posts
    (cond->> posts
      true (map post-item)
      processor processor)]))
