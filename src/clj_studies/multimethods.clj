(ns clj-studies.oop-design-patterns)

(def activity {:type :activity :content "Cow"})

(def message  {:type :message :content "Say"})

; formats :pdf :xml

(defn a->pdf [item] (println "a->pdf"))
(defn m->pdf [item] (println "m->pdf"))
(defn a->xml [item] (println "a->xml"))
(defn m->xml [item] (println "m->xml"))

(defmulti export
  (fn [item form] [(:type item) form]))

(defmethod export [:activity :pdf] [item form]
  (a->pdf item))

(defmethod export [:activity :xml] [item form]
  (a->xml item))

(defmethod export [:message :pdf] [item form]
  (m->pdf item))

(defmethod export [:message :xml] [item form]
  (m->xml item))

(defmethod export :default [item form]
  (throw (IllegalArgumentException. "not supported")))

; generalizando
(derive ::xml ::doc)
(derive ::pdf ::doc)
(derive ::csv ::doc)
(defmethod export [:activity ::doc] [item form]
  (println "::doc pegou"))
(defmethod export [:message ::doc] [item form]
  (println "::doc pegou"))

(export message ::csv)

