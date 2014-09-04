## 0.1.1

- Add :body parameter for post/put request
  Translate the keys of the map passed to post/put requests to be camel
  cased, and pass to the http method.

- Actually use query parameters in http requests

- Translate keys of body data to be dashed keywords
  Makes keys more idiomatic for clojure.

- Make generated api asynch a la http-kit
  Functions return a promise and take an optional callback, in the same way
  http-kit client requests work.

- Pass all params as a single map
  Makes it easier to construct a request as a data structure.

- Add a :kw-f option to schema generation

- Add a runtime jar
  The runtime jar contains code used by the generated APIs.  This avoids
  having to generate this code in every generated API.

## 0.1.0

- Initial release
