---
title: Generating RDF data from source
layout: page
---


## RDF data for testing `sparqlcts` ##


in fuseki/resources/ttl-src/cts, you can build the test data from source with the script cts.groovy

    groovy cts.groovy <INVENTORY> <ARCHIVEROOT> <SCHEMA>

i.e., from fuseki/resources/ttl-src/cts

    groovy cts.groovy inventory.xml editions TextInventory.rng


## RDF data for testing `orca` ##



To run the included integration tests, you need to:

1.  Generate an RDF data set, and
2.  load the data set into fuseki


### Generate RDF data ###


In `fuseki/resources/ttl-src/orca`, run 

    ttlFrom2cols.groovy tabs-tokens.tsv > orca.ttl


### Load the RDF data into fuseki ###

Start the fuseki server with  `gradle :orca:farmRun`; load data into the `orca` endpoint from the file you created