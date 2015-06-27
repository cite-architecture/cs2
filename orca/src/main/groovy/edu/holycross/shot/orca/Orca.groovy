package edu.holycross.shot.orca

import edu.holycross.shot.citeservlet.Sparql

//import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn



/** A class representing an ORCA service
 * It uses a CtsGraph to collect information about CTS and CITE URNs
 * in response to ORCA requests, and then creates appropriate 
 * ORCA replies.
 */
class Orca {

  /** SPARQL endpoint object from citeservlet common. */
  Sparql sparql

  CtsGraph graph
  
  /** Constructor requires a citeservlet SPARQL endpoint.
   */
  Orca(Sparql sparqlService) {
    this.sparql = sparqlService
    this.graph = new CtsGraph(this.sparql)
  }

  
}