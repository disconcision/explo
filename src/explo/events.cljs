(ns explo.events
  (:require [re-frame.core :as rf]
            [explo.data :refer [seed]]
            #_[explo.logic :refer [cast cast+]]))

;; LOGIC

(defn update-stuff [stuff diff f]
  (reduce (fn [stuff [thing quantity]]
            (update stuff thing (partial f quantity)))
          stuff diff))

(defn cast [book-spells stuff spell-name]
  (let [{:keys [in out cata] :as spell} (get book-spells spell-name)
        m (or (:min spell) 0)
        M (or (:max spell) 6666666)
        all-in (into {} (for [[k v] (merge-with + in cata)] [k (- 0 v)]))
        #_all-in #_(merge-with (fn [v1 v2] (- 0 (+ v1 v2))) in cata)]
    (cond
      (every? identity (for [[k v] all-in]  (<= m (+ (get stuff k) v))))
      (let [all-out (merge-with + out cata)
            net (merge-with + all-out all-in)]
        (update-stuff stuff net (comp (partial min M) +)))
      :else stuff)))

(defn cast+ [book-spells stuff spells]
  (reduce (partial cast book-spells) stuff spells))

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
