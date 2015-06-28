---
title: Goals of the cs2 project
layout: page
---


This is a complete, ground-up redesign of the original `citeservlet`.  It includes the complete source for building the following services as subprojects:

- Canonical Text Service
- CITE Collection Service
- CITE Image Extension
- CITE Graph Service
- ORCA Service


The main goals of this redesign are:

1. design classes representing the abstract models behind each of the implemented services:
    - the OHCO2 model of texts underlying CTS
    - the CITE Object model underlying the CITE Collection Service
    - the CITE Image model underlying CITE Image Extension to Collection Service
    - the RDF graph model underlying the CITE Graph Service
    - the ORCA model of aligning citable analyses of texts with the OCHO2 content they analyze, which underlies the ORCA Service
2. share common code, where appropriate, across subprojects.    Specifically, interaction with a SPARQL endpoint is abstracted and shared by all `cs2` services.
3. end-to-end testing of all services.  Each service includes suites of:
    - unit tests on all classes implementing the CITE Architecture's abstract models
    - integration tests on the interaction of all implementing classes with a SPARQL endpoint
    - specification tests providing human-readable prose with testable living documentation.  Tests are written in markdown,  converted to HTML and tested with concordion.
4.  documentation published on this site for each service:
    - concordion living documentation
    - API documentation

