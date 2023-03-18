(ns clj-studies.atoms)

(def textbox (atom {}))

(defn init-textbox [] 
  (reset! textbox {:text ""
                   :color :BLACK
                   :width 100}))

(init-textbox)
@textbox

(def memento (atom nil))

(defn type-text [text]
  (swap! textbox
         (fn [m]
           (update-in m [:text] (fn [s] (str s text))))))

(defn save []
  (reset! memento (:text @textbox)))

(defn restore []
  (swap! textbox
         assoc :text @memento))

@memento
@textbox

(type-text "-")
(save)
(restore)


