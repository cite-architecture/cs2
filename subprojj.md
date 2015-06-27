---
layout: page
title: Organization of subprojects
---



- gradle build system with subprojects for each individual CITE service, and for the coordinating servlet.  All configuration of subprojects is managed through the project's root build file.
- each subproject has distinct unit tests and integration tests.
- runs integration tests against a SPARQL endpoint with test data.  The fuseki endpoint is automatically booted by gretty.
