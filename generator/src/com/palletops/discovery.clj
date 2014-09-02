(ns com.palletops.discovery
  "Build HTTP clients from discovery documents"
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [clojure.string :as string :refer [lower-case join split]]
   [com.palletops.api-builder.api :refer [defn-api]]
   [com.palletops.discovery.print-api :refer [print-api]]
   [com.palletops.discovery.resources :refer [resources]]
   [com.palletops.discovery.schemas :refer [schemas]]
   [schema.core :as schema]))

(def Document {schema/Keyword schema/Any})

(def Options
  {(schema/optional-key :fn-name-f) (schema/=> schema/Keyword schema/Symbol)})

(defn-api document-from-resource
  "Load a discovery document from a resource."
  {:sig [[schema/Str :- Document]]}
  [resource-path]
  (if-let [url (io/resource resource-path)]
    (json/parse-stream (io/reader url) keyword)
    (throw (ex-info
            (str "Could not find resource with path: " (pr-str resource-path))
            {:type :discovery/no-such-resource
             :resource-path resource-path}))))

(defn-api api-ns-string
  "Return a string containing a namespace definition for an API generated
  from the the clojure representation of a discovery document."
  {:sig [[Document schema/Symbol Options :- schema/Str]]}
  [document ns-sym options]
  (with-out-str
    (print-api ns-sym (schemas document) (resources document options))))

;;; ## Resources

(comment
  (def s (document-from-resource "discovery/v1-alpha"))
  (schemas s)
  (resources s)

  (defn fleet-fn-name [id]
    (-> (com.palletops.discovery.resources/default-function-name id)
        (string/replace "fleet-" "")
        symbol))

  (let [options {:fn-name-f fleet-fn-name}
        c (api-ns-string 'com.palletops.fleet s options)
        f (io/file "target/generated/src/com/palletops/fleet.clj")]
    (.mkdirs (.getParentFile f))
    (spit f  c))
  )
