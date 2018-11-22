(ns server.sanitize
  (:require [clojure.string :refer [trim]])
  (:import [org.owasp.html Sanitizers
            HtmlPolicyBuilder]))

(def block-elements
  ["p" "h1" "h2" "h3" "h4" "h5" "h6" "ul" "ol" "li" "blockquote"])

(def sanitizer
  (->
    (-> (HtmlPolicyBuilder.)
        (.allowElements (into-array String block-elements))
        .toFactory)
    (.and Sanitizers/FORMATTING)
    (.and Sanitizers/LINKS)))

(defn sanitize
  [html]
  (-> sanitizer
      (.sanitize html)
      trim))
