(ns explo.core
  (:require [clojure.string :as string]
            [reagent.core :as reagent]
            [re-frame.core :as rf]
            [explo.events]
            [explo.views :refer [stage]]))

;; idea:
;; imagine storage space of an item type is determined by representation of quantity
;; reducing space taken by upgrading notation (but possibly )

(defn dispatch-timer-event []
  (let [now (js/Date.)]
    (rf/dispatch [:timer now])))

;; defonce to prevent clobbering on hot-reloading
(defonce do-timer (js/setInterval dispatch-timer-event 1000))

(defn render [] ;; load stage into <div id="app" />
  (reagent.dom/render [stage] (js/document.getElementById "app")))

(defn ^:dev/after-load clear-cache-and-render! []
  ;; The `:dev/after-load` metadata causes this function to be called
  ;; after shadow-cljs hot-reloads code. We force a UI update by clearing
  ;; the Reframe subscription cache.
  (rf/clear-subscription-cache!)
  (render))

(defn run []
  (rf/dispatch-sync [:initialize])
  (render))
