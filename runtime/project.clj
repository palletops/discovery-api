(defproject com.palletops/discovery-api-runtime "0.1.1-SNAPSHOT"
  :description "A runtime dependency for APIs generated by discovery-api"
  :url "https://github.com/palletops/discovery-api"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[http-kit "2.1.19" :exclusions [org.clojure/clojure]]]
  :plugins [[lein-modules "0.3.8"]])