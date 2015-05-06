package edu.holycross.shot.sparqlcts

import edu.holycross.shot.citeservlet.Sparql

import edu.harvard.chs.cite.CtsUrn
import groovy.json.JsonSlurper

/** A class interacting with a SPARQL endpoint to
 * to resolve SPARQL replies into objects in the abstract data
 * model of OHCO2.
 */
class CtsGraph {

  /** SPARQL endpoint object from citeservlet lib. */
  Sparql sparql


  /** Constructor with required SPARQL endpoint object */  
  CtsGraph(Sparql endPoint) {
    sparql = endPoint
  }
  
  /**  Determines whether or not a CTS URN refers to a leaf
   * citation node by consulting the SPARQL endpoint to see
   * if the node has text content or not.
   * @returns True if urn refers to a leaf citation node.
   */
  boolean isLeafNode(CtsUrn requestUrn) {
    String replyText = ""
    CtsUrn urn = resolveVersion(requestUrn)
    String leafQuery = QueryBuilder.getIsLeafQuery(urn)
    String reply = sparql.getSparqlReply("application/json", leafQuery)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
    replyText = parsedReply.boolean
    return (replyText == "true")
  }

  /**  Constructs a version-level CTS URN for a
   * given URN.  If the request URN already has a version,
   * it is returned unchanged.  If the request URN is at the
   * work level, a version attested in the triple store is chosen,
   * and URN referring to that version for the same passage is returned.
   * @param urn A CTS URN reference to resolve to a version-level CTS URN.
   * @returns A CTS URN at the version level citing the passage in 
   * the request urn.
   */
  CtsUrn resolveVersion(CtsUrn urn) 
  throws Exception {
    String workLevel = urn.labelForWorkLevel()
    if ((workLevel == 'version')|(workLevel == 'exemplar')) {
      return urn

    } else {
      String version = findVersion(urn)
      if (version) {
	String resolvedStr  = "urn:cts:${urn.getCtsNamespace()}:${urn.getTextGroup()}.${urn.getWork()}.${version}:${urn.getPassageComponent()}"
	
	try {
	  return (new CtsUrn(resolvedStr))
	} catch (Exception e) {
	  System.err.println ("CtsGraph:resolveVersion: error: ${e}")
	  throw e
	}
	
      } else {
	throw new Exception("CtsGraph: resolveVersion: no version found for urn ${urn}")
      }
    }
  }



  
  
  /** Determines a valid version value for a CTS URN.
   * If the URN is at the version level, this value is simply the
   * version element of the URN.  If the URN is at the work level,
   * the SPARQL endpoint is consulted and the identifier of 
   * the first version mapped to this work is returned.
   * @param urn CTS URN to find version for.
   * @returns A string with the version identifier component of a
   * version-level URN, without namespace qualifier.
   */
  String findVersion(CtsUrn urn) 
  throws Exception {
    String workLevel = urn.labelForWorkLevel()
    String vers = null
    if ((workLevel == 'version') || (workLevel == 'exemplar')) {
      vers = urn.getVersion(false)
    } else {
      String vQuery = QueryBuilder.getVersionQuery(urn)
      String reply = sparql.getSparqlReply("application/json", vQuery)

      JsonSlurper slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(reply)
      parsedReply.results.bindings.each { bndng ->
	if (bndng.vers) {
	  CtsUrn versionUrn = new CtsUrn(bndng.vers?.value)
	  vers = versionUrn.getVersion(false)
	}
      }
    }
    if (vers == null) {
      throw new Exception("CtsGraph:findVersion: could not determine version for ${urn}")
    } else {
	return vers
    }
  }


  /** Constructs an Ohco2Node object for a node 
   * identified by CtsUrn.
   * @param leafNode CtsUrn of the node.
   * @returns An Ohco2Node object.
   * @throws Exception if leafNode is not a single leaf node.
   */
  Ohco2Node getLeafNodeObject(CtsUrn leafNode)
  throws Exception {
    if (leafNode.isRange()) {
      throw new Exception("CtsGraph:getLeafNodeObject: ${leafNode} is a range reference.")
    }
    if (! isLeafNode(leafNode) ) {
      throw new Exception("CtsGraph:getLeafNodeObject: ${leafNode} is not a leaf node.")
    }

    String label  = getLabel(leafNode)
    String txtContent = getLeafNodeText(leafNode)
    return null
  }


  // Applicable only to leaf node...
  // useful in constructing leafnode object
  String getLeafNodeText(CtsUrn urnSubmitted) {

    CtsUrn urn = resolveVersion(urnSubmitted)
    String q = QueryBuilder.getLeafNodeTextQuery(urn, 0)
    String reply = sparql.getSparqlReply("application/json", q)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)

    
    parsedReply.results.bindings.eachWithIndex { bndng, i ->
      println "Here's results of query for leaf text: ${i}: ${bndng}"
      println "ANCESTOR converts to : "  + XmlFormatter.openAncestors(bndng.anc.value)
      println "TEXT: "  + bndng.txt.value
      println "XPATH TEMPLATE: " + bndng.xpt.value

      
    }
  }


  
  String getNodeText(CtsUrn urn, Integer context, boolean openXml, boolean closeXml) {
  
    String currentWrapper = ""
    
    String q = QueryBuilder.getLeafNodeTextQuery(urn, context)
    String reply = sparql.getSparqlReply("application/json", q)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
    parsedReply.results.bindings.eachWithIndex { bndng, i ->
      println "${i}: ${bndng}"
    }

    return ""
  }


  
  String getLabel(CtsUrn urn)
  throws Exception {
    
    switch(urn.labelForWorkLevel()) {
    case "version":
    String vQuery = QueryBuilder.getVersionDescrQuery(urn)
    String reply = sparql.getSparqlReply("application/json", vQuery)

    def slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)

    def bndng = parsedReply.results.bindings[0]
    if (bndng) {
      return "${bndng.gname?.value},${bndng.title?.value},${bndng.lab?.value}"
    } else {
      System.err.println "Failed on query:\n ${vQuery}\n"
      throw new Exception("CtsGraph:getLabel: no results from query on ${urn}.")
    }
    break

    default:
    System.err.println "LABEL NOT IMPLEMENTED FOR ${urn.labelForWorkLevel()}"
    break
    }
    return ""
  }
  
}