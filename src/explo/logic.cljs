(ns explo.logic
  (:require))

(defn update-stuff [stuff diff f]
  (reduce (fn [stuff [thing quantity]]
            (update stuff thing (partial f quantity)))
          stuff diff))

(defn cast [book-spells stuff spell]
  (let [{:keys [in out cata]} (get book-spells spell)]
    ;; TODO: check first. max/min guards. catalysts.
    (-> stuff
        (update-stuff in (fn [n x] (- x n)))
        (update-stuff out +))))

(defn cast+ [book-spells stuff spells]
  (reduce (partial cast book-spells) stuff spells))
