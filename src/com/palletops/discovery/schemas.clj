(ns com.palletops.discovery.schemas
  "Generate prismatic schemas for schemas in discovery document."
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.string :as string :refer [lower-case join split]]
            [schema.core :as schema]))

(defmulti schema-type
  "Return a type for a discovery property"
  (fn [{:keys [type] :as property}] type))

(defn generate-schema-map
  [properties]
  (reduce-kv
   (fn [m k {:keys [required] :as property}]
     (assoc m
       (if required k `(schema/optional-key ~k))
       (or (schema-type property)
           (throw
            (ex-info
             (str "Don't know how to map discovery type '"
                  type "' to schema")
             {:property k
              :type type})))))
   {} properties))


(defmethod schema-type "string" [_] `schema/Str)

(defmethod schema-type "object"
  [{:keys [properties additionalProperties]}]
  (merge
   (generate-schema-map properties)
   (if additionalProperties
     {`schema/Keyword (schema-type additionalProperties)})))

(defmethod schema-type "array" [{:keys [items] :as property}]
  (if-let [t (:$ref items)]
    [(symbol t)]
    (throw (ex-info "Don't understand array type for property"
                    {:property property}))))


(defn generate-schema
  "Generate a prismatic schema for a discovery document schema"
  [{:keys [id properties] :as s}]
  {:name (symbol id)
   :schema (generate-schema-map properties)})

(defn schemas
  "Build schemas from the discovery docs"
  [{:keys [schemas] :as discovery-doc}]
  (for [[_ s] schemas]
    (generate-schema s)))
