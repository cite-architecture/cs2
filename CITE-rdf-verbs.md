# RDF Verbs

A list of RDF verbs used by the [Homer Multitext](http://homermultitext.org) for implementing CITE/CTS services with a Sparql backend.

## General

| Verb | Notes|
|------|------|
| http://www.w3.org/1999/02/22-rdf-syntax-ns#label | |
| http://www.w3.org/1999/02/22-rdf-syntax-ns#type | | 

## CTS Verbs

| Verb | Notes|
|------|------|
| http://www.homermultitext.org/cts/rdf/contains |  <…msA:1> `cts:contains` <…msA:1.1>. Inverse is `cts:containedBy`  |
| http://www.homermultitext.org/cts/rdf/containedBy |  Inverse of `cts:contains` |
| http://www.homermultitext.org/cts/rdf/possesses |  Identifies a relationship in the bibliographic hierarchy: namespace > work > version > exeplar. Inverse is `cts:belongsTo`. |
| http://www.homermultitext.org/cts/rdf/belongsTo |  Inverse of `cts:possesses` |
| **http://www.homermultitext.org/cts/rdf/hasPassage** | Connects a leaf-node citation to a work, version, or exemplar. E.g. `<urn:cts:greekLit:tlg0012.tlg001:> cts:hasPassage <urn:cts:greekLit:tlg0012.tlg001:1.1>` Inverse is `cts:isPassageOf`. |
| **http://www.homermultitext.org/cts/rdf/isPassageOf** | Inverse of `cts:hasPassage` |
| http://purl.org/dc/terms/title |  titles of CTS textgroups and works |
| http://www.homermultitext.org/cts/rdf/abbreviatedBy |  For CTS namespaces |
| http://www.homermultitext.org/cts/rdf/citationDepth |  |
| http://www.homermultitext.org/cts/rdf/hasSequence |  Currently only given to leaf-nodes |
| **http://www.homermultitext.org/cts/rdf/hasSubref** | Formerly "hasSubstring"  |
| http://www.homermultitext.org/cts/rdf/hasTextContent |   |
| **http://www.homermultitext.org/cts/rdf/isSubrefOf** | Formerly "isSubstringOf"  |
| http://www.homermultitext.org/cts/rdf/lang |  |
| http://www.homermultitext.org/cts/rdf/next |  |
| http://www.homermultitext.org/cts/rdf/prev |  |
| **http://www.homermultitext.org/cts/rdf/translationLang** | Check that Hocus Pocus is adding this  |
| http://www.homermultitext.org/cts/rdf/xmlns |  |
| http://www.homermultitext.org/hmt/rdf/xmlOpen |  |
| http://www.homermultitext.org/hmt/rdf/xpTemplate |  |


## CITE Verbs

| Verb | Notes|
|------|------|
| http://purl.org/ontology/olo/core#item |  |
| http://www.homermultitext.org/cite/rdf/abbreviatedBy |  |
| http://www.homermultitext.org/cite/rdf/belongsTo |  |
| http://www.homermultitext.org/cite/rdf/canonicalId |  |
| http://www.homermultitext.org/cite/rdf/collProperty |  |
| http://www.homermultitext.org/cite/rdf/ordered |  |
| http://www.homermultitext.org/cite/rdf/orderedBy |  |
| http://www.homermultitext.org/cite/rdf/possessedBy |  |
| http://www.homermultitext.org/cite/rdf/possesses |  |
| http://www.homermultitext.org/cite/rdf/propLabel |  |
| http://www.homermultitext.org/cite/rdf/propType |  |

## ORCA Specific

| Verb | Notes|
|------|------|
| http://www.homermultitext.org/orca/rdf/exemplifiedBy | Expresses the relationship of a version-level CTS URN with an analytical exemplar URN. E.g. `<urn…msA:1.1@μῆνιν[1]> orca:exemplifiedBy <urn…msA.wt:1.1.1>. Inverse is `orca:exemplifies`.  |

| http://www.homermultitext.org/orca/rdf/exemplifies | Inverse of `orca:exemplifiedBy`. |
| http://www.homermultitext.org/orca/rdf/analyzes | <ORCA-Relation-URN> `analyzes` <CTS-URN>. Inverse is `analyzedBy`.    |
| http://www.homermultitext.org/orca/rdf/analyzedBy | Inverse of `orca:analyzes`  |
| http://www.homermultitext.org/orca/rdf/hasAnalysis | <ORCA-Relation-URN> `orca:hasAnalysis` <CITE-Object-URN>. Inverse is `orca:analysisFor`.  |
| http://www.homermultitext.org/orca/rdf/analysisFor | Inverse of `orca:hasAnalysis`. |
| http://www.homermultitext.org/orca/rdf/textDeformation | <ORCA-Relation-URN> `orca:textDeformation "string-value".  |

## Homer Multitext Verbs

| Verb | Notes|
|------|------|
| http://www.homermultitext.org/hmt/citedata/msA_Label |  |
| http://www.homermultitext.org/hmt/citedata/msA_RV |  |
| http://www.homermultitext.org/hmt/citedata/msA_Sequence |  |
| http://www.homermultitext.org/hmt/citedata/venAsign_CriticalSign |  |
| http://www.homermultitext.org/hmt/citedata/venAsign_Sequence |  |
| http://www.homermultitext.org/hmt/citedata/venAsign_TextPassage |  |

| http://www.homermultitext.org/hmt/rdf/commentsOn |  We leave this for now, as a project-specific verb; ultimately, the text-scholia relationship should be expressed by an ORCA relation. Inverse is `hmt:commentedOnBy`. |
| http://www.homermultitext.org/hmt/rdf/commentedOnBy | Inverse of `hmt:commentsOn` |

## DSE Verbs (Digital Scholarly Editions)

| Verb | Notes|
|------|------|
| http://www.homermultitext.org/dse/rdf/appearsOn |  |
| http://www.homermultitext.org/dse/rdf/hasOnIt |  |
| http://www.homermultitext.org/dse/rdf/illustratedBy |  |
| http://www.homermultitext.org/dse/rdf/illustrates |  |
