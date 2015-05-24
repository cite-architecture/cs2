package edu.holycross.shot.sparqlcts

import edu.holycross.shot.citeservlet.Sparql

//import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn



/** A class representing a Canonical Text Service.
 */
class Cts {

  /** SPARQL endpoint object from citeservlet common. */
  Sparql sparql

  /** Constructor requires a citeservlet SPARQL endpoint.
   */
  Cts(Sparql sparqlService) {
    this.sparql = sparqlService
  }


  
}