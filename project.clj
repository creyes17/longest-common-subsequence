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
