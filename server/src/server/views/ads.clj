(ns server.views.ads
  (:require [environ.core :refer [env]]
            [server.views.google :as google])
  (:import (java.time LocalDate)))

(defn get-ad-code
  [ad-id]
  (some-> (or (env ad-id)
              (env :global-ad-slot-id))
          google/ad-unit))

(def notice
  [:div.notice
   "&ltcc;"])

(defn get-ad
  [ad-id]
  (when-let [code (get-ad-code ad-id)]
    [:div {:id (name ad-id)}
     notice
     code]))

(def ad1
  (partial get-ad :ad1))

(def ad2
  (partial get-ad :ad2))

(def ad3
  (partial get-ad :ad3))

(def ad4
  (partial get-ad :ad4))

(def ad5
  (partial get-ad :ad5))

(defn today-date
  []
  (.getDayOfMonth (LocalDate/now)))

(defn insert-ad?
  [id]
  (when (integer? id)
    (let [bin (-> (today-date)
                  (+ id)
                  (rem 2))]
      (if (= bin 1)
        true
        false))))

(defn insert-ad-pos
  [total id]
  (when (and (integer? total)
             (integer? id)
             (> total 0))
    (-> (today-date)
        (+ id)
        (rem (quot total 2))
        (* 2))))

(defn insert-at
  [n item coll]
  (if (and (integer? n)
           item)
    (let [n-bounded (min (if (< n 0) 0 n)
                         (count coll))
          v-coll (vec coll)
          head (subvec v-coll 0 n-bounded)
          tail (cons item
                     (subvec v-coll n-bounded))]
      (concat head tail))
    coll))
