(ns explo.data
  (:require))

(def seed
  {:book {:trash {:flowers  {:glyph "🌼" :name "flowers"}
                  :light    {:glyph "🔅" :name "light" :max 1000}
                  :sun      {:glyph "☀️" :name "sun" :max 1}
                  :mana     {:glyph "🍔" :name "mana"}
                  :cell     {:glyph "🕋" :name "cell"}
                  :drone    {:glyph "🐝" :name "drone"}} ;;  :max '(count :bag-cells)
          :spell {:->light  {:in   {}
                             :out  {:light 4}
                             :cata {:sun 1}}
                  :->mana   {:in   {}
                             :out  {:mana 1 :flowers 8}
                             :cata {:light 1}}
                  :->farm   {:in   {:mana 10}
                             :out  {:farm 1}}
                  :->cell   {:in   {:mana 100}
                             :out  {:cell 1}}
                  :cell->   {:in   {:cell 1}
                             :out  {:mana 70}}}
          :actors {:sun  {:op :->light
                          :time 1}
                   :farm {:op :->mana
                          :time 1}}}
   :bag {:stuff {:sun 1
                 :mana 0
                 :light 1000}
         :spell {:->mana 1
                 :->farm 1}
         :actors {:sun 1}}
   :exe {:always [:->light]}})