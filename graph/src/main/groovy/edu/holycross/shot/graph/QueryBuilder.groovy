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
	static String generalQuery(CtsUrn urn) {
		String q = """
			${GraphDefinitions.prefixPhrase}
			select ?s ?v ?o ?label ?seq where {
				BIND ( <${urn}> as ?s ) .
				?s ?v ?o .
				optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?seq . }
			    optional { ?o <http://purl.org/ontology/olo/core#item> ?seq . }
				optional { ?o rdf:label ?label . }
			}
		"""
		return q
	}

	static String generalQuery(CiteUrn urn) {
		String q = """
			${GraphDefinitions.prefixPhrase}
			select ?s ?v ?o ?label ?seq where {
				BIND ( <${urn}> as ?s ) .
				?s ?v ?o .
				optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?seq . }
			    optional { ?o <http://purl.org/ontology/olo/core#item> ?seq . }
				optional { ?o rdf:label ?label . }
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
	SELECT ?s ?v ?o ?label ?obSeq WHERE {  
		{
			bind ( <${urn}> as ?s ) .
			?s ?v ?o .
			optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?obSeq . }
			optional { ?o <http://purl.org/ontology/olo/core#item> ?obSeq . }

		} union {
			<${urn}> cts:hasSubstring ?substr .
			bind (?substr as ?s) .
			?s ?v ?o .
			optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?obSeq . }
			optional { ?o <http://purl.org/ontology/olo/core#item> ?obSeq . }
			optional { ?o rdf:label ?label . }
			optional { 
				?o cite:isExtendedRef ?citeRef .
				?citeRef rdf:label ?label .
			}
			optional { 
				?o cts:isSubstringOf ?ctsRef .
				?ctsRef rdf:label ?label .
			}	
		}
	}
	order by ?s  ?obSeq ?v 


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
	  String returnString = """ ${GraphDefinitions.prefixPhrase} 
	  							SELECT ?s ?v ?o ?label ?subSeq ?obSeq where {     """
      returnString += """       { bind (<${urn}> as ?s ) .
	  							?s ?v ?o . 
								optional { ?o rdf:label ?label . } } """
	  urnArray.each { u ->
	 	returnString +=  """ 
			union {  
				bind (<${u}${urn.passageComponent}> as ?s ) . 
				?s ?v ?o . 
				optional { ?s <http://www.homermultitext.org/cts/rdf/hasSequence> ?subSeq . }
				optional { ?s <http://purl.org/ontology/olo/core#item> ?subSeq .}
				optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?obSeq . }
				optional { ?o <http://purl.org/ontology/olo/core#item> ?obSeq .}
				optional { ?o rdf:label ?label . } 
				optional { 
				  ?o cite:isExtendedRef ?citeRef .
				  ?citeRef rdf:label ?label .
				}
				 optional { 
				  ?o cts:isSubstringOf ?ctsRef .
				  ?ctsRef rdf:label ?label .
				}
			} union { 
			    <${u}${urn.passageComponent}> cts:hasSubstring ?substr .  
			    bind (?substr as ?s) .  
			    ?s ?v ?o .  
				optional { ?s <http://www.homermultitext.org/cts/rdf/hasSequence> ?subSeq . }    
				optional { <${u}${urn.passageComponent}> <http://www.homermultitext.org/cts/rdf/hasSequence> ?subSeq .}
				optional { ?s <http://purl.org/ontology/olo/core#item> ?subSeq .}
				optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?obSeq . }
				optional { ?o <http://purl.org/ontology/olo/core#item> ?obSeq .}
			    optional { ?o rdf:label ?label . } 
				optional { 
				  ?o cite:isExtendedRef ?citeRef .
				  ?citeRef rdf:label ?label .
				}
				 optional { 
				  ?o cts:isSubstringOf ?ctsRef .
				  ?ctsRef rdf:label ?label .
				}
			}
			"""
	  }
	  returnString += """} order by ?s ?subSeq ?v ?obSeq ?o"""


		  return returnString
	  }


      
  /** Builds SPARQL query string to find data
   * adjacent to a version-level containing (non-leaf-node) URN 
   * @param urn The URN to test.
   * @returns A complete SPARQL query string.
   */
  static String getQueryVersionLevelContaining(CtsUrn urn){

  String returnString = """ ${GraphDefinitions.prefixPhrase} 
	SELECT ?s ?v ?o ?label ?primarySeq ?secondarySeq  WHERE {
	{
	 bind (<${urn}> as ?s) .
	  ?s ?v ?o .
	  optional { ?o rdf:label ?label . }
		optional { 
		  ?o cite:isExtendedRef ?citeRef .
		  ?citeRef rdf:label ?label .
		}
		 optional { 
		  ?o cts:isSubstringOf ?ctsRef .
		  ?ctsRef rdf:label ?label .
		}
	  optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?primarySeq . }
	  } union {
		bind (<${urn}> as ?container) .
		?container cts:contains ?s .
		?s ?v ?o .
		optional { ?s <http://www.homermultitext.org/cts/rdf/hasSequence> ?primarySeq . }
		optional { ?o rdf:label ?label . }
		optional { 
		  ?o cite:isExtendedRef ?citeRef .
		  ?citeRef rdf:label ?label .
		}
		 optional { 
		  ?o cts:isSubstringOf ?ctsRef .
		  ?ctsRef rdf:label ?label .
		}
		optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?secondarySeq . }
	  }

	  }

	order by ?primarySeq ?s ?o ?secondarySeq
	"""


  }


}
