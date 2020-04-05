(ns explo.views
  (:require [clojure.string :as string]
            [reagent.core :as reagent]
            [re-frame.core :as rf]
            [explo.events]))

(defn whole-state []
  [:div
   (str @(rf/subscribe [:state]))])

(defn stuff []
  [:div {:style {:padding "30px" :font-size "14pt"}}
   (doall (for [[thing-name n] @(rf/subscribe [:stuff])]
            ^{:key thing-name}
            [:div {:style {:color "#555" :padding "2px"}}
             @(rf/subscribe [:glyph thing-name]) " " n]))])

(defn thing+n->string [[thing n]]
  (str n @(rf/subscribe [:glyph thing])))

(defn things->string [things]
  (apply str (interpose " + " (map thing+n->string things))))

(defn spell->string [{:keys [in out cata]}]
  (str (if (empty? in) "" #_"🕳️" (things->string in))
       (if (empty? in) "" #_" ↳ " " ⟶ ")
       (if (empty? out) "🕳️" (things->string out))
       ;; hiding catalyst for now
       #_(when-not (empty? cata) (str " [" (things->string cata) "]"))))

(defn spells []
  [:div {:style {:padding "30px"}}
   (doall (for [[spell-name v] @(rf/subscribe [:spells])]
            ^{:key spell-name}
            [:div (let [spell @(rf/subscribe [:spell spell-name])]
                    [:input {:type "button" :value (spell->string spell)
                             :style {:width "100%" :font-size "14pt" :padding "8px" :margin "2px":border-radius "8px"
                                     :background-color "#444" :border-color "#666" :color "#eee"}
                             :on-click #(rf/dispatch [:cast-spell spell-name])}])]))])

(defn clock []
  [:div.example-clock
   {:style {}}
   (-> @(rf/subscribe [:time])
       .toTimeString
       (string/split " ")
       first)])

(defn stage []
  [:div {:style {:padding "30px"}}
   [:h1 "explo"]
   [clock]
   [:div {:style {:display :flex :align-items "horizontal"}}
    [stuff]
    [spells]]
   #_[whole-state]])
