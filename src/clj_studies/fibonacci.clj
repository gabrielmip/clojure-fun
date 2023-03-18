(ns clj-studies.fibonacci)

;; reference to this whole thing:
;; http://danmidwood.com/content/2013/02/24/exploring-clojure-memoization.html

(defn fib
  [n]
  (println ".")
  (cond
    (<= n 1) n
    :else (+
           (fib (- n 1))
           (fib (- n 2)))))

(comment
  "rebinding to memoize works, apparently"
  (def fib (memoize fib)))

;; but this is better
(def fib2
  (memoize
   (fn [n]
     (println ".")
     (cond
       (<= n 1) n
       :else (+
              (fib2 (- n 1))
              (fib2 (- n 2)))))))

;; with a macro now
(defmacro defn-memo [name & body]
  `(def ~name (memoize (fn ~@body))))

(defn-memo fib3
  [n]
  (println ".")
  (if (<= n 1)
    n
    (+ (fib3 (- n 1)) (fib3 (- n 2)))))

(fib3 12)
