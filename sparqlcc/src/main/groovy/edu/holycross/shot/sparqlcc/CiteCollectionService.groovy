package edu.holycross.shot.sparqlcc

import edu.holycross.shot.citeservlet.Sparql

import edu.harvard.chs.cite.CiteUrn
//import edu.harvard.chs.cite.CtsUrn



/** A class representing a CITE Collections Service.
 * It uses a CtsGraph to collect information about CITE URNs
 * in response to CITE Collection Service requests, and then creates appropriate 
 * CITE Collection Service replies.
 */
class CiteCollectionService {

  /** SPARQL endpoint object from citeservlet common. */
  Sparql sparql

  CtsGraph graph
  
  /** Constructor requires a citeservlet SPARQL endpoint.
   */
  CiteCollection(Sparql sparqlService) {
    this.sparql = sparqlService
    this.graph = new CtsGraph(this.sparql)
  }
}