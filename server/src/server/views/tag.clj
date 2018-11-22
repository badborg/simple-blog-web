(ns server.views.tag
  (:require [environ.core :refer [env]]
            [server.views.ads :as ads]
            [server.views.color-class :refer [color-class]]
            [server.views.layout :as layout]
            [server.views.pagination :refer [paginate]]
            [server.views.posts :as posts]))

(def default-per-page
  (some-> (env :per-page)
          Integer/parseInt))

(defn main
  [tag posts page]
  (let [next? (-> (count posts)
                  (>= default-per-page))]
    (layout/main
      (:name tag)
      (str "tag " (color-class (:id tag)))
      (posts/posts-list posts
                        #(let [total (count %)]
                           (cond->> %
                             (> total 12) (ads/insert-at 12
                                                         (ads/ad4))
                             (> total 23) (ads/insert-at 23
                                                         (ads/ad5)))))
      (paginate (:url tag) page :next? next?))))
