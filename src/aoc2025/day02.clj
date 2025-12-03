(ns aoc2025.day02
  (:require [clojure.java.io :as jio]
            [clojure.string :as string]))

(defn is-id-str-repeated-n-times?
  [id-str n]
  (let [num-chars (count id-str)
        partition-size (let [init-psize (quot num-chars n)]
                         (if (zero? init-psize)
                           1
                           init-psize))
        ;; _ (println "partition size:" partition-size)
        chars-partitions (partition-all partition-size id-str)]
    (and (= (count (first chars-partitions))
            (count (last chars-partitions)))
         (apply = chars-partitions))))


(defn is-id-str-doubled?
  [id-str]
  (let [num-chars (count id-str)]
    (and (even? num-chars)
         (let [id-str-split (->> id-str
                                 (split-at (/ num-chars 2)))]
           (= (first id-str-split)
              (second id-str-split))))))

(defn is-id-str-repeated?
  "Note: this is not an efficient implementation. One among many possible
  ways to improve efficiency:
  Don't iterate over the number of times of repeating. Instead, iterate over the partition size, from 1 to (quot num-chars 2), to avoid potentially duplicate iterations."
  [id-str]
  (let [num-chars (count id-str)]
    (let [possible-repeat-counts (range 2 (inc num-chars))]
      (some (partial is-id-str-repeated-n-times? id-str) possible-repeat-counts))))

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

(defn p2
  []
  (with-open [rdr (jio/reader *in*)]
    (let [lines (line-seq rdr)
          first-line (first lines)
          ranges (parse-ranges first-line)
          invalid-ids (flatten (map (partial invalid-ids-in-range is-id-str-repeated?) ranges))
          invalid-id-sum (reduce + invalid-ids)]
      (println invalid-id-sum))))
