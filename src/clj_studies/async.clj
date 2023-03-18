(ns clj-studies.async)

; futures
(let [f (future
          (+ 1 1))
      wait-for 10
      if-times-out 5]
  (println (realized? f))
  (println (deref f wait-for if-times-out))
  (println (realized? f)))

; delays
(defn my-identity [message]
  message)

; delays ran only once and its result is cached
(let [delayed (delay (my-identity "hi"))]
  (println (realized? delayed))
  (force delayed))

; promise
(let [my-promise (promise)]
  (println (realized? my-promise))
  (deliver my-promise [1 2 3 4])
  (println (realized? my-promise))
  @my-promise)

; EX: promises to deliver first good element in async search
; BEGIN
(defn mock-api-call
  [result]
  (Thread/sleep 1000)
  result)

(defn satisfactory?
  "If the butter meets criteria"
  [butter]
  (and (<= (:price butter) 100)
       (>= (:smoothness butter) 97)
       butter))

(def butters
  [{:store "Baby Got Yak"
    :price 94
    :smoothness 99}
   {:store "Butter than nothing"
    :price 150
    :smoothness 83}])

; remember: one can add timeout and default value to deref
(time
 (let [butter-promise (promise)]
   (doseq [butter butters]
     (future (if-let [good-enough (satisfactory? (mock-api-call butter))]
               (deliver butter-promise good-enough))))
   (println "Chosen butter: " (deref butter-promise))))
; END

; macros
