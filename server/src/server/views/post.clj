(ns server.views.post
  (:require [server.views.ads :as ads]
            [server.views.color-class :refer [color-class]]
            [server.views.layout :as layout]
            [server.views.lazy-load-img :refer [lazy-load-img]]
            [server.views.posts :as posts]
            [server.views.structured-data :refer [json-ld]]))

(defn post-album
  [title images]
  (let [img-urls (map :url images)
        sorted-urls (concat (rest img-urls)
                            [(first img-urls)])]
    [:div.images
     (for [[idx img] (zipmap (range) sorted-urls)
           :let [alt (str title " " idx)]]
       [:div.image
        (lazy-load-img img {:alt alt})])]))

(defn post-tags
  [tags]
  [:div.tags
   (for [{:keys [name url id] :as tag} tags]
     [:div.tag
      {:class (color-class id)}
      [:a {:href url}
       name]])])

(defn related-posts
  [id posts]
  [:div.related
   (posts/posts-list posts
                     #(ads/insert-at (some-> (ads/insert-ad-pos (count %)
                                                                id)
                                             (max 2))
                                     (ads/ad2)
                                     %))
   [:div#more-related.posts
     {:data-id id}]])

(defn post-details
  [{:keys [id title image_url content images tags]}]
  (let [ad? (ads/insert-ad? id)]
    [:div.post
     [:div.image
      [:img {:src image_url
             :alt title}]]
     (when ad?
       (ads/ad1))
     [:div.content
      content]
     (when-not ad?
       (ads/ad1))
     (post-album title images)
     (when ad?
       (ads/ad3))
     (post-tags tags)
     (when-not ad?
       (ads/ad3))]))

(defn main
  [{:keys [id url] :as post} r-posts]
  (layout/main
    {:title (:title post)
     :class (str "post " (color-class id))
     :canonical url
     :structured-data (json-ld post)}
    (post-details post)
    (related-posts id r-posts)))
