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

Start the fuseki server with  `gradle :orca:farmRun` if it is not already running.

Load data into the `orca` endpoint at `http://localhost:8080/fuseki` from the file you created:

1. Follow the link to `Manage datasets`
2. Ad new dataset
3. check the *persistent* dataset type
4. name the dataset `orca`
5. choose "upload data"
6. leave "Destination graph name" empty, and choose "select files..." 
7. select the file you created, and choose "upload now"