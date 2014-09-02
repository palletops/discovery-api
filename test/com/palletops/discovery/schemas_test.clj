(ns com.palletops.discovery.schemas-test
  (:require [com.palletops.discovery.schemas :refer :all]
            [clojure.test :refer :all]
            [schema.core :as schema]))

(deftest schema-type-test
  (is (= `schema/Str (schema-type {:type "string"}))
      "Produces a string schema")
  (is (= {:m `schema/Str
          `(schema/optional-key :n) `schema/Str}
         (schema-type
          {:type "object"
           :properties {:m {:type "string" :required true}
                        :n {:type "string"}}}))
      "Produces a map schema")
  (is (= ['Machine]
         (schema-type
          {:type "array" :required true :items {:$ref "Machine"}}))
      "Produces an array schema")
  (is (thrown? java.lang.IllegalArgumentException
               (schema-type {:type "unknown"}))
      "Throws on unknown type"))
