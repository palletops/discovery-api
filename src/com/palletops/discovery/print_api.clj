(ns com.palletops.discovery.print-api
  "Print a generated API"
  (:require
   [clojure.java.io :as io]
   [com.palletops.api-builder.api :refer [defn-api]]
   [com.palletops.api-builder.impl]
   [fipp.clojure :refer [pprint]]))

(def fn-form #'com.palletops.api-builder.impl/fn-form)

(defn print-ns [ns-sym]
  (pprint
   `(~'ns ~ns-sym
      (:require
       [~'cheshire.core :as ~'json]
       [~'clojure.java.io :as ~'io]
       [~'com.palletops.api-builder.api :refer [~'defn-api]]
       [~'com.palletops.discovery.runtime :as ~'runtime]
       [~'schema.core])))
  (println \newline)
  (pprint `(def ~'Connection {:endpoint schema.core/Str}))
  (println \newline)
  (pprint `(defn-api ~'connect
             "Return a connection map for the given endpoint"
             {:sig [[schema.core/Str :- ~'Connection]]}
             [~'endpoint]
             {:endpoint ~'endpoint}))
  (println \newline))

(defn print-schema [{:keys [name schema]}]
  (pprint
   `(def ~name ~schema))
  (println \newline))

(defn print-fn [{:keys [name doc args sig body]}]
  (pprint
   `(defn-api ~name
      ~doc
      {:sig ~sig}
      [~@args]
      ~body))
  (println \newline))

(defn print-api
  "Print a generated api"
  [ns-sym schemas resources]
  (print-ns ns-sym)
  (doseq [schema schemas]
    (print-schema schema))
  (doseq [resource resources]
    (print-fn resource)))
