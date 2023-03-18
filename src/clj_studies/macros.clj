(ns clj-studies.macros)

(quote (str "asdf" "fdas"))

(eval (read-string "(def value 10)"))

(defn fn1 [a] {})
(defn fn2 [a] {})
(defn fn3 [a] {})
(defn fn4 [a] {})

(defn pipe2 [a & fns]
  (fn [init] (reduce
              #(merge %1 (%2 %1))
              init
              fns)))

((pipe2 fn1 fn2 fn3) {})
