(ns server.views.structured-data
  (:require [cheshire.core :refer [generate-string]]
            [server.views.structured-data.how-to :as how-to]))

(defn image
  [url]
  (when url
    {"image" {"@type" "ImageObject"
              "url" url}}))

(defn how-to?
  [type]
  (= type "how-to"))

(defn json-ld
  [{:keys [title image_url structured-data] :as post}]
  (when structured-data
    (let [data-type (:data-type structured-data)]
      [:script {:type "application/ld+json"}
       (generate-string
         (cond->
             {"@context" "https://schema.org/"
              "name" title}
           image_url (merge (image image_url))
           (how-to? data-type) (merge (how-to/json-ld structured-data))))])))
