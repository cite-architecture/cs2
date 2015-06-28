package edu.holycross.shot.sparqlcc

import edu.harvard.chs.cite.CiteUrn

/** Class representing a CITE Property
 */
class CiteProperty {

  enum PropertyType {
    CITE_URN, CTS_URN, STRING, NUMBER, BOOLEAN, MARKDOWN 
  }
  
  
  String label


  CiteProperty()
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