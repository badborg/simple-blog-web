(defproject styles "0.1.0-SNAPSHOT"
  :description "Styles for simple blog web"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [garden "1.3.6"]]
  :plugins [[lein-garden "0.3.0"]]
  ;; :main ^:skip-aot styles.core
  :profiles {:uberjar {:aot :all}}
  :garden {:builds [{:source-paths ["src"]
                     :stylesheet styles.core/main
                     :compiler {:output-to "../server/resources/public/s/core.css"
                                :pretty-print? false}}]})
