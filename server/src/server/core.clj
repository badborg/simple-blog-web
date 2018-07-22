(ns server.core
  (:require [environ.core :refer [env]]
            [org.httpkit.server :refer [run-server]]
            [server.routes :refer [handler]])
  (:gen-class))

(def port
  (some-> (env :port)
          Integer/parseInt))

(defn -main
  [& args]
  (run-server #'handler {:port port}))
