(ns com.palletops.discovery.print-api
  "Print a generated API"
  (:require
   [fipp.clojure :refer [pprint]]
   [com.palletops.api-builder.api :refer [defn-api]]))

(defn print-ns [ns-sym]
  (pprint
   `(~'ns ~ns-sym
      (:require
       [~'clj-http.client]
       [~'com.palletops.api-builder.api]
       [~'schema.core])))
  (println \newline)
  (pprint `(def ~'Connection {:endpoint schema.core/Str}))
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