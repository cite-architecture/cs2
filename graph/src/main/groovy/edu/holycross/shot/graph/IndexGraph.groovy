package edu.holycross.shot.graph

import edu.holycross.shot.citeservlet.Sparql

import edu.harvard.chs.cite.CiteUrn
import groovy.json.JsonSlurper

/** A class interacting with a SPARQL endpoint to
 * to resolve SPARQL replies into objects in the abstract data 
 * model of CITE Collection objects.
 */
class IndexGraph {

  /** SPARQL endpoint object from citeservlet lib. */
  Sparql sparql

  /** Constructor with required SPARQL endpoint object */  
  IndexGraph(Sparql endPoint) {
    sparql = endPoint
  }

  /** Find all nodes at one degree of 
   * relation to the object identified by
   * urn.
   * @param urn CITE Object to find in the graph.
   * @returns ArrayList of Triple objects.
   */
  ArrayList findAdjacent(CiteUrn urn) {
  }


  /** Find all nodes at one degree of 
   * relation to the text passage identified by
   * urn.
   * @param urn Text passage to find in the graph.
   * @returns ArrayList of Triple objects.
   */
  ArrayList findAdjacent(CtsUrn urn) {
  }
  
}
