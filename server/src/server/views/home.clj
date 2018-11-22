(ns server.views.home
  (:require [environ.core :refer [env]]
            [server.views.ads :as ads]
            [server.views.layout :as layout]
            [server.views.pagination :refer [paginate]]
            [server.views.posts :as posts]))

(def default-per-page
  (some-> (env :per-page)
          Integer/parseInt))

(defn main
  [posts page]
  (let [next? (-> (count posts)
                  (>= default-per-page))]
    (layout/main
      nil
      nil
      (posts/posts-list posts
                        #(let [total (count %)]
                           (cond->> %
                             (> total 12) (ads/insert-at 12
                                                         (ads/ad4))
                             (> total 23) (ads/insert-at 23
                                                         (ads/ad5)))))
      (paginate "/" page :next? next?))))
