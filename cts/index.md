---
layout: page
title: Canonical Text Services (CTS)
---

For information about CTS, see <http://cite-architecture.github.io/cts/>.

Features planned for this implementation:

- draws data from a SPARQL endpoint
- expects RDF as generated by `hocuspocus`: <http://cite-architecture.github.io/hocuspocus/>
- works with object representation of OHCO2 abstract data model
- resolves notional-level URNs to a version in the SPARQL endpoint's data
- for ranges of citable nodes, identifies URN of each individual node in the range.   Identification of individual node is maintained in serialization of ranges.
- serializes to XML or JSON
- for text content including XML markup, correctly produces well-formed XML for arbitary citable passages including ranges that span containing elements
- also works with plain-text content
