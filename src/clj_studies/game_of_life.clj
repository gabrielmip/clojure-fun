(ns clj-studies.game-of-life)

(def size 32)

(defn get-neighboors [[px py] world]
  (filter
   (fn [[x y]] (and
                (not= [x y] [px py])
                (contains? world [x y])))
   (for [x (range (- px 1) (+ px 2))
         y (range (- py 1) (+ py 2))]
     [x y])))

(defn lives? [world position]
  (let [alive? (contains? world position)
        n-neighs (count (get-neighboors position world))]
    (if alive?
      (or (= n-neighs 2) (= n-neighs 3))
      (= n-neighs 3))))

(defn get-positions []
  (for [x (range 0 size)
        y (range 0 size)]
    [x y]))

(defn evolve [world]
  (into #{}
        (filter #(lives? world %) (get-positions))))
