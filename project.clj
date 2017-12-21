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

(defproject longest-common-subsequence "1.0.0"
  :description "O(NLogN) solution to a Longest Common Subsequence"
  :url "http://github.com/creyes17/longest-common-subsequence"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot longest-common-subsequence.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :bin {:name "longest-common-subsequence"})
