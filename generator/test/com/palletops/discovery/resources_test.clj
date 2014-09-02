(ns com.palletops.discovery.resources-test
  (:require [com.palletops.discovery.resources :refer :all]
            [clojure.test :refer :all]))

(deftest generate-action-test
  (is (generate-action "/" "unit" :list
                       {:id :list-units
                        :httpMethod :get
                        :path "/units"
                        :response 'Response}
                       {})))
