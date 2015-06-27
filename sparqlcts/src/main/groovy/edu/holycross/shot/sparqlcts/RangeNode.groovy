package edu.holycross.shot.sparqlcts

import edu.harvard.chs.cite.CtsUrn

/** Class representing a single node in the ordered
 * list of nodes in a range.
 */
class RangeNode {


  /** Machine-actionable URN for the node. */
  CtsUrn nodeUrn
  /** Text content of the node. */
  String textContent
  
  // ?? for internal stuff ??
  String xmlOpen

  
  
  /** Constructor requiring all member properties. 
   */
  RangeNode(CtsUrn urn, String txt)
  throws Exception {
    if (urn == null) {
      throw new Exception("RangeNode: URN for node cannot be null.")
    } else {
      this.nodeUrn = urn
    }
    if ((txt == null) || (txt.size() < 1)) {
      throw new Exception("RangeNode: text content of node cannot be null.")
    } else {
      this.textContent = txt
    }
  }


 
}