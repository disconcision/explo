(ns explo.views
  (:require [clojure.string :as string]
            [reagent.core :as reagent]
            [re-frame.core :as rf]
            [explo.events]))

(defn whole-state []
  [:div
   (str @(rf/subscribe [:state]))])

(defn stuff []
  [:div
   (for [[thing-name n] @(rf/subscribe [:stuff])]
     [:div @(rf/subscribe [:glyph thing-name]) ": " n])])

(defn thing+n->string [[thing n]]
  (str n @(rf/subscribe [:glyph thing])))

(defn things->string [things]
  (apply str (interpose "+" (map thing+n->string things))))

(defn spell->string [{:keys [in out cata]}]
  (str (if (empty? in) "🕳️" (things->string in))
       " ⟶ "
       (if (empty? out) "🕳️" (things->string out))
       (when-not (empty? cata) (str " [" (things->string cata) "]"))))

(defn spells []
  [:div
   (for [[spell-name v] @(rf/subscribe [:spells])]
     [:div (let [spell @(rf/subscribe [:spell spell-name])]
             [:input {:type "button" :value (spell->string spell)
                      :on-click #(rf/dispatch [:cast-spell spell-name])}])])])

(defn clock []
  [:div.example-clock
   {:style {:color "#666"}}
   (-> @(rf/subscribe [:time])
       .toTimeString
       (string/split " ")
       first)])

(defn stage []
  [:div
   [:h1 "explo"]
   [clock]
   [stuff]
   [spells]
   [whole-state]])
