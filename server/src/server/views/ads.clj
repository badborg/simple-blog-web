(ns server.views.ads
  (:require [clojure.java.io :as io]
            [environ.core :refer [env]])
  (:import (java.time LocalDate)))

(defn read-file
  [f]
  (when-let [file (io/file f)]
    (when (.exists file)
      (slurp file))))

(def ad1-file
  (some-> (env :ad1)
          read-file))

(def ad2-file
  (some-> (env :ad2)
          read-file))

(def ad3-file
  (some-> (env :ad3)
          read-file))

(def ad4-file
  (some-> (env :ad4)
          read-file))

(def ad5-file
  (some-> (env :ad5)
          read-file))

(def notice
  [:div.notice
   "&ltcc;"])

(defn ad1
  []
  (when ad1-file
    [:div#ad1
     notice
     ad1-file]))

(defn ad2
  []
  (when ad2-file
    [:div#ad2
     notice
     ad2-file]))

(defn ad3
  []
  (when ad3-file
    [:div#ad3
     notice
     ad3-file]))

(defn ad4
  []
  (when ad4-file
    [:div#ad4
     notice
     ad4-file]))

(defn ad5
  []
  (when ad5-file
    [:div#ad5
     notice
     ad5-file]))

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
