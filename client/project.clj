(defproject client "0.1.0-SNAPSHOT"
  :description "Web client for simple blog web"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [org.clojure/core.async "0.4.474"]
                 [cljs-ajax "0.7.4"]
                 [rum "0.11.2"]]
  :plugins [[lein-cljsbuild "1.1.7"]]
  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:output-dir "../server/resources/js"
                                   :optimizations :simple
                                   ;; :optiomizations :advanced
                                   ;; :pretty-print false
                                   :modules {:cljs-base
                                             {:output-to "../server/resources/public/js/core.js"}}}}]})
