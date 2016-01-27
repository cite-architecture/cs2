package edu.holycross.shot.graph

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn


abstract class QueryBuilder {

	static String getExampleQuery(String urn) {
		return "Query about ${urn}"
	}

  
	/** Constructs SPARQL query to find CITE Image Archives. */


	/** Constructs SPARQL query find the value of an rdf:label statement
	 * for a specified object. 
	 * N.b. the SparQl variable names are significant.
	 * ?ctsSeq captures sequencing information for the CTS hierarchy.
	 * ?objSeq captures olo:item sequencing for CITE ordered collections.
	 * @param URN, as a String, of the object to label.
	 * @returns Text of a SPARQL query.
	 */
	static String generalQuery(String urn) {
		String q = """
			${GraphDefinitions.prefixPhrase}
			select ?s ?v ?o ?label ?ctsSeq ?objSeq where {
				BIND ( <${urn}> as ?s ) .
				?s ?v ?o .
				optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?ctsSeq . }
			    optional { ?o <http://purl.org/ontology/olo/core#item> ?objSeq . }
				optional { ?o rdf:label ?label . }
			}
		"""
		return q
	}


/** Builds SPARQL query string to find urns and data adjacent
* to a textGroup-lecvel CTS URN. Returns anything directly
* indexed to the URN, as well as work, all versions, and all
* derived exemplars (as version- or exemplar- level URNs), with 
* types and labels.
* @param urn The urn to test.
* @returns a completre SPARQL query string.
*/
static String getTextGroupAdjacentQuery(String urn){

    return """${GraphDefinitions.prefixPhrase}
SELECT ?s ?v ?o
WHERE {
	{
		bind(<${urn}> as ?s)
			?s ?v ?o  .

	} union {
		bind(<${urn}> as ?parent)

			?parent cts:possesses ?s .
			bind (rdf:type as ?v )
			?s ?v ?o  .
	} union {
		bind(<${urn}> as ?parent)

			?parent cts:possesses ?s .
			bind (rdf:label as ?v )
			?s ?v ?o  .    

	} union {
		bind(<${urn}> as ?s)
			bind(cts:possesses as ?v)
			?s cts:possesses+ ?o .
			?o rdf:type cts:Edition .
	} union {
		bind(<${urn}> as ?s)
			?s cts:possesses+ ?o .
			?o rdf:type cts:Edition .
			bind (cts:possesses as ?v )
			?s ?v ?o .
	} union {
		bind(<${urn}> as ?parent)
			?parent cts:possesses+ ?s .
			?s rdf:type cts:Edition .
			bind (rdf:type as ?v)
			?s ?v ?o .
	} union {
		bind(<${urn}> as ?parent)
			?parent cts:possesses+ ?s .
			?s rdf:type cts:Edition .
			bind (rdf:label as ?v)
			?s ?v ?o .
	} union {
		bind(<${urn}> as ?parent)
			?parent cts:possesses+ ?o .
			?o rdf:type cts:Translation .
			bind (cts:possesses as ?v )
			?s ?v ?o .
	} union {
		bind(<${urn}> as ?parent)
			?parent cts:possesses+ ?s .
			?s rdf:type cts:Translation .
			bind (rdf:type as ?v)
			?s ?v ?o .
	} union {
		bind(<${urn}> as ?parent)
			?parent cts:possesses+ ?s .
			?s rdf:type cts:Translation .
			bind (rdf:label as ?v)
			?s ?v ?o .
	} union {
		bind(<${urn}> as ?parent)
			?parent cts:possesses+ ?o .
			?o rdf:type cts:Exemplar .
			bind (cts:possesses as ?v )
			?s ?v ?o .
	} union {
		bind(<${urn}> as ?parent)
			?parent cts:possesses+ ?s .
			?s rdf:type cts:Exemplar .
			bind (rdf:type as ?v)
			?s ?v ?o .
	} union {
		bind(<${urn}> as ?parent)
			?parent cts:possesses+ ?s .
			?s rdf:type cts:Exemplar .
			bind (rdf:label as ?v)
			?s ?v ?o .
	}
}
	"""
}
	
  /** Builds SPARQL query string to find urns and data adjacent
   * to a  version-level CTS URN leaf citation node. Also includes
   * any versions of the parameter URN with sub-references.
	 * N.b. the SparQl variable names are significant.
	 * ?ctsSeq captures sequencing information for the CTS hierarchy.
	 * ?objSeq captures olo:item sequencing for CITE ordered collections.
   * @param urn The urn to test.
   * @returns A complete SPARQL query string.
   */
  static String getSingleLeafNodeQuery(String urn) {
    return """
    ${GraphDefinitions.prefixPhrase}
	SELECT ?s ?v ?o ?label ?ctsSeq ?objSeq WHERE {  
		{
			bind ( <${urn}> as ?s ) .
			?s ?v ?o .
			optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?ctsSeq . }
			optional { ?o <http://purl.org/ontology/olo/core#item> ?objSeq . }

		} union {
			<${urn}> cts:hasSubstring ?substr .
			bind (?substr as ?s) .
			?s ?v ?o .
			optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?ctsSeq . }
			optional { ?o <http://purl.org/ontology/olo/core#item> ?objSeq . }
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
	order by ?s ctsSeq ?objSeq ?v 


    """
  }

  /** Builds SPARQL query string to find version-level
   * urns, given a work-level urn.
   * @param urn The urn to test.
   * @returns A complete SPARQL query string.
   */
  static String getVersionsForWork(String urn) {
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
	 * N.b. the SparQl variable names are significant.
	 * ?ctsSeq captures sequencing information for the CTS hierarchy.
	 * ?objSeq captures olo:item sequencing for CITE ordered collections.
   * @param urn The work-level URN to test.
   * @param urnArray An ArrayList of version-level URNs
   * @returns A complete SPARQL query string.
   */
  static String getQueryNotionalCitation(String urn, ArrayList urnArray){
	  String returnString = """ ${GraphDefinitions.prefixPhrase} 
	  							SELECT ?s ?v ?o ?label ?ctsSeq ?objSeq where {     """
      returnString += """       { bind (<${urn}> as ?s ) .
	  							?s ?v ?o . 
								optional { ?o rdf:label ?label . } } """
	  urnArray.each { u ->
	 	returnString +=  """ 
			union {  
				bind (<${u}${urn.passageComponent}> as ?s ) . 
				?s ?v ?o . 
				optional { ?s <http://www.homermultitext.org/cts/rdf/hasSequence> ?ctsSeq . }
				optional { ?s <http://purl.org/ontology/olo/core#item> ?objSeq .}
				optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?ctsSeq . }
				optional { ?o <http://purl.org/ontology/olo/core#item> ?objSeq .}
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
				optional { ?s <http://www.homermultitext.org/cts/rdf/hasSequence> ?ctsSeq . }    
				optional { <${u}${urn.passageComponent}> <http://www.homermultitext.org/cts/rdf/hasSequence> ?ctsSeq .}
				optional { ?s <http://purl.org/ontology/olo/core#item> ?objSeq .}
				optional { ?o <http://www.homermultitext.org/cts/rdf/hasSequence> ?ctsSeq . }
				optional { ?o <http://purl.org/ontology/olo/core#item> ?objSeq .}
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
	  returnString += """} order by ?s ?ctsSeq ?v ?objSeq ?o"""


		  return returnString
	  }


      
  /** Builds SPARQL query string to find data
   * adjacent to a version-level containing (non-leaf-node) URN 
	 * N.b. the SparQl variable names are significant.
	 * ?ctsSeq captures sequencing information for the CTS hierarchy.
	 * ?objSeq captures olo:item sequencing for CITE ordered collections.
   * @param urn The URN to test.
   * @returns A complete SPARQL query string.
   */
  static String getQueryVersionLevelContaining(String urn){

  String returnString = """ ${GraphDefinitions.prefixPhrase} 
	  SELECT ?s ?v ?o ?label ?ctsSeq ?objSeq
	  WHERE {

		  bind (<${urn}> as ?s)
			  ?s ?v ?o .
			  optional { ?o rdf:label ?label . }
		  optional { ?o cts:hasSequence ?ctsSeq . }
		  optional { ?o olo:item ?objSeq . }
	  } 
	"""
	return returnString

  }


  /** Builds SPARQL query string to find all exemplars
   * derived from a version
   * @param urn The URN to test.
   * @returns A complete SPARQL query string.
   */
  static String getQueryExemplarsForVersion(String urn){

	  String returnString = """ ${GraphDefinitions.prefixPhrase} 
		SELECT ?o WHERE {
		bind (<${urn}> as ?s) .
		        ?s orca:exemplifiedBy ?o .
		}

	  """

  }


}
