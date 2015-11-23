# Relations for CITE Graph

## CTS Relations

- ref hierarchy
- frbr ontology
- sequence
- cts:isSubstringOf, cts:hasSubstring
- versions (editions and translations) from work

*N.b.* Any query that returns leaf-nodes should return all CTS data for those leaf-nodes.

## CITE Relations

- cite:illustratedBy, cite:illustrates
- cite:possesses, cite:belongsTo
- olo:next, olo:prev
- cite:isExendedRe, cite:hasExtendedRef
- cite:analyzes, cite:analysisFor, cite:analyzedBy
- cite:transformedToString


## HMT Relations

- hmt:commentsOn, hmt:commentedOnBy
- dse:hasOnIt, dse:appearsOn
- dse:hasDefaultImage

# Tests

> Given the input-URN, what results will we want to see?

## urn:cts:greekLit.tlg0012.tlg001.msA:1.1

- All CTS records for this line.
- An image-roi.
- Any image w/o the roi; the image-part of the image-roi (check and eliminate
  duplicates).
- A folio urn.
- All tokens hasSubstring
- CITE objects indexed
	- commentedOnBy.
	- cite:analyzes (requires any substrings). 

## 



