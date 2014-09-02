(defproject com.palletops/discovery-api-project "0.1.1-SNAPSHOT"
  :description "Generate API from a discovery document"
  :url "https://github.com/palletops/discovery-api"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-modules "0.3.8"]]
  :aliases {"install" ["modules" "install"]
            "deploy" ["modules" "deploy"]
            "check" ["modules" "check"]
            "test" ["modules" "test"]
            "clean" ["with-profile" "+no-subprocess" "modules" "clean"]})
