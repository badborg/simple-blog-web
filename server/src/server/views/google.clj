(ns server.views.google
  (:require [environ.core :refer [env]]))

(def ga-tracking-id
  (env :ga-tracking-id))

(def google-ad-client-id
  (env :google-ad-client-id))

(defn analytics-tracking
  []
  (when ga-tracking-id
    (str
      "<script async src=\"https://www.googletagmanager.com/gtag/js?id="
      ga-tracking-id
      "\"></script>"
      "<script>"
      "window.dataLayer = window.dataLayer || [];"
      "function gtag(){dataLayer.push(arguments);}"
      "gtag('js', new Date());"
      "gtag('config', '"
      ga-tracking-id
      "');"
      "</script>")
    ))

(defn auto-ads
  []
  (when google-ad-client-id
    (str
      "<script async src=\"http://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js\"></script>"
      "<script>"
      "(adsbygoogle = window.adsbygoogle || []).push({"
      "google_ad_client: \""
      google-ad-client-id
      "\","
      "enable_page_level_ads: true"
      "});"
      "</script>")))

(defn ad-unit
  [ad-slot-id]
  (when (and google-ad-client-id ad-slot-id)
    (str
      "<script async src=\"//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js\"></script>"
      "<ins class=\"adsbygoogle\""
      "style=\"display:inline-block\""
      "data-ad-client=\""
      google-ad-client-id
      "\""
      "data-ad-slot=\""
      ad-slot-id
      "\">"
      "</ins>"
      "<script>(adsbygoogle = window.adsbygoogle || []).push({});</script>")))
