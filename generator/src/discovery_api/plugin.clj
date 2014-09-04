(ns discovery-api.plugin
  "Leiningen plugin to provide a project profile for projects using a
  generated api."
  (:require
   [leiningen.core.project :refer [add-profiles]]))

(def profiles
  {:discovery-api
   {:dependencies [['com.palletops/discovery-api-runtime "0.1.2-SNAPSHOT"]]}})

(defn middleware
  [project]
  (add-profiles project profiles))
