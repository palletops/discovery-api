(ns com.palletops.discovery.utils
  (:require
   [clojure.string :as string :refer [capitalize lower-case join split]]
   [schema.core :as schema]))

(defn camel->dashed
  "Convert a camel cased string to a hyphenated lowercase string."
  [s]
  (->> (split s #"(?<=[a-z])(?=[A-Z$])")
       (map lower-case)
       (join \-)))

(defn dashed->camel
  "Convert a hyphenated string to a initial lowercase camel cased string."
  [s]
  (let [[first & others] (split s #"-")]
    (str first (->> others (map capitalize) join))))

(defn kw->clj-kw
  [k]
  {:pre [(schema/validate schema/Keyword k)]}
  (keyword (camel->dashed (name k))))

(defn kw->sym
  [k]
  {:pre [(schema/validate schema/Keyword k)]}
  (symbol (camel->dashed (name k))))

(defn kw->camel-kw
  [k]
  {:pre [(schema/validate schema/Keyword k)]}
  (keyword (dashed->camel (name k))))
