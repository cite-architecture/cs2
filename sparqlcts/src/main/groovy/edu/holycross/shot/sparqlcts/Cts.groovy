package edu.holycross.shot.sparqlcts

import edu.holycross.shot.citeservlet.Sparql

//import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn



/** A class representing a Canonical Text Service.
 */
class Cts {

  /** SPARQL endpoint class from citeservlet common. */
  Sparql sparql

  /** Constructor requires base URL for SPARQL endpoint.
   */
  Cts(Sparql sparqlService) {
    this.sparql = sparqlService
  }
}