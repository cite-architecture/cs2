---
title: Testing cs2
layout: page
---



## Requirements ##

Integration tests use `gretty` to start up a fuseki SPARQL endpoint with a test dataset preloaded.  To run fuseki, you must have Java 7 or more recent.  Fuseki is run from the prebuilt `.war` file that requires an environment variable named `FUSEKI_BASE`.  Set this to point to the directory `fuseki/fusekibase` on your system.

## Loading test data ##


A TTL data set to use with integration tests is in the file `fuseki/resources.ttl`. You can run `:sparqlcts:farmRun` to start fuseki, manually load the data in the `ds` data set, and then proceed to run tests.  (Make sure you load persistent storage.)

## Running tests ##

To run unit tests within a subproject:

    gradle test

To run integration tests within a subproject, make sure FUSEKI_BASE is set, then

    gradle farmIntegrationTest


### Known issues ###

The shiro security environment does not fully release at the end of successful integration tests.  (It does if tests fail, however!)  When you see this message at the end of integration tests:

    INFO  Cleaning up Shiro Environment

your tests have completed successfully, and you can safely use ^C to stop the test process.


