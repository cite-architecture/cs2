package edu.holycross.shot.sparqlcc

import edu.harvard.chs.cite.CiteUrn

abstract class QueryBuilder {


    static String prefixPhrase  = """
PREFIX cts:        <http://www.homermultitext.org/cts/rdf/>
PREFIX cite:        <http://www.homermultitext.org/cite/rdf/>
PREFIX hmt:        <http://www.homermultitext.org/hmt/rdf/>
PREFIX citedata:        <http://www.homermultitext.org/hmt/citedata/>
PREFIX dcterms: <http://purl.org/dc/terms/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX olo:     <http://purl.org/ontology/olo/core#>
PREFIX lex:        <http://data.perseus.org/rdfverbs/>
PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>
PREFIX owl:        <http://www.w3.org/2002/07/owl#>
PREFIX dse:        <http://www.homermultitext.org/dse/rdf>
PREFIX orca: <http://www.homermultitext.org/orca/rdf/>

"""

  static String getExampleQuery(CiteUrn urn) {
    return "Query about ${urn}"
  }

  /** Generates a Sparql query for versions of an object
  * @param CiteUrn
  * @returns String
  */
  static String resolveVersionQuery(CiteUrn urn){
	String queryString = prefixPhrase
	queryString += """
select ?v where {

<${urn.toString()}> cite:hasVersion ?v

}
"""
	return queryString
  }

  /** Generates a Sparql query for the presence of a cite:orderedBy statement
  * @param CiteUrn
  * @returns String
  */
  static String isOrderedQuery(CiteUrn urn){
	String queryString = prefixPhrase
	queryString += """
ask {
  <${urn.toString()}> cite:orderedBy ?z .
}
"""
	return queryString
  }

  /** Generates a Sparql query for finding the previous object
  * in an ordered collection
  * @param CiteUrn
  * @returns String
  */
  static String getPreviousQuery(CiteUrn urn){
	String queryString = prefixPhrase
	queryString += """
select ?u where {
     <${urn.toString()}> olo:previous  ?u
}
"""
	return queryString
  }

  /** Generates a Sparql query for finding the next object
  * in an ordered collection
  * @param CiteUrn
  * @returns String
  */
  static String getNextQuery(CiteUrn urn){
	String queryString = prefixPhrase
	queryString += """
select ?u where {
     <${urn.toString()}> olo:next  ?u
}
"""
	return queryString
  }

}
