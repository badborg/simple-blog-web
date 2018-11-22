(ns client.core.scroll
  (:require [clojure.core.async :refer [go <! >! timeout]]))

(defn non-zero
  [v]
  (or v 0))

(defn scrolled-bottom?
  []
  (let [doc (.-documentElement js/document)
        body (.-body js/document)
        win-height (some-> js/window .-innerHeight non-zero)
        scroll-top (max (some-> js/window .-scrollY non-zero)
                        (some-> js/window .-pageYOffset non-zero)
                        (+ (some-> body .-scrollTop non-zero)
                           (some-> doc .-scrollTop non-zero)))
        offset (+ scroll-top
                  win-height)
        height (max (some-> doc .-clientHeight non-zero)
                    (some-> doc .-offsetHeight non-zero)                    
                    (some-> doc .-scrollHeight non-zero)
                    (some-> body .-clientHeight non-zero)
                    (some-> body .-offsetHeight non-zero)                    
                    (some-> body .-scrollHeight non-zero))]
    (>= offset height)))

;; (defn scrolled-to?
;;   [el]
;;   (let [el-top (some-> el
;;                        .getBoundingClientRect
;;                        .-top)
;;         ;; doc (.-documentElement js/document)
;;         ;; body (.-body js/document)
;;         win-height (some-> js/window .-innerHeight non-zero)
;;         scroll-top (max (some-> js/window .-scrollY non-zero)
;;                         (some-> js/window .-pageYOffset non-zero)
;;                         ;; (+ (some-> body .-scrollTop non-zero)
;;                         ;;    (some-> doc .-scrollTop non-zero))
;;                         )
;;         offset (+ scroll-top
;;                   ;; win-height
;;                   )]
;;     (>= offset
;;         el-top)))

(defn on-scrolled-bottom
  [callback]
  (let [done? (atom false)]
    (-> js/window
        (.addEventListener "scroll"
                           (fn [_]
                             (when (and (scrolled-bottom?)
                                        (not @done?))
                               (go (callback)
                                   (<! (timeout 100))
                                   (reset! done? false))
                               (reset! done? true)))))))

;; (defn on-scrolled-to
;;   [el callback]
;;   (let [done? (atom false)]
;;     (-> js/window
;;         (.addEventListener "scroll"
;;                            (fn [_]
;;                              (when (and (scrolled-to? el)
;;                                         (not @done?))
;;                                (go (callback)
;;                                    (<! (timeout 1000))
;;                                    (reset! done? false))
;;                                (reset! done? true)))))))
