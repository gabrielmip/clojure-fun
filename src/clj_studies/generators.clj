(ns clj-studies.generators
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as str]))

(s/def ::id (s/and string? #(str/starts-with? % "FOO-")))

; this does not work because generating a string
; starting with FOO- at random will take a long time
(s/exercise ::id)

; the gen/fmap function takes a generator and a function
; which will be applied to the generator value
(defn foo-gen
  []
  (->> (s/gen ; this gets the generator from the spec that follows
              (s/int-in 1 100))
       (gen/fmap #(str "FOO-" %))))

(s/exercise ::id
            ; wanted values
            10
            ; overriding generators
            {::id foo-gen})

; defining the generator directly in the spec


(s/def ::id
  (s/spec (s/and string?
                 #(str/starts-with? % "FOO-"))
          :gen foo-gen))

; now, we do not need to override it
(s/exercise ::id)

; lookup
(s/def ::lookup (s/map-of keyword? string? :min-count 1))
(s/exercise ::lookup 10)

; now, creating a spec with a lookup and a key from that lookup
(s/def ::lookup-finding-k (s/and (s/cat :lookup ::lookup
                                        :k keyword?)
                                 (fn [{:keys [lookup k]}]
                                   (contains? lookup k))))

; this uses bind instead of fmap probably because
; they are using the generated value as "argument" to
; other generator, instead of the example in the fmap
; where we simply transformed the generated value.
(defn lookup-finding-k-gen
  []
  (gen/bind
   (s/gen ::lookup)
   #(gen/tuple
     (gen/return %)
     (gen/elements (keys %)))))

(s/exercise ::lookup-finding-k 10 {::lookup-finding-k lookup-finding-k-gen})

; generators for functions
(defn my-index-of
  [source search]
  (str/index-of source search))

(s/def ::my-index-of-args (s/cat :source string? :search string?))

(s/fdef my-index-of
        :args ::my-index-of-args)

; exercising this function results in strings that rarely match
(s/exercise-fn `my-index-of)

; lets model this domain
(def model (s/cat :prefix string? :match string? :suffix string?))
(defn gen-string-and-substring
  []
  (gen/fmap
   (fn [[prefix match suffix]] [(str prefix match suffix) match])
   (s/gen model)))

(s/fdef my-index-of
        :args (s/spec
               ::my-index-of-args
               :gen gen-string-and-substring))

(s/exercise-fn `my-index-of)

; but this always get good, happy examples.
; lets mix them with the degenerate cases too.
(defn gen-my-index-of-args
  []
  (gen/one-of [(gen-string-and-substring) ;; well formed examples
               (s/gen ::my-index-of-args) ;; mostly degenerate cases
               ]))

(s/fdef my-index-of
        :args (s/spec
               ::my-index-of-args
               :gen gen-my-index-of-args))

(s/exercise-fn `my-index-of)
