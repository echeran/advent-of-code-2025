(ns aoc2025.day03
  (:require [clojure.java.io :as jio]
            [clojure.string :as string]))

(defn joltage-n-helper
  "Return the selected n digits for joltage in the line, preserving relative order from the input,
  given n as an integral number, and digits as a seq of integral numbers"
  [n digits]
  (loop [remaining-digits digits
         curr-n n
         selected-digits []]
    (if (zero? curr-n)
      selected-digits
      (let [max-next-digit (apply max (drop-last (dec curr-n) remaining-digits))
            new-remaining-digits (->> remaining-digits
                                      (drop-while #(not= % max-next-digit))
                                      rest)
            new-curr-n (dec curr-n)
            new-selected-digits (conj selected-digits max-next-digit)]
        (recur new-remaining-digits
               new-curr-n
               new-selected-digits)))))

(defn joltage-n
  "Return the n digit number (as number type)
  when given as input a line (string) representing a bank of batteries' digits"
  [n line]
  (let [digits (->> line
                    (map int)
                    (map #(- % 48)))
        selected-digits (joltage-n-helper n digits)]
    (Long/parseLong (apply str selected-digits))))

(defn joltage-2
  "Return the 2-digit joltage number (as number type)
  when given as input a line (string) representing a bank of batteries' digits"
  [line]
  (let [digits (->> line
                    (map int)
                    (map #(- % 48)))
        max-first-digit (apply max (butlast digits))
        digits-after-first (->> digits
                                (drop-while #(not= % max-first-digit))
                                rest)
        max-second-digit (apply max digits-after-first)]
    (+ max-second-digit (* 10 max-first-digit))))

(defn p1
  []
  (with-open [rdr (jio/reader *in*)]
    (let [lines (line-seq rdr)
          joltages (map joltage-2 lines)
          result (reduce + joltages)]
      (println result))))


(defn p2
  []
  (with-open [rdr (jio/reader *in*)]
    (let [lines (line-seq rdr)
          joltages (map (partial joltage-n 12) lines)
          result (reduce + joltages)]
      (println result))))
