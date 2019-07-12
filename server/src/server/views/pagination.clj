(ns server.views.pagination
  (:require [server.permalink :refer [paginated-url]]))

(def prev-sym
  "&laquo;")

(def next-sym
  "&raquo;")

(defn paginate
  [path page & {:keys [next? params]
                :or {next? false
                     params {}}}]
  (let [next (inc page)
        prev (max 1
                  (dec page))
        prev? (not (= page prev))]
    [:div#paginate
     (when prev?
       [:a.prev {:href (paginated-url path prev params)}
        prev-sym])
     (when next?
       [:a.next {:href (paginated-url path next params)}
        next-sym])]))
