(ns server.views.search
  (:require [clojure.string :as s]
            [environ.core :refer [env]]
            [hiccup.util :refer [escape-html]]
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
      {:title (str "\"" search-phrase "\"")
       :class "search"
       :noindex true}
      (posts/posts-list posts)
      (paginate "/" page :next? next? :params params))))
