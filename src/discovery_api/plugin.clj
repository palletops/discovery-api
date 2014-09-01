(ns discovery-api.plugin
  "Leiningen plugin to provide a project profile for projects using a
  generated api."
  (:require
   [leiningen.core.project :refer [add-profiles]]))

(def profiles
  {:discovery-api
   {:dependencies [['clj-http "1.0.0"]]}})

(defn middleware
  [project]
  (add-profiles project profiles))
