package edu.holycross.shot.sparqlcc

/** A class representing a SPARQL endpoint.
 */
class Sparql {

  String baseUrl

  String prefix = """ PREFIX cts:        <http://www.homermultitext.org/cts/rdf/>
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
  PREFIX orca: <http://www.homermultitext.org/orca/rdf/> """



  /** Constructor requires base URL for SPARQL endpoint.
   */
  Sparql(String sparqlUrl) {
    this.baseUrl = sparqlUrl
  }


  /** Submits a SPARQL query to the configured endpoint
   * and returns the text of the reply.
   * @param acceptType  Value to use for headers.Accept in
   * http request.  If the value of acceptType is 'application/json'
   * fuseki's additional 'output' parameter is added to the
   * http request string so that the string returned for the
   * the request will be in JSON format.  This separates the
   * concerns of forming SPARQL queries from the decision about
   * how to parse the reply in a given format.
   * @param query Text of SPARQL query to submit.
   * @returns Text content of reply.
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
