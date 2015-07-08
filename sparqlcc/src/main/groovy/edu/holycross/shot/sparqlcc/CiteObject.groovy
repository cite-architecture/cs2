package edu.holycross.shot.sparqlcc

import edu.harvard.chs.cite.CiteUrn

/** Class representing a CITE Object.
 */
class CiteObject {

  // In the future, may wish to expand this to validate propertyList
  // against definition for the Colleciton the object belongs to.
  

  /** URN of the object.*/
  CiteUrn urn
  
  /** List of CiteProperty objects */
  ArrayList propertyList

  
  CiteObject(CiteUrn objUrn, ArrayList objProperties)
  throws Exception {
    this.urn = objUrn
    this.propertyList = objProperties
    if (! isValid()) {
      throw new Exception("CiteObject: list of properties was not valid: ${objProperties}")
    }
  }


  boolean isValid() {
    boolean valid = true
    propertyList.each { p ->
      if (p instanceof CiteProperty) {
      } else {
	valid = false
      }
    }
    return valid
  }

  /**
   * Overrides default toString() method.
   * @returns Description, URN and text content of this node.
   */
  String toString() {
    return ""
  }

  String toXml() {
    return ""
  }

 
}