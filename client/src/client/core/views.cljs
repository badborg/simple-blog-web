(ns client.core.views
  (:require [clojure.string :as s]))

(defn thumb_url
  [image_url]
  (some-> image_url
          (s/replace #"([^\.]+)$"
                     "thumbnail.$1")))

(defn color-class
  [id]
  (let [n (or (some-> id
                      (rem 8))
              0)]
    (str "c-" n)))

(defn post-item
  [{:keys [title slug image_url url id] :as post}]
  [:div.post
   {:class (color-class id)}
   [:div.title
    [:a {:href url}
     title]]
   [:a {:href url}
    [:img {:src image_url}]]])
