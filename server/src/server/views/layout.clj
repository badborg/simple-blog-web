(ns server.views.layout
  (:require [environ.core :refer [env]]
            [hiccup.page :refer [html5]]
            [server.css :as css]
            [server.views.google :as google]
            [server.logo :as logo]
            [server.resources :as resources]))

(def site-name
  (env :name))

(def host-name
  (env :hostname))

(def protocol
  (env :protocol))

(defn search-form
  []
  [:form#search {:action "/"
                 :method :get}
   [:input {:name "s"
            :type "text"}]])

(defn absolute-url
  [url]
  (when (and url protocol host-name)
    (str protocol "://" host-name url)))

(defn main
  [{:keys [title class noindex canonical structured-data]} & content]
  (html5
    [:head
     [:title (or title
                 site-name)]
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     [:link {:rel :stylesheet
             :href (resources/core-style-path-version)}]
     (when (css/available?)
       [:link {:rel :stylesheet
               :href (css/path-version)}])
     (when noindex
       [:meta {:name "robots"
               :content "noindex"}])
     (when canonical
       [:link {:rel "canonical"
               :href (absolute-url canonical)}])
     (when structured-data
       structured-data)
     (google/auto-ads)
     (google/analytics-tracking)]
    [:body
     [:div#navigation
      [:a#home {:href "/"}
       (if (logo/available?)
         [:img {:src (logo/path-version)}]
         site-name)]
      (search-form)]
     [:div#quick-results]
     (when title
       [:h1 {:class class}
        title])
     content
     [:script {:src (resources/core-script-path-version)
               :defer "defer"}]]))
