---
title: To build CTS test data from source editions
layout: page
---

in fuseki/resources/ttl-src/cts, you can build the test data from source with the script cts.groovy

    groovy cts.groovy <INVENTORY> <ARCHIVEROOT> <SCHEMA>

i.e., from fuseki/resources/ttl-src/cts

    groovy cts.groovy inventory.xml editions TextInventory.rng