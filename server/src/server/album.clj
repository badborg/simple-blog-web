(ns server.album)

(defn album->images
  [image_url album_url album_num]
  (let [suffix (when image_url
                 (some-> (re-find #"_\d+(\.[^/]+)$" image_url)
                         (nth 1)))
        image (fn [n]
                (when (and suffix album_url)
                  (str album_url "_" n suffix)))]
    (when album_num
      (->> (range album_num)
           (keep image)
           ;; no need to turn into hash?
           (map #(hash-map :url %))
           ))))
