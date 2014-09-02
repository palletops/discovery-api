(ns com.palletops.discovery.runtime
  "Functions used by generate API's at runtime"
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
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

(defn body-if-not-error [{:keys [body error] :as resp}]
  (if error
    resp
    body))

(defn request
  "Make an http request, using the specified callback.
  The response body will be parsed if it is json, before
  the callback is invoked.  If there is no error, only the body
  is passed to the callback."
  [opts callback]
  (http/request opts (comp callback body-if-not-error read-json)))
