---
title: Testing cs2
layout: page
---



## Requirements ##

Integration tests use `gretty` to start up a fuseki SPARQL endpoint with a test dataset preloaded.  To run fuseki, you must have Java 7 or more recent.  Fuseki is run from the prebuilt `.war` file that requires an environment variable named `FUSEKI_BASE`.  Set this to point to the directory `cs2/fuseki/fusekibase` on your system.

## Overview of Steps ##

The steps for testing SparqlCTS are summarized here:

1. Configure the Gradle project, preparing the database.
1. Run SparqlCTS (with no data yet).
1. Load the [test data](https://github.com/cite-architecture/cite_test_ttl) into the database.
1. Stop SparqlCTS.
1. Run the Unit- and Integration tests.
1. **Optional.** Use the [CTS Validator](https://github.com/cite-architecture/ctsvalidator) project to confirm output to all CTS requests, based on the test data.

## Loading test data ##

- A library of texts and inventories on which unit- and integration-tests for SparqlCTS depend is a [separate repository, `cite_test_ttl` on Github](https://github.com/cite-architecture/cite_test_ttl). Clone that repository.

- You should already have cloned [the CITE Servlet 2, repository, CS2](https://github.com/cite-architecture/cs2).

- Navigate to `.../cs2/`.

- Prepare the database, using these command-line prompts (the `>` is the prompt; don't type it):

~~~
	 > gradle configure
	 > cd sparqlcts
	 > gradle farmRun
~~~

- Navigate in a browser to: `http://localhost:8080/fuseki`.
- Click on "ctsTest", and choose **Upload






A TTL data set to use with integration tests is in the file `fuseki/resources.ttl`. You can run `:sparqlcts:farmRun` to start fuseki, manually load the data in the `ds` data set, and then proceed to run tests.  (Make sure you load persistent storage.)

You can [rebuild this TTL data set from source](testdata) if you wanted to for some reason.

## Running tests ##

To run unit tests within a subproject:

    gradle test

To run integration tests within a subproject, make sure FUSEKI_BASE is set, then

    gradle farmIntegrationTest


### Known issues ###

The shiro security environment does not fully release at the end of successful integration tests.  (It does if tests fail, however!)  When you see this message at the end of integration tests:

    INFO  Cleaning up Shiro Environment

your tests have completed successfully, and you can safely use ^C to stop the test process.


