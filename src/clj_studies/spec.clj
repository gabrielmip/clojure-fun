(ns clj-studies.spec
  (:require [clojure.pprint :as pprint]))

(def pp pprint/pprint)




; -------------- Tipos primitivos --------------


42
"hi"
:foo ; palavra-chave
{:foo "bar" :hey "ho"}
[1 2 3 :foo "bar"]





; -------------- Funções ---------------


; Em vez de function(arg1, arg2), eu chamo (function arg1 arg2)


(inc 2) ; em vez de inc(1)

(+ 2 1) ; em vez de 2 + 1






; -------------- Definições ---------------


(def value 20)

(defn super-add [a b]
  (+ a b 10))

(super-add value 40)





; -------------- Problema ---------------


(require '[clojure.string :as string])

(def person {:person/firstname "Alfredo"
             :person/lastname "Milani"
             :person/age -57})

(defn full-name [person]
  (string/join
   " "
   [(get person :person/firstname)
    (get person :person/lastname)]))

(full-name person)





; -------------- Spec de objetos ---------------


(require '[clojure.spec.alpha :as s]
         '[clojure.spec.gen.alpha :as gen])

(s/def :person/firstname (s/and string? not-empty))
(s/def :person/lastname (s/and string? not-empty))
(s/def :person/age nat-int?)

(s/def ::person
  (s/keys :req [:person/age
                :person/firstname
                :person/lastname]))

(s/valid? ::person person)
(s/explain-data ::person person)

(pp (gen/generate (s/gen ::person)))
(pp (s/exercise ::person))

(defn name-gen []
  (s/gen #{"Daniel" "Gabriel" "Rafael" "Josiel" "Maciel"}))

(s/def :person/firstname (s/spec (s/and string? not-empty)
                                 :gen name-gen))

(gen/generate (s/gen ::person))





; -------------- Spec de funções ---------------


(require '[clojure.spec.test.alpha :as stest])

(comment
  (defn no-dangling-spaces? [text]
    (nil? (re-matches #"^ .*|.* $" text)))

  (defn count-name [data name-key]
    (count (-> data :args :person name-key)))

  (defn has-name-counts-plus-1-space? [test-data]
    (= (count (:ret test-data))
       (+ 1 ; esse 1 é para o espaço entre os nomes
          (count-name test-data :person/firstname)
          (count-name test-data :person/lastname)))))

(defn full-name [person]
  (string/join
   " "
   [(get person :person/firstname)
    (get person :person/lastname)]))

(s/fdef full-name
  :args (s/cat :person (s/keys :req [:person/firstname :person/lastname]))
  :ret (s/and string? no-dangling-spaces?)
  :fn has-name-counts-plus-1-space?)

(pp (s/exercise-fn `full-name))
(pp (stest/abbrev-result (first (stest/check `full-name))))

(full-name person)
(full-name {:person/firstname ""})

(stest/instrument)
(stest/unstrument)

(stest/instrument `full-name {:stub #{`full-name}})

;; Read Eval Print Loop