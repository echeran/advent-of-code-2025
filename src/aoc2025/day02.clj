(ns aoc2025.day02
  (:require [clojure.java.io :as jio]
            [clojure.string :as string]))

(defn is-id-str-doubled?
  [id-str]
  (let [num-chars (count id-str)]
    (and (even? num-chars)
         (let [id-str-split (->> id-str
                                 (split-at (/ num-chars 2)))]
           (= (first id-str-split)
              (second id-str-split))))))

(defn invalid-ids-in-range
  "Return a seq of invalid ids as longs, given a range of id strings where the range
  is a 2-elem list of id strings"
  [is-invalid-fn [start-id-str end-id-str]]
  (let [start-id (Long/parseLong start-id-str)
        end-id (Long/parseLong end-id-str)]
    (filter (comp is-invalid-fn str) (range start-id (inc end-id)))))

(defn parse-ranges
  "Given an input, return ranges as a list of 2-element lists of strings,
  where the strings are the stringified IDs"
  [line]
  (let [range-strs (string/split line #",")
        ranges (map #(string/split % #"-") range-strs)]
    ranges))

(defn p1
  []
  (with-open [rdr (jio/reader *in*)]
    (let [lines (line-seq rdr)
          first-line (first lines)
          ranges (parse-ranges first-line)
          invalid-ids (flatten (map (partial invalid-ids-in-range is-id-str-doubled?) ranges))
          invalid-id-sum (reduce + invalid-ids)]
      (println invalid-id-sum))))
