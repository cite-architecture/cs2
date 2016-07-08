package edu.holycross.shot.sparqlcts

import groovy.json.*
import edu.harvard.chs.cite.CtsUrn

/** Class representing a leaf node in the OHCO2 model of text.
 */
class Ohco2Node {


  /** Machine-actionable URN for the node. */
  CtsUrn nodeUrn
  /** Human-readable label for the node*/
  String nodeLabel

  /** An Ohco2Node must be either at the verion- or exemplar-level **/
  String workLevel

  /** An Ohco2Node must be either an Edition or a Translation, or an exampler from one or the other **/
  String versionType

  /** An Ohco2Node must have a language **/
  String nodeLang

  /** Human-readable label for bibliographic components **/
  String groupLabel
  String workLabel
  String versionLabel
  String exemplarLabel

  
  /** Possibly null URN for preceding leaf node. */
  CtsUrn prevUrn = null
  /** Possibly null URN for following leaf node. */
  CtsUrn nextUrn = null
  /** Ordered list of RangeNode objects. */
  /** N.b. a RangeNode is a map, consisting of 'rangeNode' and 'typeExtras' **/
  ArrayList leafNodes
  
  
  /** Constructor for an OHCO2 citable node requiring all member properties. 
   * @param urn Urn for the node.
   * @param label Human-readable label.
   * @param prev URN for preceding node, or null if no preceding node.
   * @param next URN for following node, or null if no following node.
   * @param rangeNodeMap Non-null arraylist of leafNode objects.
   * @throws Exception if urn, label or txt is empty; or if a non-null
   * value for prev or next is not a valid URN.
   */
  Ohco2Node(CtsUrn urn, String label, String nodeLang, CtsUrn prev, CtsUrn next, ArrayList rangeNodeMap)
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

    this.nodeLang = nodeLang 
    
    if ((rangeNodeMap == null) || (rangeNodeMap.size() < 1)) {
      throw new Exception("Ocho2Node: text content of node cannot be null.")
    } else {
      this.leafNodes = rangeNodeMap
    }
    
    this.prevUrn = prev
    this.nextUrn = next

  }


  /**
   * Overrides default toString() method.
   * @returns Description, URN and text content of this node.
   */
  String toString() {
	String tempString = "" 
	leafNodes.each{ 
		tempString += "${it}\r"
	}

    return "${nodeLabel} (${nodeUrn}): ${tempString}"
  }


	/**
	* Outputs a pretty JSON representation of the Ohco2Node
	* @returns String.
	*/
  String toJson() {
		return new JsonBuilder(this).toPrettyString()
  }

  String toXml() {
    return "<cts:node xmlns:cts='http://chs.harvard.edu/xmlns/cts' urn='" + this.nodeUrn +  "'>${this.textContent}</cts:node>"
  }

 
}
