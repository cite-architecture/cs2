package edu.holycross.shot.graph

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn


abstract class QueryBuilder {

	static String getExampleQuery(CiteUrn urn) {
		return "Query about ${urn}"
	}

  
	/** Constructs SPARQL query to find CITE Image Archives. */


	/** Constructs SPARQL query find the value of an rdf:label statement
	 * for a specified object.
	 * @param URN, as a String, of the object to label.
	 * @returns Text of a SPARQL query.
	 */
	static String labelQuery(String urnStr) {
		String q = """
			select ?s ?o where {
				BIND ( <${urnStr}> as ?s )
					?s  <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> ?o .
			}
		"""
		return q
	}
	
  /** Builds SPARQL query string to determine if a 
   * CTS URN refers to a leaf citation node.
   * @param urn The urn to test.
   * @returns A complete SPARQL query string.
   */
  static String getIsLeafQuery(CtsUrn urn) {
    return """
    ${GraphDefinitions.prefixPhrase}
        ASK
         WHERE {
           <${urn}> cts:hasTextContent ?txt .
         }
    """
  }

}
