(defproject simple-blog-web "0.2.0"
  :description "Web server for simple blog web"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [cheshire "5.8.0"]
                 [clj-time "0.14.2"]
                 [compojure "1.6.0"]
                 [environ "1.1.0"]
                 [hiccup "1.0.5"]
                 [http-kit "2.2.0"]
                 [ring/ring-defaults "0.3.1"]
                 [ring/ring-codec "1.1.1"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-json "0.4.0"]
                 [com.googlecode.owasp-java-html-sanitizer/owasp-java-html-sanitizer "20180219.1"]
                 ]
  :plugins [[lein-environ "1.1.0"]]
  :main server.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :test {:dependencies [[ring/ring-mock "0.3.2"]]
                    :resource-paths ["test-resources"]}})
