(ns server.css
  (:require [clojure.java.io :as io]
            [environ.core :refer [env]]))

(def css-file
  (env :custom-css))

(defn path
  []
  "/custom.css")

(defn css-content
  []
  (when css-file
    (let [file (io/file css-file)]
      (when (.exists file)
        file))))

(def available?
  css-content)

(defn response
  []
  (when-let [content (css-content)]
    {:headers {"Content-Type" "text/css"}
     :body content}))
