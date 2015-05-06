---
layout: page
title: CITE servlet
---

A servlet including implementations of the following suite of services:

- [Canonical Text Services](cts)
- CITE Collection Services
- CITE Image Extension
- CITE Graph Service



## Technical details ##

- git repository <https://github.com/neelsmith/cs2> with public issue tracker <https://github.com/neelsmith/cs2/issues>.
- gradle build system with subprojects for each individual CITE service, and for the coordinating servlet.  All configuration of subprojects is managed through the project's root build file.
- each subproject has distinct unit tests and integration tests.
- runs integration tests against a SPARQL endpoint with test data.  The fuseki endpoint is automatically booted by gretty.


Usage for unit and integration tests, respectively, within a subproject:

    gradle test
    gradle farmIntegrationTest


To be added:

- concordion specifications for each subproject