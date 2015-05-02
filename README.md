# cs2

Experimental refactoring of cite servlet.

Goals include: 

- folding in the suite of sparql* services as subprojects.
- sharing common code where appropriate across subprojects
- end-to-end testing of services

The `fuseki` subproject currently has a task that starts up a SPARQL endpoint with a test data set, but I don't know how to run it in the background, so testing requires:

1. `gradle startFuseki`
2. then whatever test(s) you want to run



Perhaps in the future, this could use something like ant's `exec` task with `spawn=true` but at present, it doesn't.
