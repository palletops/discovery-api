{:provided {:dependencies [[org.clojure/clojure "1.6.0"]
                           [leiningen-core "2.4.3"]]}
 :dev {:plugins [[lein-pallet-release "RELEASE"]]}
 :release {:set-version
           {:updates
            [{:path "README.md" :no-snapshot true}
             {:path "src/discovery_api/plugin.clj"
              :search-regex
              #"discovery-api-runtime \"\d+\.\d+\.\d+(-SNAPSHOT)?\""}]}}}
