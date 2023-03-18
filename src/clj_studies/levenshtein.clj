(ns clj-studies.levenshtein)

(defn distance
  ([w1 w2] (distance w1 w2 (count w1) (count w2)))
  ([w1 w2 i1 i2]
   (cond
     (and (= i1 0) (= i2 0)) 0
     (= i1 0) (+ 1 (distance w1 w2 i1 (- i2 1)))
     (= i2 0) (+ 1 (distance w1 w2 (- i1 1) i2))
     :else (+ (if (= (nth w1 (- i1 1))
                     (nth w2 (- i2 1)))
                0
                1)
              (min
               (distance w1 w2 i1 (- i2 1))
               (distance w1 w2 (- i1 1) i2)
               (distance w1 w2 (- i1 1) (- i2 1)))))))

(defn to-key [index err] (str index "-" err))

(defn init
  [graph x y]
  (assoc graph (to-key x y) {}))

(defn insert
  [graph x y nerr]
  (if (= y nerr)
    graph
    (assoc-in graph
              [(to-key x y) :insert]
              (to-key x (+ y 1)))))

(defn subst
  [graph x y w nerr]
  (if (or (= y nerr) (= x (count w)))
    graph
    (assoc-in graph
              [(to-key x y) :subst]
              (to-key (+ x 1) (+ y 1)))))

(defn epsilon
  [graph x y w nerr]
  (if (or (= y nerr) (= x (count w)))
    graph
    (assoc-in graph
              [(to-key x y) :epsilon]
              (to-key (+ x 1) (+ y 1)))))

(defn match
  [graph x y w]
  (if (= x (count w))
    graph
    (assoc-in graph
              [(to-key x y) (nth w x)]
              (to-key (+ x 1) y))))

(defn nfa-states
  [w nerr]
  (for [x (range (+ 1 (count w)))
        y (range (+ 1 nerr))]
    [x y]))

(defn word->nfa
  [w nerr]
  (reduce
   (fn [graph [x y]]
     (-> graph
         (init x y)
         (insert x y nerr)
         (subst x y w nerr)
         (epsilon x y w nerr)
         (match x y w)))
   {(to-key 0 0) {}}
   (nfa-states w nerr)))


(def graph
  {"1-0" {:insert "1-1", :subst "2-1", :epsilon "2-1", \s "2-0"},
   "2-0" {:insert "2-1", :subst "3-1", :epsilon "3-1", \d "3-0"},
   "0-2" {\a "1-2"},
   "0-0" {:insert "0-1", :subst "1-1", :epsilon "1-1", \a "1-0"},
   "3-0" {:insert "3-1", :subst "4-1", :epsilon "4-1", \f "4-0"},
   "3-1" {:insert "3-2", :subst "4-2", :epsilon "4-2", \f "4-1"},
   "3-2" {\f "4-2"},
   "4-2" {},
   "4-1" {:insert "4-2"},
   "2-1" {:insert "2-2", :subst "3-2", :epsilon "3-2", \d "3-1"},
   "0-1" {:insert "0-2", :subst "1-2", :epsilon "1-2", \a "1-1"},
   "1-1" {:insert "1-2", :subst "2-2", :epsilon "2-2", \s "2-1"},
   "4-0" {:insert "4-1"},
   "1-2" {\s "2-2"},
   "2-2" {\d "3-2"}})

(seq graph)

(defn epsilon-states
  [graph start]
  (loop [states [start]
         curr start]
    (if (contains? (get graph curr) :epsilon)
      (recur (conj states (get-in graph [curr :epsilon]))
             (get-in graph [curr :epsilon]))
      states)))

(epsilon-states graph "1-0")

(defn epsilon-from-op
  [graph state op]
  (if (contains? (get graph state) op)
    (epsilon-states graph (get-in graph [state op]))
    []))

(defn letter
  [graph state]
  (first (filter char? (keys (get graph state)))))

; this is not correct. it only works in this case, for levenshtein automata.
(defn nfa->dfa
  [graph]
  (into
   {}
   (map
    (fn [state]
      [state {:epsilon (epsilon-states graph state)
              :insert (epsilon-from-op graph state :insert)
              :subst (epsilon-from-op graph state :subst)
              (letter graph state) (epsilon-from-op
                                    graph
                                    state
                                    (letter graph state))}])
    (keys graph))))

(nfa->dfa (word->nfa "asdf" 2))

(word->nfa "asdf" 2)
