(ns com.palletops.discovery.schemas
  "Generate prismatic schemas for schemas in discovery document."
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.string :as string :refer [lower-case join split]]
            [schema.core :as schema]))

(defmulti schema-type
  "Return a type for a discovery property"
  (fn schema-type-dispatch
    [{:keys [type] :as property} {:keys [kw-f]}] type))

(defn generate-schema-map
  [properties {:keys [kw-f] :as options}]
  (reduce-kv
   (fn [m k {:keys [required] :as property}]
     (assoc m
       (if required (kw-f k) `(schema/optional-key ~(kw-f k)))
       (or (schema-type property options)
           (throw
            (ex-info
             (str "Don't know how to map discovery type '"
                  type "' to schema")
             {:property k
              :type type})))))
   {} properties))

(defmethod schema-type "string"
  [_ _]
  `schema/Str)

(defmethod schema-type "object"
  [{:keys [properties additionalProperties]}
   {:keys [kw-f] :as options}]
  (merge
   (generate-schema-map properties options)
   (if additionalProperties
     {`schema/Keyword (schema-type additionalProperties options)})))

(defmethod schema-type "array"
  [{:keys [items] :as property} _]
  (if-let [t (:$ref items)]
    [(symbol t)]
    (throw (ex-info "Don't understand array type for property"
                    {:property property}))))


(defn generate-schema
  "Generate a prismatic schema for a discovery document schema"
  [{:keys [id properties] :as s}]
  {:name (symbol id)
   :schema (generate-schema-map properties {:kw-f identity})})

(defn schemas
  "Build schemas from the discovery docs"
  [{:keys [schemas] :as discovery-doc}]
  (for [[_ s] schemas]
    (generate-schema s)))
