package edu.holycross.shot.graph

import edu.harvard.chs.cite.CiteUrn

/** Class representing a triple in a directed
 * graph as subject-verb-object (i.e, vertex1->edge->vertext2)
 */
class Triple {

  /** Subject of the triple. */
  URI subj
  /** Verb of the triple. */
  URI verb

  /** Object of the triple if it is a URI.*/
  URI objUri
  /** Object of the triple if it is raw data. */
  String objString
  
  

  Triple()
  throws Exception {

  }


  /**
   * Overrides default toString() method.
   * @returns Description, URN and text content of this node.
   */
  String toString() {
    return ""
  }


 
}