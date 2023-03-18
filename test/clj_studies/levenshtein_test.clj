(ns clj-studies.levenshtein-test
  (:require [clojure.test :refer :all]
            [clj-studies.levenshtein :refer [distance]]))

(deftest distance-test
  (testing "base case"
    (is (= (distance "" "") 0)))
  (testing "the size of the string if the second is empty"
    (is (= (distance "asf" "") 3)))
  (testing "the size of the string if the first is empty"
    (is (= (distance "" "asf") 3)))
  (testing "adhoc example"
    (is (= (distance "Maillot" "Ballot") 2))))
