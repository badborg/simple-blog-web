(ns server.views.layout
  (:require [environ.core :refer [env]]
            [hiccup.page :refer [html5]]
            [server.css :as css]
            [server.views.google :as google]
            [server.logo :as logo]))

(def site-name
  (env :name))

(def core-script
  (or (env :js-core)
      "/js/core.js?v=0.1.0"))

(def core-style
  (or (env :css-core)
      "/s/core.css?v=0.1.0"))

(defn search-form
  []
  [:form#search {:action "/"
                 :method :get}
   [:input {:name "s"
            :type "text"}]])

(defn main
  [title class & content]
  (html5
    [:head
     [:title (or title
                 site-name)]
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width; initial-scale=1;"}]
     [:link {:rel :stylesheet
             :href core-style}]
     (when (css/available?)
       [:link {:rel :stylesheet
               :href (css/path)}])
     (google/auto-ads)
     (google/analytics-tracking)]
    [:body
     [:div#navigation
      [:a#home {:href "/"}
       (if (logo/available?)
         [:img {:src "/logo.png"}]
         site-name)]
      (search-form)]
     [:div#quick-results]
     (when title
       [:h1 {:class class}
        title])
     content
     [:script {:src core-script
               :defer "defer"}]]))
