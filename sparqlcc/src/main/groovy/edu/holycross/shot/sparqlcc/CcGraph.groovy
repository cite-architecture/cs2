package edu.holycross.shot.sparqlcc

import edu.holycross.shot.citeservlet.Sparql

import edu.harvard.chs.cite.CiteUrn
import groovy.json.JsonSlurper

/** A class interacting with a SPARQL endpoint to
 * to resolve SPARQL replies into objects in the abstract data 
 * model of CITE Collection objects.
 */
class CcGraph {

  /** SPARQL endpoint object from citeservlet lib. */
  Sparql sparql

  /** Constructor with required SPARQL endpoint object */  
  CcGraph(Sparql endPoint) {
    sparql = endPoint
  }
  
}
