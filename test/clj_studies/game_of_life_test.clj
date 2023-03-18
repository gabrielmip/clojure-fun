(ns clj-studies.game-of-life-test
  (:require [clojure.test :refer :all]
            [clj-studies.game-of-life :refer [lives?]]))

(deftest game-of-life
  (testing "alive alone"
    (is (= false (lives? #{[0 0]} [0 0]))))

  (testing "alive one neighboor"
    (is (= false (lives? #{[0 0] [0 1]} [0 0]))))

  (testing "alive two neighboors"
    (is (= true (lives? #{[0 0] [0 1] [1 1]} [0 0]))))

  (testing "alive three neighboors"
    (is (= true (lives? #{[0 0] [0 1] [1 1] [1 0]} [0 0]))))

  (testing "dead one neigh"
    (is (= false (lives? #{} [1 1]))))

  (testing "dead two neigh"
    (is (= false (lives? #{[0 1] [0 0]} [1 1]))))

  (testing "dead three neigh"
    (is (= true (lives? #{[0 1] [0 0] [2 1]} [1 1]))))

  (testing "dead four neigh"
    (is (= false (lives? #{[0 1] [0 0] [0 2] [2 2]} [1 1]))))

  (testing "alive in center alone"
    (is (= false (lives? #{[1 1]} [1 1]))))

  (testing "alive in center one neigh"
    (is (= false (lives? #{[1 1] [0 1]} [1 1]))))

  (testing "alive in center two neigh"
    (is (= true (lives? #{[1 1] [0 0] [0 1]} [1 1]))))

  (testing "alive in center three neigh"
    (is (= true (lives? #{[1 1] [0 0] [0 1] [1 2]} [1 1]))))

  (testing "alive in center four neigh"
    (is (= false (lives? #{[1 1] [0 0] [0 1] [1 2] [2 2]} [1 1]))))

  (testing "alive in center five neigh"
    (is (= false (lives? #{[1 1] [0 0] [0 1] [0 2] [1 2] [2 2]} [1 1])))))

