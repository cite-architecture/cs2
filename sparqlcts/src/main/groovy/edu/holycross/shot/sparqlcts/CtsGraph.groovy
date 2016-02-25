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

  /** Finds URNs and text-passages for nodes defined by a CTS URN.
   * If urn is a leaf node, the resulting list will contain one item,
   * a single rangeNode.  If urn is a range, the list will contain rangeNodes
   * for all leaf nodes bounded by the range, as well as the
   * beginning and end nodes identified in the CTS URN. If
   * urn is a single containing node, the list will contains rangeNodes
   * for all leaf-nodes contained by that node. Whenever the list contains more than
   * one leaf-node, they will appear in document order.
   * Returns an ArrayList , consisting of Map ['rangeNode'] is rangeNode, and ['typeExtras'] a typeExtras map, for now
   * containing the XML stuff necessary for reconstructing an XML reply.
   * @param submittedUrn The CtsUrn in question.
   * @returns An ordered list of pairs: [0] is rangeNode, [1] is typeExtras
   */
  ArrayList getRangeNodes(CtsUrn submittedUrn) {
	println "Starting getRangeNodes ${submittedUrn}"

	StringBuffer reply = new StringBuffer()
	String listUrnsQuery = ""
	String ctsReply = ""
	Integer int1
	Integer int2
	Integer startAtStr 
	Integer endAtStr


	CtsUrn urn = resolveVersion(submittedUrn)
	ArrayList responseList = []

	// Three Possibilities: node, container, range
	if (isLeafNode(urn)){
		println "RangeNode ${submittedUrn} is leafnode."
		
			String leafUrnQuery = QueryBuilder.getLeafNodeTextQuery(urn)
            ctsReply =  sparql.getSparqlReply("application/json", leafUrnQuery)
            def slurper = new groovy.json.JsonSlurper()
            def parsedReply = slurper.parseText(ctsReply)
            parsedReply.results.bindings.each { b ->
                if (b.txt) {
					RangeNode rn = new RangeNode(urn, b.txt?.value)
					def typeExtras = [:]
					if (b.anc){
						typeExtras['type'] = "xml"
						typeExtras['anc'] = b.anc?.value
						typeExtras['xpt'] = b.xpt?.value
						typeExtras['nxt'] = b.nxt?.value
						typeExtras['xmlns'] = b.xmlns?.value
						typeExtras['xmlnsabbr'] = b.xmlnsabbr?.value
					} else {
						typeExtras['type'] = "unknown"				
					}
					Map tempMap = [ 'rangeNode':rn, 'typeExtras':typeExtras ]
					responseList.add(tempMap)
                }
            }
	} else {

		if (urn.isRange()){
			println "RangeNode ${submittedUrn} is range."
			CtsUrn urn1 = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeBegin()}")
			CtsUrn urn2 = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeEnd()}")

            if (isLeafNode(urn1)) {
           	     startAtStr =  getSequence(urn1)
			} else {
				startAtStr = getFirstSequence(urn1)
			}
			
			if (isLeafNode(urn2)) {
				endAtStr = getSequence(urn2)
			} else {
				endAtStr = getLastSequence(urn2)
			}
			// error check these…
		    int1 = startAtStr.toInteger()
			int2 = endAtStr.toInteger()

		      
			listUrnsQuery = QueryBuilder.getRangeNodesQuery(int1, int2, "${urn.getUrnWithoutPassage()}")
            ctsReply =  sparql.getSparqlReply("application/json", listUrnsQuery)
            def slurper = new groovy.json.JsonSlurper()
            def parsedReply = slurper.parseText(ctsReply)
            parsedReply.results.bindings.each { b ->
                if (b.ref) {
					RangeNode rn = new RangeNode(new CtsUrn(b.ref?.value),b.t?.value)
					def typeExtras = [:]
					if (b.anc){
						typeExtras['type'] = "xml"
						typeExtras['anc'] = b.anc?.value
						typeExtras['xpt'] = b.xpt?.value
						typeExtras['nxt'] = b.nxt?.value
						typeExtras['xmlns'] = b.xmlns?.value
						typeExtras['xmlnsabbr'] = b.xmlnsabbr?.value
					} else {
						typeExtras['type'] = "unknown"				
					}
					Map tempMap = [ 'rangeNode':rn, 'typeExtras':typeExtras ]
					responseList.add(tempMap)
                }
            }
				
		} else { // must be containing element
			
			println "RangeNode ${submittedUrn} is container."
			startAtStr = getFirstSequence(urn)
			endAtStr = getLastSequence(urn)
			// error check these…
			int1 = startAtStr.toInteger()
			int2 = endAtStr.toInteger()
			listUrnsQuery = QueryBuilder.getRangeNodesQuery(int1, int2, "${urn.getUrnWithoutPassage()}")
			ctsReply =  sparql.getSparqlReply("application/json", listUrnsQuery)
			def slurper = new groovy.json.JsonSlurper()
			def parsedReply = slurper.parseText(ctsReply)
			parsedReply.results.bindings.each { b ->
				if (b.ref) {
					RangeNode rn = new RangeNode(new CtsUrn(b.ref?.value),b.t?.value)
					def typeExtras = [:]
					if (b.anc){
						typeExtras['type'] = "xml"
						typeExtras['anc'] = b.anc?.value
						typeExtras['xpt'] = b.xpt?.value
						typeExtras['nxt'] = b.nxt?.value
						typeExtras['xmlns'] = b.xmlns?.value
						typeExtras['xmlnsabbr'] = b.xmlnsabbr?.value
					} else {
						typeExtras['type'] = "unknown"				
					}
					Map tempMap = [ 'rangeNode':rn, 'typeExtras':typeExtras ]
					responseList.add(tempMap)
				}
			}

		}
	}

		return responseList
  }


  /** Finds URNs for leaf nodes defined by a CTS URN.
   * If urn is a leaf node, the resulting list will contain one item,
   * urn.  If urn is a range, the list will comprise all URNs
   * for all leaf nodes bounded by the range, as well as the
   * beginning and end nodes identified in the CTS URN. If
   * urn is a single containing node, the list will comprise all
   * URNs contained by that node. Whenever the list contains more than
   * one URN, they will appear in document order.
   * @param urn The CtsUrn in question.
   * @returns An ordered list of CtsUrns.
   */
  ArrayList getUrnList(CtsUrn submittedUrn) {
	StringBuffer reply = new StringBuffer()
	String listUrnsQuery = ""
	String ctsReply = ""
	Integer int1
	Integer int2
	Integer startAtStr 
	Integer endAtStr

	
	//println "getUrnList ${submittedUrn}"

	CtsUrn urn = resolveVersion(new CtsUrn (submittedUrn.reduceToNode()))
	//println "resolved version: ${urn}"
    ArrayList urns = []

	// Three Possibilities: node, container, range
	if (isLeafNode(urn)){
		urns.add(urn)	
	} else {

		if (urn.isRange()){
			CtsUrn urn1 = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeBegin()}")
			//println "urn1: ${urn1}"
			CtsUrn urn2 = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeEnd()}")
			//println "urn2: ${urn1}"

            if (isLeafNode(urn1)) {
           	     startAtStr =  getSequence(urn1)
			} else {
				startAtStr = getFirstSequence(urn1)
			}
			 //println "startAtStr = ${startAtStr}"
			
			if (isLeafNode(urn2)) {
				endAtStr = getSequence(urn2)
			} else {
				endAtStr = getLastSequence(urn2)
			}
			//println "endAtStr = ${endAtStr}"

			// error check these…
		    int1 = startAtStr.toInteger()
			int2 = endAtStr.toInteger()

		      
			listUrnsQuery = QueryBuilder.getRangeUrnsQuery(int1, int2, "${urn.getUrnWithoutPassage()}")
            ctsReply =  sparql.getSparqlReply("application/json", listUrnsQuery)
            def slurper = new groovy.json.JsonSlurper()
            def parsedReply = slurper.parseText(ctsReply)
            parsedReply.results.bindings.each { b ->
                if (b.ref) {
                    urns.add(new CtsUrn(b.ref?.value))
                }
            }
				
			

			
		} else { // must be containing element
			
			startAtStr = getFirstSequence(urn)
			endAtStr = getLastSequence(urn)
			// error check these…
			int1 = startAtStr.toInteger()
			int2 = endAtStr.toInteger()
			listUrnsQuery = QueryBuilder.getRangeUrnsQuery(int1, int2, "${urn.getUrnWithoutPassage()}")
			ctsReply =  sparql.getSparqlReply("application/json", listUrnsQuery)
			def slurper = new groovy.json.JsonSlurper()
			def parsedReply = slurper.parseText(ctsReply)
			parsedReply.results.bindings.each { b ->
				if (b.ref) {
					urns.add(new CtsUrn(b.ref?.value))
				}
			}


		}
	}
	
    return urns
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


  /** Constructs an Ohco2Node object for a passage
   * identified by CtsUrn.
   * @param leafNode CtsUrn of the node.
   * @returns An Ohco2Node object.
   * @throws Exception if leafNode is not a single leaf node.
   */
  Ohco2Node getOhco2Node(CtsUrn urn) {

    String label  = getLabel(urn)
    ArrayList leafNodes = getRangeNodes(urn)

    CtsUrn prev = getPrevUrn(urn)
    CtsUrn nxt = getNextUrn(urn)
    Ohco2Node ond = new Ohco2Node(urn, label, prev, nxt, leafNodes)
    if (ond == null) {
      System.err.println "COULD NOT MAKE Ohco2Node!"
      System.err.println "${leafNode} (${label}): ${txtContent}"
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
	CtsUrn rurn = resolveVersion(urn)
    CtsUrn prev = getPrevUrn(rurn)
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
      queryUrn  = resolveVersion(requestUrn)
    }

    if (isLeafNode(queryUrn)){
      StringBuilder reply = new StringBuilder()
      String ctsReply =  sparql.getSparqlReply("application/json", QueryBuilder.getPrevUrnQuery(queryUrn))
      def slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(ctsReply)
      parsedReply.results.bindings.each { bndng ->
	if (bndng.prevUrn) {

	  replyUrn = new CtsUrn(bndng.prevUrn?.value)
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
	CtsUrn rurn = resolveVersion(requestUrn)
    CtsUrn nxt = getNextUrn(rurn)
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
      queryUrn  = resolveVersion(requestUrn)
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
      // implement
    }	

    return replyUrn
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
      //txtNode =  XmlFormatter.openAncestors(bndng.anc.value, bndng.xmlns.value) + bndng.txt.value + XmlFormatter.closeAncestors(bndng.anc.value)
	  txtNode = bndng.txt.value
    }
    return txtNode
  }

  

  // calling programs need to be careful.
  // no rdf:labels on passages on notioanl works, for example.
  // but shouldn't there be??
  String getLabel(CtsUrn urnSubmitted)
  throws Exception {

		String workLevel = urnSubmitted.getWorkLevel()
		String labelQuery = ""
		if (urnSubmitted.passageComponent){

			CtsUrn urn = resolveVersion(urnSubmitted)
			if (urnSubmitted.isRange()){
				return "range label."
			} else {
				labelQuery = QueryBuilder.getRdfLabel(urn)
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

		} else {
				labelQuery = QueryBuilder.getRdfLabel(urnSubmitted)
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


    /** Finds the first sequence value for a set of URNs contained 
    * by a given URN.
    * @param urn A containing URN.
    * @returns The sequence property of this URN, as an Integer.
    */
    Integer getFirstSequence(CtsUrn urn) {
        Integer firstInt = null
		String firstContainedQuery = QueryBuilder.getFirstContainedQuery(urn)
		//println "${firstContainedQuery}"
        String ctsReply = sparql.getSparqlReply("application/json", firstContainedQuery)
		//println ctsReply
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(ctsReply)
        
        String intStr
        parsedReply.results.bindings.each { b ->
            if (b.seq) {
                intStr = b.seq?.value
            }
            try {
                firstInt = intStr.toInteger()
            } catch (Exception e) {
                System.err.println "Could not parse sequence ${intStr} as Integer: ${e}"
            }

        }
        return firstInt
    }

    /** Finds the last sequence value for a set of URNs contained 
    * by a given URN.
    * @param urn A containing URN.
    * @returns The sequence property of this URN, as an Integer.
    */
    Integer getLastSequence(CtsUrn urn) {
        Integer lastInt = null
		String lastContainedQuery = QueryBuilder.getLastContainedQuery(urn)
        String ctsReply = sparql.getSparqlReply("application/json", lastContainedQuery )
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(ctsReply)
        
        String intStr
        parsedReply.results.bindings.each { b ->
            if (b.seq) {
                intStr = b.seq?.value
            }
            try {
                lastInt = intStr.toInteger()
            } catch (Exception e) {
                System.err.println "Could not parse sequence ${intStr} as Integer: ${e}"
            }
        }
        return lastInt
    }


    /** Finds sequence number for a requested leaf-node CtsUrn.
    * @param urn The requested CtsUrn.
    * @returns The sequence number of the requested URN.
    * @throws Exception if urn is not a leaf-node URN, or if a
    * sequence could not be found in the triple store.
    */
    Integer getSequence(CtsUrn urn) 
    throws Exception {
        if (isLeafNode(urn)) {
            StringBuffer reply = new StringBuffer()
			String seqQuery = QueryBuilder.getSeqQuery(urn)
            String ctsReply =  sparql.getSparqlReply("application/json", seqQuery)
            def slurper = new groovy.json.JsonSlurper()
            def parsedReply = slurper.parseText(ctsReply)
            parsedReply.results.bindings.each { b ->
                if (b.seq) {
                    reply.append(b.seq?.value)
                }
            }
            try {
                return reply.toString().toInteger()
            } catch (Exception e) {
                throw new Exception ("CtsGraph:getSequence: could not find sequence for ${urn}. ${e}")
            }
        } else {
            throw new Exception("CtsGraph:getSequence: ${urn} is not a leaf-node URN.")
        }
    }

  
}
