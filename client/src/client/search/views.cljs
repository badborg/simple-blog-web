(ns client.search.views
  (:require [clojure.core.async :refer [go timeout <!]]
            [clojure.string :as s]
            [goog.string :refer [unescapeEntities]]
            [rum.core :as rum]
            [client.core.views :refer [post-item]]))

(rum/defcs main-input < rum/reactive
  (rum/local false ::block?)
  (rum/local nil ::current-search)
  [local {:keys [state set-search]}]
  (let [block? (::block? local)
        block! #(reset! block? true)
        unblock! #(reset! block? false)
        current-search (::current-search local)
        delayed-search #(when-not @block?
                          (go (<! (timeout 1000))
                              (set-search {:search @current-search})
                              (unblock!))
                          (block!))]
    [:input {:name "s"
             :type "text"
             :on-change (fn [e]
                          (.preventDefault e)
                          (let [v (-> e .-target .-value)]
                            (reset! current-search v)
                            (delayed-search)))}]))

(defn reactive-search-results
  [state]
  (let [posts (-> state
                  (rum/cursor :posts)
                  rum/react)
        search (-> state
                   (rum/cursor :search)
                   rum/react)
        total (count posts)]
    {:posts posts
     :search search
     :posts? (> total 0)
     :more? (and (-> search s/blank? not)
                 (>= total 24))}))

(def more-sym
  (unescapeEntities "&ctdot;"))

(def close-sym
  (unescapeEntities "&times;"))

(rum/defc main-results < rum/reactive
  [{:keys [state set-search]}]
  (let [{:keys [posts search posts? more?]}
        (reactive-search-results state)]
    (list
      (when posts?
        [:button.close
         {:on-click (fn [e]
                      (set-search {:search ""}))}
         close-sym])
      (when posts?
        (map post-item posts))
      (when posts?
        [:a.more {:href (str "/?s=" search)}
         more-sym]))))

(defn mount
  [input-el results-el events]
  (rum/mount (main-input events)
             input-el)
  (rum/mount (main-results events)
             results-el))
