;    This file is part of longest-common-substring
;
;    longest-common-substring is an implementation of the Reyes-Perera LCS Method
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

(ns longest-common-subsequence.core
  (:gen-class))

;;  Strategy is to reduce the longest common subsequence problem to a longest increasing subsequence problem.
;   We currently have a solution to the longest increasing subsequence problem in O(N LogN) time.
;   We can reduce the longest common subsequence between two sequences problem to the longest increasing
;   subsequence problem ins O(N LogN) time, giving us a total O(N LogN) solution.

(defn- transform-element
  "Returns a map containing the value and index of the element"
  [index element]
  {:value element
   :index index})


(defn binary-search-first-occurrence
  "Returns the index of the smallest element in sorted-seq that is larger than or equal to the search-value. If search-value is larger than the largest element in sorted-seq, returns the size of sorted-seq."
  ([sorted-seq search-value]
   (binary-search-first-occurrence sorted-seq search-value identity))
  ([sorted-seq search-value transform]
   (if (< (transform (last sorted-seq))
          search-value)
     ; If the largest value is less than the search value, return the size of the sorted-seq (which is the first index after the end of the seq)
     (count sorted-seq)
   (loop [low 0
          high (dec (count sorted-seq))]
     (if (<= high low)
       ; When we've finished searching through the entire seq, return the high index because we round the midpoint down
       high
       ; For the normal case, find the midpoint and compare the element
       (let [mid (int (+ (/ (- high low) 2) low))]
         (if (>= (transform (nth sorted-seq mid))
                 search-value)
           ; Search value is smaller than or equal to actual mid value we're examining.
           ; Need to recur on the left side
           (recur low mid)
           ; Otherwise, recurse on the right side
           (recur (inc mid) high))))))))


(defn get-truncated-seq
  "Returns the sorted elements of sorted-seq after and including the given search value"
  [sorted-seq search-value]
  ; Note: I cannot use 'java.util.Collections/binarySearch because it does not guarantee ordering of equivalent results
  (drop
    (binary-search-first-occurrence sorted-seq search-value (comp int :value))
    sorted-seq))


(defn transform-input
  "Converts the two seqs into a single new seq that can be used as input to the longest-increasing-subsequence problem"
  [seq1 seq2]
  (loop [processed1 seq1
        processed2 seq2
        matching-characters []
        escape 0]
    (if (or
          (empty? processed1)
          (empty? processed2)
          (> escape (+ (count seq1) (count seq2))))
      ; We're done if we've finished processing either coll
      matching-characters
      ; Compare the first elements of the processed seqs
      (let [element1 (first processed1)
            element2 (first processed2)
            max-val (:value (max-key (fn [element] (int (char (:value element)))) element1 element2))]
        (if (= (:value element1) (:value element2))
          ; If the elements are equal, then add them to the matching characters seq and keep going
          (recur (rest processed1) (rest processed2) (cons {:value max-val
                                                            :index1 (:index element1)
                                                            :index2 (:index element2)}
                                                           matching-characters) (inc escape))
          ; Otherwise, find the next element where the inputs are equal.
          ; Do this with a binary search on the :value
          (do 
            (recur (get-truncated-seq processed1 (int max-val)) (get-truncated-seq processed2 (int max-val)) matching-characters (inc escape))))))))



(defn get-best-and-parents-lis
  "Returns the best and parents vectors with the longest increasing subsequence data for the given collection. coll should be a collection of vectors, each with two elements: [index int-value]"
  [coll]
  (if (empty? coll)
    [[][]]
    (loop [parent [nil]
           best [(first coll)]
           index 1]
      ; Find biggest element that's smaller
      ; If this is the smallest, then replace first in array
      ; Otherwise, replace element to the right, appending to the array
      ; THEN update parent array with the element to the left of the newly appended element
      ;   nil if we replaced the first in the array
      (if (>= index (count coll))
        [best parent]
        (let [raw (nth coll index)
              value (second raw)
              next-index (inc index)]
          (if (< value (second (first best)))
            (recur (conj parent nil)
                   (cons raw (rest best))
                   next-index)
            (let [next-best-index (binary-search-first-occurrence best value second)]
              (if (>= next-best-index (count best))
                ; This is the biggest value we've seen so far
                (recur (conj parent (first (last best)))
                       (concat best [raw])
                       next-index)
                ; Otherwise, replace the specified index
                (recur (conj parent (first (nth best (dec next-best-index))))
                       (concat (take next-best-index best)
                               [raw]
                               (drop (inc next-best-index) best))
                       next-index)))))))))


(defn parse-parent
  "Returns a longest common subsequence identified by the parents array and the starting index"
  [coll parent best-index]
  (loop [parsed []
         current-index best-index]
    (let [value (nth coll current-index)
          next-index (nth parent current-index)
          next-parsed (cons value parsed)]
      (if (nil? next-index)
        next-parsed
        (recur next-parsed next-index)))))


(defn longest-increasing-subsequence
  "Finds a longest increasing subsequence in the given sequence. Uses the transform function to convert each element in the sequence to an integer"
  ([coll]
   (longest-increasing-subsequence coll identity))
  ([coll transform]
   ; Step through the list, keeping track of a Parents array [P] and a Sequence array [S]
   ; S[i] = the last element of an increasing sub-sequence of length i+1
   (let [raw (map-indexed (fn [index raw-value]
                            (vector index (transform raw-value)))
                          coll)
         [best parent] (get-best-and-parents-lis raw)]
     (parse-parent coll parent (first (last best))))))


(defn longest-common-subsequence
  "Returns a longest common subsequence of two sequences. Not necessarily consecutive"
  [seq1 seq2]
  (let [sorted1 (sort-by :value 
                            (map-indexed transform-element seq1))
        sorted2 (sort-by :value
                            (map-indexed transform-element seq2))]
    ; Sort the transformed input by the index of the first seq. 
    ; Then problem is longest increasing subsequence of :index2 over that seq
    (map :value (longest-increasing-subsequence (sort-by :index1 (transform-input sorted1 sorted2)) :index2))))


(defn- usage
  "Describes how to use the script"
  [scriptname]
  (println "Usage:\t" scriptname "[string1] [string2]")
  (println "\tPrints the longest common substring between the two given strings"))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (if (not= 2 (count args))
    ;TODO: Figure out how to put the script name here, like $0 in bash
    (usage "longest-common-subsequence")
    (println (apply str (longest-common-subsequence (first args) (second args))))))
