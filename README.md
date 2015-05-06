# `citeservlet`, second generation

A servlet running all CITE services.  Currently in development: see web page for current development at <http://neelsmith.github.io/cs2/>.


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

To run integration tests:

    gradle farmIntegrationTest
