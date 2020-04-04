(ns explo.events
  (:require [re-frame.core :as rf]
            [explo.data :refer [seed]]
            [explo.logic :refer [cast cast+]]))

;; EVENTS

(rf/reg-event-db
 :initialize
 (fn [_ _]
   (assoc seed :time (js/Date.))))

(rf/reg-event-db
 :cast-tick
 (fn [db [_ _]]
   (update-in db [:bag :stuff]
              #(cast+ (get-in db [:book :spell]) %
                      (get-in db [:exe :always])))))

(rf/reg-event-db
 :cast-spell
 (fn [db [_ spell]]
   (update-in db [:bag :stuff]
              #(cast (get-in db [:book :spell]) % spell))))

(rf/reg-event-fx
 :timer
 (fn [cofx [_ new-time]]
   {:db       (assoc (:db cofx) :time new-time)
    :dispatch [:cast-tick]}))


;; QUERY

(rf/reg-sub
 :time
 (fn [db _]
   (:time db)))

(rf/reg-sub
 :state
 (fn [db _]
   db))

(rf/reg-sub
 :stuff
 (fn [db _]
   (get-in db [:bag :stuff])))

(rf/reg-sub
 :spell
 (fn [db [_ name]]
   (get-in db [:book :spell name])))

(rf/reg-sub
 :spells
 (fn [db _]
   (get-in db [:bag :spell])))

(rf/reg-sub
 :glyph
 (fn [db [_ name]]
   (get-in db [:book :trash name :glyph])))
