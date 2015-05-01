## `fuseki` subproject ##


The current 2.x series of jena fuseki does not seem to offer a runnable `jar` or `war` (at least, I cannot make sense of the list of modules accessible from maven, listed at <http://jena.apache.org/download/maven.html>, to suggest that).

Consequently, this subproject includes the version `2.0.0` binary `.jar` of fuseki server, and runs it from a `JavaExec` gradle task.

