(ns com.palletops.discovery.resources
  "Generate API functions for resources"
  (:require
   [clojure.string :as string :refer [lower-case]]
   [com.palletops.api-builder.core :refer [DefnMap]]
   [com.palletops.discovery.schemas :refer [generate-schema-map schema-type]]
   [com.palletops.discovery.utils :refer [camel->dashed kw->clj-kw]]
   [schema.core :as schema]))

(defn default-function-name
  "Default function name generator."
  [id]
  (symbol (string/replace (camel->dashed (name id)) "." "-")))

(defn generate-action
  [base-path resource-id action
   {:keys [id description httpMethod path parameters response]}
   {:keys [fn-name-f] :or {fn-name-f default-function-name}}]
  {:post [(schema/validate DefnMap %)]}
  (let [required (filter (fn [[p v]] (:required v)) parameters)
        optional (into {} (filter (fn [[p v]] (not (:required v))) parameters))]
    {:name (fn-name-f id)
     :type :defn
     :meta {:doc description
            :sig `[[~'Connection
                    ~(generate-schema-map parameters {:kw-f kw->clj-kw})
                    :- clojure.lang.IDeref]
                   [~'Connection
                    ~(generate-schema-map parameters {:kw-f kw->clj-kw})
                    (schema/maybe {schema/Keyword schema/Any})
                    :- clojure.lang.IDeref]
                   [~'Connection
                    ~(generate-schema-map parameters {:kw-f kw->clj-kw})
                    (schema/maybe {schema/Keyword schema/Any})
                    (schema/=> schema/Any {schema/Keyword schema/Any})
                    :- clojure.lang.IDeref]]}
     :arities [{:args
                ['connection
                 {:keys (mapv kw->clj-kw (keys parameters)) :as 'options}
                 'http-options
                 'callback]
                :body `(->
                        (runtime/request
                         (merge
                          ~'http-options
                          {:method ~(keyword (lower-case httpMethod))
                           :url (str (:endpoint ~'connection)
                                     ~base-path
                                     (-> ~path
                                         ~@(map
                                            #(do
                                               `(string/replace
                                                 ~(str "{" (name %) "}")
                                                 ~(symbol
                                                   (camel->dashed (name %)))))
                                            (keys required))))
                           :as :stream})
                         ~'callback
                         ~(if-let [r (:$ref response)]
                            (symbol (str (name r)))
                            `(schema.core/eq nil))))}
               {:args
                ['connection
                 {:keys (mapv kw->clj-kw (keys parameters)) :as 'options}
                 'http-options]
                :body `(~(fn-name-f id)
                        ~'connection ~'options ~'http-options identity)}
               {:args
                ['connection
                 {:keys (mapv kw->clj-kw (keys parameters)) :as 'options}]
                :body `(~(fn-name-f id)
                        ~'connection ~'options nil identity)}]}))

(defn generate-resource
  [base-path id methods options]
  (for [[action method] methods]
    (generate-action base-path id action method options)))

(defn resources
  "Build api functions for resources"
  [{:keys [basePath resources] :as discovery-doc} options]
  (apply concat (for [[resource {:keys [methods]}] resources]
                  (generate-resource basePath resource methods options))))
