(ns server.css
  (:require [clojure.java.io :as io]
            [environ.core :refer [env]]))

(def version
  "0.1.1")

(def css-file
  (env :custom-css))

(defn path
  []
  "/custom.css")

(defn path-version
  []
  (str (path) "?v=" version))

(defn css-content
  []
  (when css-file
    (let [file (io/file css-file)]
      (when (.exists file)
        file))))

(def available?
  css-content)

(def hours->secs
  (partial * 3600))

(defn response
  []
  (when-let [content (css-content)]
    {:headers {"Content-Type" "text/css"
               "Cache-Control" (str "max-age=" (hours->secs 2400))}
     :body content}))
