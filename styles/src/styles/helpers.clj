(ns styles.helpers
  (:require [garden.stylesheet :refer [at-media]]))

(defn px
  [n]
  (str n "px"))

(defn %
  [n]
  (str n "%"))

(defn em
  [n]
  (str n "em"))

(defn neg
  [v]
  (str "-" v))

(defn mergeable
  [styles]
  (fn
    ([]
     styles)
    ([s]
     (merge styles s))))

(defn all-sels?
  [v]
  (not-any? vector? v))

(defn into-inner-most
  [v style]
  (if (all-sels? v)
    (into v style)
    (let [v2 (last v)]
      (when (vector? v2)
        (into (pop v)
              [(into-inner-most v2 style)])))))

(defn add-sels
  [selectors style]
  (cond
    (map? style) (into-inner-most selectors [style])
    (vector? style) (into-inner-most selectors style)))

(defn build-media
  [media-ranges]
  (map
    (fn [[[from to] s]]
      (at-media
        (cond-> {:screen :only
                 :min-width (px from)}

          to (assoc :max-width (px to)))
        s))
    media-ranges))
