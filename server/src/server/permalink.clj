(ns server.permalink
  (:require [clj-time.core :as t]
            [clj-time.format :as tf]
            [environ.core :refer [env]]
            [ring.util.codec :refer [form-encode]]))

(def scheme
  (env :permalink-scheme))

(defn get-date
  [date-string]
  (let [formatter (.withZone (tf/formatter :date-time-no-ms)
                             (t/time-zone-for-offset +7))]
    (tf/parse formatter date-string)))

(defn date-path-format
  [date]
  (let [formatter (.withZone (tf/formatter "yyyy/MM/dd")
                             (t/time-zone-for-offset +7))]
    (tf/unparse formatter date)))

(defn date-scheme
  [id date]
  (str "/"
       (-> date
           get-date
           date-path-format)
       "/"
       id))

(defn slug-scheme
  [slug]
  (str "/"
       slug))

(defn post
  [{:keys [id slug date] :as post}]
  (case scheme
    "date" (date-scheme id date)
    (slug-scheme slug)))

(defn post-path
  []
  (case scheme
    "date" "/:year/:month/:date/:id"
    "/:id"))

(defn tag
  [{:keys [slug] :as tag}]
  (str "/tag/"
       slug))

(defn tag-path
  []
  "/tag/:id")

(defn non-root-page?
  [page]
  (and page (> page 1)))

(defn paginated-url
  ([url page]
   (paginated-url url page {}))
  ([url page params]
   (let [query (cond-> params
                 (non-root-page? page) (assoc :page page))]
     (cond-> nil
       url (str url)
       (not-empty query) (str "?" (form-encode query))))))
