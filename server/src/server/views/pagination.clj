(ns server.views.pagination
  (:require [ring.util.codec :refer [form-encode]]))

(defn index-url
  [path page params]
  (str path
       "?"
       (form-encode (assoc params
                           :page page))))

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
       [:a.prev {:href (index-url path prev params)}
        prev-sym])
     (when next?
       [:a.next {:href (index-url path next params)}
        next-sym])]))
