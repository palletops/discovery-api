(ns com.palletops.discovery.runtime
  "Functions used by generate API's at runtime"
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [com.palletops.discovery.utils :refer [kw->camel-kw kw->clj-kw]]
   [org.httpkit.client :as http]
   [schema.core :as schema]))

(defn success?
  "Check for a successful HTTP status code."
  [{:keys [status]}]
  (<= 200 status 299))

(defn read-json
  "Parse the response body if it is json."
  [resp]
  (if (if-let [ct (:content-type (:headers resp))]
        (.startsWith ct "application/json"))
    (update-in resp [:body] #(json/parse-stream (io/reader %) keyword))
    resp))

(defn key-coercer
  "Coerce the keys in maps."
  [schema key-coercer-f]
  (schema/start-walker
   (fn [s]
     (let [walk (schema/walker s)]
       (fn [x]
         (let [result (walk x)]
           (if (map? result)
             (zipmap (map key-coercer-f (keys result)) (vals result))
             result)))))
   schema))

(defn key->clj-kw
  "Coerce the keys in maps from keywords to clojure-style keywords"
  [schema]
  (key-coercer schema kw->clj-kw))

(defn key->camel-kw
  "Coerce the keys in maps from keywords to clojure-style keywords"
  [schema]
  (key-coercer schema kw->camel-kw))

(defn body-if-not-error
  [{:keys [body error] :as resp} body-schema]
  (if (:error body)
    (update-in resp [:opts] dissoc :response)
    ((key->clj-kw body-schema) (schema/validate body-schema body))))

(defn request
  "Make an http request, using the specified callback.
  The response body will be parsed if it is json, before
  the callback is invoked.  If there is no error, only the body
  is passed to the callback."
  [opts callback body-schema]
  (http/request
   opts
   (comp callback #(body-if-not-error % body-schema) read-json)))

;; (def S {:aB schema/Keyword})

;; ((key->clj-kw S) {:aB :k})
