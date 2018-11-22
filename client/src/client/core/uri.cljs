(ns client.core.uri
  (:require [clojure.string :as s]))

(defn not-blank?
  [v]
  (not (s/blank? v)))

(defn extract-query-string
  [query params-sanitizer]
  (when query
    (->> (s/split query #"\&")
         (filter not-blank?)
         (reduce (fn [m p]
                   (let [[k v] (s/split p #"\=")]
                     (cond-> m
                       (not-blank? k) (assoc (keyword k)
                                             (js/decodeURIComponent v)))))
                 {})
         params-sanitizer)))

(defn extract-uri
  [uri params-sanitizer]
  (when uri
    (let [[path query] (s/split uri #"\?")]
      (cond-> {}
        (not-blank? path) (assoc :path path)
        (not-blank? query) (assoc :params
                                  (extract-query-string query
                                                        params-sanitizer))))))
