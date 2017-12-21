;    This file is part of longest-common-subsequence
;
;    longest-common-subsequence is an implementation of the Reyes-Perera LCS Method
;    for finding the longest common subsequence between two inputs in O(N logN) time.
;
;    Copyright (C) 2017  Christopher R Reyes
;
;    This program is free software: you can redistribute it and/or modify
;    it under the terms of the GNU General Public License as published by
;    the Free Software Foundation, either version 3 of the License, or
;    (at your option) any later version.
;
;    This program is distributed in the hope that it will be useful,
;    but WITHOUT ANY WARRANTY; without even the implied warranty of
;    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;    GNU General Public License for more details.
;
;    You should have received a copy of the GNU General Public License
;    along with this program.  If not, see <http://www.gnu.org/licenses/>.


(ns longest-common-subsequence.core-test
  (:require [clojure.test :refer :all]
            [longest-common-subsequence.core :refer :all]))

(deftest binary-search-test
  (testing "Binary Search returns first element that matches"
    (is (= (binary-search-first-occurrence [7 8 8 20] 8) 1))
    (is (= (binary-search-first-occurrence [7 8 8 20] 7) 0))
    (is (= (binary-search-first-occurrence [7 8 8 20] 20) 3))
    (is (= (binary-search-first-occurrence [{:foo 1} {:foo 2} {:foo 3}] 1 :foo) 0))
    (is (= (binary-search-first-occurrence [{:foo 1} {:foo 2} {:foo 3}] 2 :foo) 1))
    (is (= (binary-search-first-occurrence [{:foo 1} {:foo 2} {:foo 3}] 3 :foo) 2))))

(deftest lis-parents-test
  (testing "Test Single Element"
    (let [index 0
          value 1
          [best parent] (get-best-and-parents-lis [[index value]])
          best-element (first best)
          parent-element (first parent)]
      (is (= 1 (count best)))
      (is (= 1 (count parent)))
      (is (= index (first best-element)))
      (is (= value (second best-element)))
      ; The parent element of the first best element should be nil
      (is (nil? parent-element))))
  (testing "Test increasing elements"
    (let [small-value 3
          large-value (+ small-value 17)
          [best parent] (get-best-and-parents-lis [[0 small-value] [1 large-value]])
          first-best (first best)
          second-best (second best)]
      (is (= 2 (count best)))
      (is (= 0 (first first-best)))
      (is (= small-value (second first-best)))
      (is (= 1 (first second-best)))
      (is (= large-value (second second-best)))
      (is (= 2 (count parent)))
      (is (nil? (first parent)))
      (is (= 0 (second parent)))))
  (testing "Test decreasing elements"
    (let [small-value 3
          large-value (+ small-value 17)
          [best parent] (get-best-and-parents-lis [[0 large-value] [1 small-value]])
          best-element (first best)]
      (is (= 1 (count best)))
      (is (= 1 (first best-element)))
      (is (= small-value (second best-element)))
      (is (= 2 (count parent)))
      (is (nil? (first parent)))
      (is (nil? (second parent)))))
  (testing "Decreasing then increasing elements"
    (let [small-value 2
          medium-value (+ small-value 3)
          large-value (+ medium-value 17)
          [best parent] (get-best-and-parents-lis [[0 large-value] [1 small-value] [2 medium-value]])
          first-best (first best)
          second-best (second best)]
      (is (= 2 (count best)))
      (is (= 1 (first first-best)))
      (is (= small-value (second first-best)))
      (is (= 2 (first second-best)))
      (is (= medium-value (second second-best)))
      (is (= 3 (count parent)))
      (is (nil? (first parent)))
      (is (nil? (second parent)))
      (is (= 1 (nth parent 2)))))
  (testing "Increasing then decreasing elements"
    (let [small-value 2
          medium-value (+ small-value 3)
          large-value (+ medium-value 17)
          [best parent] (get-best-and-parents-lis [[0 small-value] [1 large-value] [2 medium-value]])
          first-best (first best)
          second-best (second best)]
      (is (= 2 (count best)))
      (is (= 0 (first first-best)))
      (is (= small-value (second first-best)))
      (is (not= 0 (first second-best)))
      (is (> (second second-best) (second first-best)))
      (is (= 3 (count parent)))
      (is (nil? (first parent)))
      (is (= 0 (second parent)))
      (is (= 0 (nth parent 2)))))
  (testing "Both increasing and decreasing elements"
    (let [readable-input [1 9 2 8 3 7]
          input (map-indexed vector readable-input)
          [best parent] (get-best-and-parents-lis input)]
      (is (= 4 (count best)))
      (is (= 1 (second (first best))))
      (is (= 2 (second (second best))))
      (is (= 3 (second (nth best 2))))
      (is (= 7 (second (nth best 3))))
      (is (= 6 (count parent)))
      (is (nil? (first parent)))
      (is (= 0 (second parent)))
      (is (= 0 (nth parent 2)))
      (is (= 2 (nth parent 3)))
      (is (= 2 (nth parent 4)))
      (is (= 4 (nth parent 5))))))


(deftest parse-test
  (testing "Single element"
    (let [coll [\a]
          parent [nil]
          best-index 0
          parsed (parse-parent coll parent best-index)]
      (is (= 1 (count parsed)))
      (is (= coll parsed))))
  (testing "Two single elements"
    (let [coll [\a \b]
          parent [nil nil]
          parsed0 (parse-parent coll parent 0)
          parsed1 (parse-parent coll parent 1)]
      (is (= 1 (count parsed0)))
      (is (= 1 (count parsed1)))
      (is (= \b (first parsed1)))
      (is (= \a (first parsed0)))))
  (testing "Two linked elements"
    (let [coll [\a \b]
          parent [nil 0]
          parsed1 (parse-parent coll parent 1)
          parsed0 (parse-parent coll parent 0)]
      (is (= 2 (count parsed1)))
      (is (= \a (first parsed1)))
      (is (= \b (second parsed1)))
      (is (= 1 (count parsed0)))
      (is (= \a (first parsed0))))))
