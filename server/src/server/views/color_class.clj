(ns server.views.color-class)

(defn color-class
  [id]
  (let [n (or (some-> id
                      (rem 8))
              0)]
    (str "c-" n)))
