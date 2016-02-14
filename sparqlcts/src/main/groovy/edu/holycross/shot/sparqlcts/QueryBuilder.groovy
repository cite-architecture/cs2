package edu.holycross.shot.sparqlcts

import edu.harvard.chs.cite.CtsUrn

abstract class QueryBuilder {




  /** Builds SPARQL query string to retrieve all 
   * relevant data about a citable leaf node.
   */
  static String getLeafNodeQuery(CtsUrn urn) {
    return getLeafNodeQuery(urn, 0)
  }
  
  static String getLeafNodeQuery(CtsUrn urn, Integer context) {
    return """
    ${CtsDefinitions.prefixPhrase}

    SELECT ?psg ?txt ?anc ?xpt ?xmlns WHERE {
    ?psg cts:belongsTo <${urn.getUrnWithoutPassage()}> .
    ?psg cts:hasTextContent ?txt .
    ?psg cts:hasSequence ?s .
    ?psg hmt:xpTemplate ?xpt .
    ?psg hmt:xmlOpen ?anc  .
    ?psg hmt:xmlNsDecl ?xmlns  .

    {
      { SELECT (xsd:int(?seq) + ${context} AS ?max)
       WHERE {
       <${urn}> cts:hasSequence ?seq .
       }
      }
  
     { SELECT (xsd:int(?seq) - ${context} AS ?min)
       WHERE {
       <${urn}> cts:hasSequence ?seq .
       }
     }
   }
   FILTER (?s <= ?max) .
   FILTER (?s >= ?min) .
  }
  ORDER BY ?s 

     """
  }

  

  /** Builds SPARQL query string to retrieve a 
   * rdf label fo a URN.
   */
  static String getRdfLabel(CtsUrn urn) {
    return """
        ${CtsDefinitions.prefixPhrase}
        SELECT ?label WHERE {
          <${urn}> rdf:label ?label .
        }
     """
  }


  /** Builds SPARQL query string to find
   * URN of leaf node preceding the 
   * requested leaf node.
   * @param urn URN at leaf node level.
   * @returns A complete SPARQL query string.
   */
  static String getPrevUrnQuery(CtsUrn urn) {
    String workUrnStr = urn.getUrnWithoutPassage()
    return """
    ${CtsDefinitions.prefixPhrase}
    SELECT ?prevUrn ?prevSeq WHERE {
    <${urn}> cts:prev ?prevUrn .
    ?prevUrn cts:hasSequence ?prevSeq .
    }
    """
  }




    /** Builds SPARQL query string to find
   * URN of leaf node following the 
   * requested leaf node.
   * @param urn URN at leaf node level.
   * @returns A complete SPARQL query string.
   */
  static String getNextUrnQuery(CtsUrn urn) {
    String workUrnStr = urn.getUrnWithoutPassage()
    return """
    ${CtsDefinitions.prefixPhrase}
    SELECT ?prevUrn ?prevSeq WHERE {
    <${urn}> cts:next ?prevUrn .
    ?prevUrn cts:hasSequence ?prevSeq .
    }
    """
  }


  
  /** Builds SPARQL query string to retrieve a 
   * strcutured description for a version-level CTS URN.
   */
  static String getVersionDescrQuery(CtsUrn psgUrn) {
    def vers = "${psgUrn.getUrnWithoutPassage()}"
    return """
        ${CtsDefinitions.prefixPhrase}

        SELECT ?gname ?title ?lab WHERE {
          <${vers}> dcterms:title ?lab .
          <${vers}> cts:belongsTo ?wk .
          ?wk dcterms:title ?title .
          ?wk cts:belongsTo ?tg .
          ?tg dcterms:title ?gname .
        }
      """
  }


  /** Builds SPARQL query string to determine if a 
   * CTS URN refers to a leaf citation node.
   * @param urn The urn to test.
   * @returns A complete SPARQL query string.
   */
  static String getIsLeafQuery(CtsUrn urn) {
    return """
    ${CtsDefinitions.prefixPhrase}
        ASK
         WHERE {
           <${urn}> cts:hasTextContent ?txt .
         }
    """
  }


  /** Builds SPARQL query string to retrieve
   * version mappings for a work-level CTS URN.
   * @param urn The work-level urn to find versions for.
   * @returns A complete SPARQL query string.
   */
  static String getVersionQuery(CtsUrn workLevelUrn) {
    return """
    ${CtsDefinitions.prefixPhrase}
       SELECT   ?vers ?wk ?type
        WHERE {
        ?vers cts:belongsTo  <${workLevelUrn.getUrnWithoutPassage()}>  .
	?vers rdf:type ?type .
	BIND (<${workLevelUrn.getUrnWithoutPassage()}> as ?wk)
        }
    """
  }

  
  /** Builds SPARQL query string to retrieve
   * text content of a leaf citation node.
   * @param urn The urn of the passage to retrieve.
   * @returns A complete SPARQL query string.
   */
  static String getLeafNodeTextQuery(CtsUrn urn) {
    return getLeafNodeQuery(urn, 0)
  }


  /** Builds SPARQL query string to retrieve
   * text content of a leaf citation node witn n units
   * of context.
   * @param urn The urn of the passage to retrieve.
   * @context Number of units of context to include.
   * @returns A complete SPARQL query string.
   */
  static String getLeafNodeTextQuery(CtsUrn urn, Integer context) {
    return """
    ${CtsDefinitions.prefixPhrase}

    SELECT ?psg ?txt ?anc ?xpt ?xmlns WHERE {
    ?psg cts:belongsTo <${urn.getUrnWithoutPassage()}> .
    ?psg cts:hasTextContent ?txt .
    ?psg cts:hasSequence ?s .
    ?psg hmt:xpTemplate ?xpt .
    ?psg hmt:xmlOpen ?anc  .
    ?psg hmt:xmlNsDecl ?xmlns  .

    {
      { SELECT (xsd:int(?seq) + ${context} AS ?max)
       WHERE {
       <${urn}> cts:hasSequence ?seq .
       }
      }
  
     { SELECT (xsd:int(?seq) - ${context} AS ?min)
       WHERE {
       <${urn}> cts:hasSequence ?seq .
       }
     }
   }
   FILTER (?s <= ?max) .
   FILTER (?s >= ?min) .
  }
  ORDER BY ?s 
  """        
  }   


  /** Builds SPARQL query string to retrieve
   * the sequence number for a leaf-node URN
   * based on document order
   * @param urn The urn of the passage whose sequence we want to find.
   * @returns A complete SPARQL query string.
   */
static String getSeqQuery(CtsUrn urn) {
return """
${CtsDefinitions.prefixPhrase}
SELECT ?urn ?seq
WHERE  {
	   <${urn}> cts:hasSequence ?seq .       
	   BIND( <${urn}> as ?urn )
	 }
"""
}

/** Builds SPARQL query string to find the URN and
* sequence number of the first citable node contained in
* a given URN.  If the URN is a leaf node, returns the
* the query to identify URN and sequence of that node.
* @param urn The urn to test.
* @returns A complete SPARQL query string.
*/
static String getFirstContainedQuery(CtsUrn containingUrn) {
  return """
  ${CtsDefinitions.prefixPhrase}
  SELECT   ?urn ?seq
	 WHERE {
		?urn  cts:containedBy*  <${containingUrn}>  .
		?urn cts:hasSequence ?seq .        
	 }
  ORDER BY ?seq
  LIMIT 1
  """
 }

/** Builds SPARQL query string to find the URN and
* sequence number of the last citable node contained in
* a given URN.  If the URN is a leaf node, returns the
* the query to identify URN and sequence of that node.
* @param urn The urn to test.
* @returns A complete SPARQL query string.
*/
static String getLastContainedQuery(CtsUrn containingUrn) {

   return """
	${CtsDefinitions.prefixPhrase}
	SELECT   ?urn ?seq
	 WHERE {
		?urn  cts:containedBy*  <${containingUrn}>  .
		?urn cts:hasSequence ?seq .        
	 }
	ORDER BY DESC(?seq)
	LIMIT 1
   """
  }

/* Forms SPARQL query to find all URNs 
*  between two leaf-nodes, identified by their sequence numbers
*  (@startCount, @endCount). Requires a version-level URN. 
*/
static String getRangeUrnsQuery(Integer startCount, Integer endCount, String versionUrn) {
return """
${CtsDefinitions.prefixPhrase}
SELECT distinct ?ref 
WHERE {
?u cts:belongsTo <${versionUrn}> .
?u cts:containedBy* ?ref .
?u cts:hasSequence ?s .
?ref cts:citationDepth ?d .
?ref cts:hasTextContent ?t .

FILTER (?s >= "${startCount}"^^xsd:integer) .    
FILTER (?s <= "${endCount}"^^xsd:integer) .
}
ORDER BY ?s
"""
}

/* Forms SPARQL query to find all URNs, and their accompanying text 
*  between two leaf-nodes, identified by their sequence numbers
*  (@startCount, @endCount). Requires a version-level URN. 
*/
static String getRangeNodesQuery(Integer startCount, Integer endCount, String versionUrn) {
return """
${CtsDefinitions.prefixPhrase}
SELECT ?ref ?t 
WHERE {
?u cts:belongsTo <${versionUrn}> .
?u cts:containedBy* ?ref .
?u cts:hasSequence ?s .
?ref cts:citationDepth ?d .
?ref cts:hasTextContent ?t .

FILTER (?s >= "${startCount}"^^xsd:integer) .    
FILTER (?s <= "${endCount}"^^xsd:integer) .
}
ORDER BY ?s
"""
}


}
