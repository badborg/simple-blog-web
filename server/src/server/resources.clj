(ns server.resources
  (:require [environ.core :refer [env]]))

(defn core-script-path
  []
  (or (env :js-core)
      "/js/core.js"))

(defn core-script-path-version
  []
  (str (core-script-path) "?v=0.2.1"))

(defn core-style-path
  []
  (or (env :css-core)
      "/s/core.css"))

(defn core-style-path-version
  []
  (str (core-style-path) "?v=0.1.0"))
