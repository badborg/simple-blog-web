(ns server.views.tag
  (:require [environ.core :refer [env]]
            [server.permalink :refer [paginated-url]]
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
      {:title (:name tag)
       :class (str "tag " (color-class (:id tag)))
       :canonical (paginated-url (:url tag)
                                 page)}
      (posts/posts-list posts)
      (paginate (:url tag) page :next? next?))))
