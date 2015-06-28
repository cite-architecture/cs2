---
title: "The fuseki server included for testing of cs2"
layout: page
---

The project defines a server farm that includes [fuseki](http://jena.apache.org/documentation/serving_data/)  for subprojects that need a SPARQL endpoint to test against for integration testing.  Test data for each subproject is segregated in its own fuseki dataset, as follows:

- test data for `sparqlcts` is served from `http://localhost:8080/fuseki/cts`
- test data for `sparqlcc` is served from `http://localhost:8080/fuseki/cc`
-  test data for `orca` is served from `http://localhost:8080/fuseki/orca`
-  test data for `citeimg` is served from `http://localhost:8080/fuseki/img`
-  test data for `graph` is served from `http://localhost:8080/fuseki/citegraph`  

