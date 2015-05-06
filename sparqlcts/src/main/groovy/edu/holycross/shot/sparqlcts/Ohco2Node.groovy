package edu.holycross.shot.sparqlcts

import edu.harvard.chs.cite.CtsUrn

/** Class representing a leaf node in the
 * OHCO2 model of text.
 */
class Ohco2Node {


  /** Machine-actionable URN for the node. */
  CtsUrn nodeUrn
  /** Human-readable label for the node*/
  String nodeLabel
  
  /** Possibly null URN for preceding leaf node. */
  CtsUrn prevUrn = null
  /** Possibly null URN for following leaf node. */
  CtsUrn nextUrn = null
  /** Text content of the node. */
  String textContent

  
  
  /** Constructor requiring all member properties. 
   */
  Ohco2Node(CtsUrn urn, String label, CtsUrn prev, CtsUrn next, String txt)
  throws Exception {
    if (urn == null) {
      throw new Exception("Ohco2Node: URN for node cannot be null.")
    } else {
      this.nodeUrn = urn
    }
    if ((label == null) || (label.size() < 1)) {
      throw new Exception("Ocho2Node: text content of node cannot be null.")
    } else {
      this.nodeLabel = label
    }
    
    if ((txt == null) || (txt.size() < 1)) {
      throw new Exception("Ocho2Node: text content of node cannot be null.")
    } else {
      this.textContent = txt
    }
    this.prevUrn = prev
    this.nextUrn = next
  }


  /**
   * Overrides default toString() method.
   * @returns Description, URN and text content of this node.
   */
  String toString() {
    return "${nodeLabel} (${nodeUrn}): ${textContent}"
  }

 
}