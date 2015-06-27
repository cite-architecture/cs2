package edu.holycross.shot.sparqlcts

import edu.holycross.shot.citeservlet.Sparql

//import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn



/** A class representing a Canonical Text Service.
 * It uses a CtsGraph to collect information about CTS URNs
 * in response to CTS requests, and then creates appropriate 
 * CTS replies.
 */
class Cts {

  /** SPARQL endpoint object from citeservlet common. */
  Sparql sparql

  CtsGraph graph
  
  /** Constructor requires a citeservlet SPARQL endpoint.
   */
  Cts(Sparql sparqlService) {
    this.sparql = sparqlService
    this.graph = new CtsGraph(this.sparql)
  }


  
}