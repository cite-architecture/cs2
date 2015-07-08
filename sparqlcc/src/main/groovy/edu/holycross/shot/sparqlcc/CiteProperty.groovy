package edu.holycross.shot.sparqlcc

import edu.harvard.chs.cite.CiteUrn

/** Class representing a CITE Property
 */
class CiteProperty {

  enum PropertyType {
    CITE_URN, CTS_URN, STRING, NUMBER, BOOLEAN, MARKDOWN 
  }


  /** Name of the property.*/
  String propertyName

  /** Type of property */
  PropertyType propertyType

  /** Human-readable label for the property */
  String label
    
  /** String representation of the property's value. 
   * Depending on its type, this string will be
   * validated at time of construction of the CiteProperty object.
   */
  String value


  CiteProperty(String pName, PropertyType pType, String pLabel, String pValue)
  throws Exception {
    this.propertyName = pName
    this.propertyType = pType
    this.label = pLabel
    this.value = pValue
    if (! isValid()) {
      String msg =  "CiteProperty: unable to validate value ${value} as type ${propertyType}"
      System.err.println msg
      throw new Exception(msg)
    }
  }


  boolean isValid() {
    switch (this.propertyType) {

    case PropertyType.STRING:
    return true
    break

    case PropertyType.CITE_URN:
    case PropertyType.CTS_URN:
    case PropertyType.NUMBER:
    case PropertyType.BOOLEAN:
    case PropertyType.MARKDOWN:
    default:
    return false
    break
    }
  }

  

  /**
   * Overrides default toString() method.
   * @returns Description, URN and text content of this node.
   */
  String toString() {
    return ""
  }


 
}