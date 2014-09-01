(defproject com.palletops/discovery-api "0.1.0-SNAPSHOT"
  :description "Generate API from a discovery document"
  :url "https://github.com/palletops/discovery-api"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[prismatic/schema "0.2.6"]
                 [cheshire "5.3.1"]
                 [fipp "0.4.3"]
                 [com.palletops/api-builder "0.3.0"]]
  :profiles {:provided {:dependencies [[org.clojure/clojure "1.6.0"]
                                       [leiningen-core "2.4.3"]]}})
