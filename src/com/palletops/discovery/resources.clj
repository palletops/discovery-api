(ns com.palletops.discovery.resources
  "Generate API functions for resources"
  (:require
   [clojure.string :as string :refer [lower-case]]
   [com.palletops.discovery.schemas :refer [generate-schema-map schema-type]]
   [com.palletops.discovery.utils :refer [camel->dashed]]))

(defn default-function-name
  "Default function name generator."
  [id]
  (symbol (string/replace (camel->dashed (name id)) "." "-")))

(defn generate-action
  [base-path resource-id action
   {:keys [id description httpMethod path parameters response]}
   {:keys [fn-name-f] :or {fn-name-f default-function-name}}]
  (let [required (filter (fn [[p v]] (:required v)) parameters)
        optional (into {} (filter (fn [[p v]] (not (:required v))) parameters))]
    {:name (fn-name-f id)
     :doc description
     :args (vec (concat
                 ['connection]
                 (map (fn [[p v]] (symbol (camel->dashed (name p)))) required)
                 ['options]))
     :sig `[[~'Connection
             ~@(map schema-type (vals required))
             ~(generate-schema-map optional)
             :-
             ~(if-let [r  (:$ref response)]
                (symbol (name r))
                `(schema.core/eq nil))]]
     :body `(->
             @(~(symbol (str "org.httpkit.client") (lower-case httpMethod))
               (str (:endpoint ~'connection)
                    ~base-path
                    (-> ~path
                        ~@(map #(do
                                  `(string/replace
                                    ~(str "{" (name %) "}")
                                    ~(symbol (camel->dashed (name %)))))
                               (keys required))))
               {:as :stream}
               ~'read-json)
             :body)}))

(defn generate-resource
  [base-path id methods options]
  (for [[action method] methods]
    (generate-action base-path id action method options)))

(defn resources
  "Build api functions for resources"
  [{:keys [basePath resources] :as discovery-doc} options]
  (apply concat (for [[resource {:keys [methods]}] resources]
                  (generate-resource basePath resource methods options))))
