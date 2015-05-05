package edu.holycross.shot.sparqlcts

import edu.harvard.chs.cite.CtsUrn

abstract class QueryBuilder {


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

}
