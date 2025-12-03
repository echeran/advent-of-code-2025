(ns aoc2025.core
  (:require [clojure.tools.cli :as cli]
            [clojure.string :as string]
            [aoc2025.day01 :as day01]
            [aoc2025.day02 :as day02]))

(def cli-options
  [

   ["-d" "--day DAY" ;; "Advent Day of Code Day Number"
    ;; :default -1
    :parse-fn #(Integer/parseInt %)
    :validate [#(<= 1 % 12) "Must be an integer from 1 to 12, inclusive"]]

   ["-p" "--problem PROBLEM" ;;
    ;; :default -1
    :parse-fn #(Integer/parseInt %)
    :validate [#(<= 1 % 2) "Must be an integer that is either 1 or 2"]]

   ["-h" "--help"]

   ])

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn usage [options-summary]
  (->> ["My code dabblings for Advent of Code 2025."
        ""
        "Usage: aoc2025 [options]"
        ""
        "Options:"
        options-summary
        ""]
       (string/join \newline)))

(defn validate-args
  "Validate command line arguments. Either return a map indicating the program
  should exit (with an error message, and optional ok status), or a map
  indicating the action the program should take and the options provided."
  [args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options) ; help => exit OK with usage summary
      {:exit-message (usage summary) :ok? true}
      
      errors ; errors => exit with description of errors
      {:exit-message (error-msg errors)}

      (not (:day options))
      {:exit-message (str \newline
                          "--day option required"
                          \newline
                          \newline
                          (usage summary))
       :ok? false}

      (not (:problem options))
      {:exit-message (str \newline
                          "--problem option required"
                          \newline
                          \newline
                          (usage summary))
       :ok? false}
      
      ;; ;; custom validation on arguments
      ;; (and (= 1 (count arguments))
      ;;      (#{"start" "stop" "status"} (first arguments)))

      ;; always run with given options
      true
      {:options options}
      
      :else ; failed custom validation => exit with usage summary
      {:exit-message (usage summary)})))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (case [(:day options) (:problem options)]
        [1 1] (day01/p1)
        [1 2] (day01/p2)
        [2 1] (day02/p1)
        [2 2] (day02/p2)))))
