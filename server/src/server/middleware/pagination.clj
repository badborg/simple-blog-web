(ns server.middleware.pagination
  (:require [environ.core :refer [env]]))

(def default-per-page
  (some-> (env :per-page)
          Integer/parseInt))

(defn wrap-pagination
  [handler
   {:keys [per-page]
    :or {per-page default-per-page}}]
  (fn [req]
    (let [{:keys [page]} (:params req)
          page (try
                 (Integer. page)
                 (catch NumberFormatException _
                   1))
          offset (-> page
                     (- 1)
                     (* per-page))
          page-params {:limit per-page
                       :page page
                       :offset offset}]
      (handler (update req :params merge page-params)))))
