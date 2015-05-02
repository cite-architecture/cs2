package edu.holycross.shot.sparqlcts

import edu.holycross.shot.citeservlet.Sparql



/** A class representing a Canonical Text Service.
 */
class Cts {

  Sparql sparql

  /** Constructor requires base URL for SPARQL endpoint.
   */
  Cts(Sparql sparqlService) {
    this.sparql = sparqlService

  }
 
}