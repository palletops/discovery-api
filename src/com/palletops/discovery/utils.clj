(ns com.palletops.discovery.utils
  (:require
   [clojure.string :as string :refer [lower-case join split]]))

(defn camel->dashed
  "Convert a camel cased string to a hyphenated lowercase string."
  [s]
  (->> (split s #"(?<=[a-z])(?=[A-Z$])")
       (map lower-case)
       (join \-)))
