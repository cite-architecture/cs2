# `citeservlet`, second generation

A servlet running all CITE services.  Currently in development: see web page for current development at http://cite-architecture.github.io/cs2/>.


Goals of the first release version include: 

- folding in the following suite of services as subprojects:
    - Canonical Text Service
    - CITE Collection Service
    - CITE Image Extension
    - CITE Graph Service
- sharing common code where appropriate across subprojects
- end-to-end testing of all services






## Requirements ##

Integration tests use `gretty` to start up a fuseki SPARQL endpoint with a test dataset preloaded.  To run fuseki, you must have Java 7 or more recent.  Fuseki is run from the prebuilt `.war` file that requires an environment variable named `FUSEKI_BASE`.  Set this to point to the directory `fuseki/fusekibase` on your system.


## Running tests ##

To run unit tests within a subproject:

    gradle test

To run integration tests within a subproject, make sure FUSEKI_BASE is set, then

    gradle farmIntegrationTest


### Known issues ###

The shiro security environment does not fully release at the end of successful integration tests.  (It does if tests fail, however!)  When you see this message at the end of integration tests:

    INFO  Cleaning up Shiro Environment

your tests have completed successfully, and you can safely use ^C to stop the test process.


