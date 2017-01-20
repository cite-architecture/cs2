package edu.holycross.shot.graph

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import edu.holycross.shot.sparqlcts.Sparql



/** A class representing a CITE Graph Service.
 * It uses a CtsGraph to collect information about CITE URNs
 * in response to CITE Collection Service requests, and then creates appropriate
 * CITE Collection Service replies.
 */
class GraphService {

  /** SPARQL endpoint object from citeservlet common. */
  Sparql sparql

  IndexGraph graph

  /** Constructor requires a citeservlet SPARQL endpoint.
   */
  GraphService(Sparql sparqlService) {
    this.sparql = sparqlService
    this.graph = new IndexGraph(this.sparql)
  }
}
