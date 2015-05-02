package edu.holycross.shot.citeservlet

/** A class representing a SPARQL endpoint.
 */
class Sparql {

  String baseUrl
  
  String prefix = "prefix cts: <http://www.homermultitext.org/cts/rdf/>\nprefix cite: <http://www.homermultitext.org/cite/rdf/>\nprefix hmt: <http://www.homermultitext.org/hmt/rdf/>\nprefix citedata: <http://www.homermultitext.org/hmt/citedata/>\nprefix dcterms: <http://purl.org/dc/terms/>\nprefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\nprefix  xsd: <http://www.w3.org/2001/XMLSchema#>\nprefix olo: <http://purl.org/ontology/olo/core#>"

  /** Constructor requires base URL for SPARQL endpoint.
   */
  Sparql(String sparqlUrl) {
    this.baseUrl = sparqlUrl
  }


  /** Submits a query to the SPARQL endpoint.
   * @param acceptType 
   * @param query A valid SPARQL query.
   * @returns Text of SPARQL reply
   */
  String getSparqlReply(String acceptType, String query) {
    String replyString
    String encodedQuery = URLEncoder.encode(query)
    String q = "${baseUrl}?query=${encodedQuery}"
    if (acceptType == "application/json") {
        q +="&output=json"
    }
    URL queryUrl = new URL(q)
    return queryUrl.getText("UTF-8")
  }

  
}