# discovery-api

An library for generating an API from a Google API
[discovery document][discovery-document].

## Installation

Add `[com.palletops/discovery-api "0.1.2-SNAPSHOT"]` to your `:dependencies`.

## Usage

```clj
(require '[com.palletops.fleet.generator :as generator])
```

You need a clojure map that contains the discovery document, with keys
that are keywords.  You can read a json file from resources using the
`document-from-resource` function.

```clj
(def doc (document-from-resource "fleet-v1-alpha"))
```

To generate an API namespace, pass the document to the `api-ns-string`
function, that returns a string that contains the source code for you
API namespace, which you can write to file.

```clj
(api-ns-string doc (symbol (str "com.palletops.fleet." version)) options)
```

For a full example, see [clj-fleet][clj-fleet-generator].

## License

Copyright © 2014 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[discovery-document]:https://developers.google.com/discovery/v1/reference/apis "Google Discovery Document"
[clj-fleet-generator]:https://github.com/palletops/clj-fleet/blob/develop/dev-src/com/palletops/fleet/generator.clj "Fleet API generator"
