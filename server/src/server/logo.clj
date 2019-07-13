(ns server.logo
  (:require [clojure.java.io :as io]
            [environ.core :refer [env]]))

(def logo-file
  (env :logo))

(defn logo-ext
  []
  (when logo-file
    (some-> (re-find #"[a-z]+$" logo-file)
            vector
            set
            (some #{"png" "svg"}))))

(defn path
  []
  (let [ext (logo-ext)]
    (cond-> "/logo"
      ext (str "." ext))))

(defn content-type
  []
  (-> {"svg" "image/svg+xml"
       "png" "image/png"}
      (get (logo-ext))
      (or "application/octet-stream")))

(defn logo-content
  []
  (when logo-file
    (let [file (io/file logo-file)]
      (when (.exists file)
        file))))

(def available?
  logo-content)

(def hours->secs
  (partial * 3600))

(defn response
  []
  (when-let [content (logo-content)]
    {:headers {"Content-Type" (content-type)
               "Cache-Control" (str "max-age=" (hours->secs 24))}
     :body content}))
