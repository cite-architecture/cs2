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

    CtsUrn prev = getPrevUrn(leafNode)
    CtsUrn nxt = getNextUrn(leafNode)
    Ohco2Node ond = new Ohco2Node(leafNode, label, prev, nxt, txtContent)
    if (ond == null) {
      System.err.println "COULD NOT MAKE Ohco2Node!"
      System.err.println "${leafNode} (${label}): ${txtContent}"
    } else {
      System.err.println "Made node " + ond.toString()
    }
    return  ond
  }



  /** Finds the previous URN preceding a given URN.
   * If the URN is a leaf node, returns the preceeding leaf node.
   * If the URN is a non-leaf node, returns the URN of the preceeding
   * citation at that level of the hierarchy ?
   * @param urn CTS URN to test.
   * @returns The urn of the preceding citable node, as a String,
   * or a null String ("") if there is no preceding citable node.
   * @throws Exception if retrieved value is not a valid CtsUrn string.
   */
  String getPrevUrnString(CtsUrn urn)
  throws Exception {
    CtsUrn prev = getPrevUrn(urn)
    if (prev != null) {
      return prev.toString()
    } else {
      return ""
    }
  }

  CtsUrn getPrevUrn(CtsUrn requestUrn)
  throws Exception {
    CtsUrn queryUrn = null
    CtsUrn replyUrn = null
    if (requestUrn.isRange()) {
      // find first node in range and query on that
        //urn = new CtsUrn("${requestUrn.getUrnWithoutPassage()}${requestUrn.getRangeBegin()}")

      
    } else {
      queryUrn  = requestUrn
    }

    if (isLeafNode(queryUrn)){
      StringBuilder reply = new StringBuilder()
      String ctsReply =  sparql.getSparqlReply("application/json", QueryBuilder.getPrevUrnQuery(queryUrn))
      def slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(ctsReply)
      parsedReply.results.bindings.each { bndng ->
	if (bndng.prevUrn) {

	  replyUrn = new CtsUrn(bndng.prevUrn?.value)
	  println "CTS GRPH PREV: " + bndng.prevUrn?.value + " ylds urn " + replyUrn
	}
      }


    } else {
      // find first node in contain, and query on that.
	/*
	Integer depthUrn = urn.getCitationDepth()
	Integer firstSequenceOfUrn = getFirstSequence(urn)
	String firstSeqUrn = getUrnForSequence(firstSequenceOfUrn, urn.getUrnWithoutPassage())
	String prevLeafUrnStr= getPrevUrn(new CtsUrn(firstSeqUrn))	
	if (prevLeafUrnStr != ""){
	  CtsUrn prevLeafUrn = new CtsUrn(prevLeafUrnStr)
	  //Janky temp fix which will become irrelevant with update to CITE library
	  CtsUrn prevUrn = new CtsUrn("${prevLeafUrn.trimPassage(depthUrn).replaceAll('::',':')}")
	  replyString = prevUrn.toString()
	} else { replyString = "" }
	*/
	
      }	
    return replyUrn
  }





  String getNextUrnString(CtsUrn requestUrn) {
    CtsUrn nxt = getNextUrn(requestUrn)
    if (nxt != null) {
      return nxt.toString()
    } else {
      return ""
    }
  }
  
  CtsUrn getNextUrn(CtsUrn requestUrn)
  throws Exception {
    CtsUrn replyUrn = null
    CtsUrn queryUrn = null
    if (requestUrn.isRange()) {
      // find first node in range and query on that
        //urn = new CtsUrn("${requestUrn.getUrnWithoutPassage()}${requestUrn.getRangeBegin()}")

      
    } else {
      queryUrn  = requestUrn
    }

    if (isLeafNode(queryUrn)){
      StringBuilder reply = new StringBuilder()
      String ctsReply =  sparql.getSparqlReply("application/json", QueryBuilder.getNextUrnQuery(queryUrn))
      def slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(ctsReply)
      parsedReply.results.bindings.each { bndng ->
	replyUrn = new CtsUrn(bndng.prevUrn?.value)
      }


    } else {
      // find first node in contain, and query on that.
	/*
	Integer depthUrn = urn.getCitationDepth()
	Integer firstSequenceOfUrn = getFirstSequence(urn)
	String firstSeqUrn = getUrnForSequence(firstSequenceOfUrn, urn.getUrnWithoutPassage())
	String prevLeafUrnStr= getPrevUrn(new CtsUrn(firstSeqUrn))	
	if (prevLeafUrnStr != ""){
	  CtsUrn prevLeafUrn = new CtsUrn(prevLeafUrnStr)
	  //Janky temp fix which will become irrelevant with update to CITE library
	  CtsUrn prevUrn = new CtsUrn("${prevLeafUrn.trimPassage(depthUrn).replaceAll('::',':')}")
	  replyString = prevUrn.toString()
	} else { replyString = "" }
	*/
	
      }	
      //      return replyString.replaceAll("::",":")
    return urn
  }



  

  // Applicable only to leaf node...
  // useful in constructing leafnode object
  String getLeafNodeText(CtsUrn urnSubmitted) {

    CtsUrn urn = resolveVersion(urnSubmitted)
    String q = QueryBuilder.getLeafNodeTextQuery(urn, 0)
    String reply = sparql.getSparqlReply("application/json", q)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)

    String txtNode = ""
    parsedReply.results.bindings.eachWithIndex { bndng, i ->
      txtNode =  XmlFormatter.openAncestors(bndng.anc.value) + bndng.txt.value + XmlFormatter.closeAncestors(bndng.anc.value)
    }
    return txtNode
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

  // calling programs need to be careful.
  // no rdf:labels on passages on notioanl works, for example.
  String getLabel(CtsUrn urn)
  throws Exception {
    String labelQuery = QueryBuilder.getRdfLabel(urn)
    String reply = sparql.getSparqlReply("application/json", labelQuery)
    def slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
    def bndng = parsedReply.results.bindings[0]
    if (bndng) {
      return "${bndng.label?.value}"
    } else {
      System.err.println "Failed on query:\n ${labelQuery}\n"
      throw new Exception("CtsGraph:getLabel: no results from query on ${urn}.")
    }
  }
  
}