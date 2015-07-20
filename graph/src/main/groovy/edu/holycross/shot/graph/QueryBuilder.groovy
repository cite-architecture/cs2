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
	
  /** Builds SPARQL query string to find urns and data adjacent
   * to a  version-level CTS URN leaf citation node. Also includes
   * any versions of the parameter URN with sub-references.
   * @param urn The urn to test.
   * @returns A complete SPARQL query string.
   */
  static String getSingleLeafNodeQuery(CtsUrn urn) {
    return """
    ${GraphDefinitions.prefixPhrase}
	SELECT ?s ?v ?o ?label WHERE {  
		{
			bind ( <${urn}> as ?s ) .
			?s ?v ?o .
		} union {
			<${urn}> cts:hasSubstring ?substr .
			bind (?substr as ?s) .
			?s ?v ?o .
			optional { ?o rdf:label ?label . }
		}
	}
    order by ?s ?v
    """
  }

  /** Builds SPARQL query string to find version-level
   * urns, given a work-level urn.
   * @param urn The urn to test.
   * @returns A complete SPARQL query string.
   */
  static String getVersionsForWork(CtsUrn urn) {
		return """
		${GraphDefinitions.prefixPhrase}
		SELECT ?version WHERE {  

  			bind ( <${urn}> as ?s ) .
			?s cts:possesses ?version .
 
		}
		"""
  }

 
  /** Builds SPARQL query string to find data
   * adjacent to a work-level leaf-node URN (with a citation value)
   * @param urn The work-level URN to test.
   * @param urnArray An ArrayList of version-level URNs
   * @returns A complete SPARQL query string.
   */
  static String getQueryNotionalCitation(CtsUrn urn, ArrayList urnArray){
	  String returnString = """ ${GraphDefinitions.prefixPhrase} SELECT ?s ?v ?o ?label where { """
      returnString += """   { bind (<${urn}> as ?s ) .  ?s ?v ?o . optional { ?o rdf:label ?label . } } """
	  urnArray.each { u ->
			returnString += """union {  bind (<${u}${urn.passageComponent}> as ?s ) . ?s ?v ?o . optional { ?o rdf:label ?label . } } """
	  }
	  returnString += """} order by ?s ?v ?o """


		  return returnString
	  }






}
