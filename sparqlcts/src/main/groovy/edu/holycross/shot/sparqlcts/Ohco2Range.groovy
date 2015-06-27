package edu.holycross.shot.sparqlcts

import edu.harvard.chs.cite.CtsUrn

/** Class representing a range of leaf nodes in the
 * OHCO2 model of text.
 */
class Ohco2Range {


  /** Machine-actionable URN for the node. */
  CtsUrn rangeUrn
  /** Human-readable label for the node. */
  String rangeLabel
  
  /** Possibly null URN for preceding leaf node. */
  CtsUrn prevUrn = null
  /** Possibly null URN for following leaf node. */
  CtsUrn nextUrn = null

  /** Ordered list of RangeNode objects. */
  ArrayList leafNodes
  
  
  /** Constructor requiring all member properties. 
   */
  Ohco2Range(CtsUrn urn, String label, CtsUrn prev, CtsUrn next, ArrayList nodeList)
  throws Exception {
    if (urn == null) {
      throw new Exception("Ohco2Range: URN for node cannot be null.")
    } else {
      this.rangeUrn = urn
    }
    if ((label == null) || (label.size() < 1)) {
      throw new Exception("Ocho2Range: text content of node cannot be null.")
    } else {
      this.rangeLabel = label
    }
    
    if (nodeList.size() < 1) {
      throw new Exception("RangeNode: must contain at least one node.")
    } else {
      leafNodes = nodeList
    }
    this.prevUrn = prev
    this.nextUrn = next
  }
 
}