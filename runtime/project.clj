(defproject com.palletops/discovery-api-runtime "0.1.2"
  :description "A runtime dependency for APIs generated by discovery-api"
  :url "https://github.com/palletops/discovery-api"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[cheshire "5.3.1"]
                 [http-kit "2.1.19" :exclusions [org.clojure/clojure]]
                 [prismatic/schema "0.2.6"]]
  :plugins [[lein-modules "0.3.8"]])
