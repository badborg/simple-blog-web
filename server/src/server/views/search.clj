(ns server.views.search
  (:require [clojure.string :as s]
            [environ.core :refer [env]]
            [hiccup.util :refer [escape-html]]
            [server.views.ads :as ads]
            [server.views.layout :as layout]
            [server.views.pagination :refer [paginate]]
            [server.views.posts :as posts]))

(def default-per-page
  (some-> (env :per-page)
          Integer/parseInt))

(defn main
  [terms posts page params]
  (let [next? (-> (count posts)
                  (>= default-per-page))
        search-phrase (escape-html (:s params))]
    (layout/main
      (str "\"" search-phrase "\"")
      "search"
      (posts/posts-list posts
                        #(let [total (count %)]
                           (cond->> %
                             (> total 12) (ads/insert-at 12
                                                         (ads/ad4))
                             (> total 23) (ads/insert-at 23
                                                         (ads/ad5)))))
      (paginate "/" page :next? next? :params params))))
