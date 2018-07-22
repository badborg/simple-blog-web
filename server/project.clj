(defproject server "0.1.0-SNAPSHOT"
  :description "Web server for simple blog web"
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main server.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :test {:resource-paths ["test-resources"]}})
