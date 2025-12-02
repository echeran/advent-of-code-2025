(ns aoc2025.day01
  (:require [clojure.java.io :as jio]))

(defn parse-line
  "Return a vector of parsed parts from the input line.
  Input line is a string. Format is [L|R](\\d+)
  Return value is a vector of parts, where the first element is a char, and the second element is a number."
  [line]
  (let [left-right-char (first line)
        number (Integer/parseInt (subs line 1))]
    [left-right-char number]))

(defn add-step-vec
  [begin-num [left-right-char increment]]
  (if (= \R left-right-char)
    (mod (+ begin-num increment) 100)
    (mod (- begin-num increment) 100)))

(defn compute-password
  "Return the number of times the number lands on 0.
Start at 50.
Dial is circular and goes from 0 to 99.
Input is a seq of vectors of the form [L/R number],
where L/R indicates to turn the dial left (counterclockwise)
or right (clockwise), and number indicates the number
of steps on the dial to advance in the L/R direction"
  [step-vecs]
  (let [init-number 50 ;; given by problem
        landing-nums (reductions add-step-vec init-number step-vecs)
        num-zeroes (->> landing-nums
                        (filter #(= 0 %))
                        count)]
    num-zeroes))

(defn p1
  []
  (with-open [rdr (jio/reader *in*)]
    (let [lines (line-seq rdr)
          parsed-lines (map parse-line lines)
          password (compute-password parsed-lines)]
      (println password))))
