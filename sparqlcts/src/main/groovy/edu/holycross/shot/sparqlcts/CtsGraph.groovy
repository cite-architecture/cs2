package edu.holycross.shot.sparqlcts
import groovy.time.TimeCategory
import groovy.time.TimeDuration


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
 q  * for all leaf-nodes contained by that node. Whenever the list contains more than
   * one leaf-node, they will appear in document order.
   * Returns an ArrayList , consisting of Map ['rangeNode'] is rangeNode, and ['typeExtras'] a typeExtras map, for now
   * containing the XML stuff necessary for reconstructing an XML reply.
   * @param submittedUrn The CtsUrn in question.
   * @returns An ordered list of Map: ['rangeNode'], ['typeExtras']
   */
  ArrayList getRangeNodes(CtsUrn submittedUrn) {

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
						typeExtras['nxt'] = b.nxt?.value
					}
					Map tempMap = [ 'rangeNode':rn, 'typeExtras':typeExtras ]
					responseList.add(tempMap)
                }
            }
	} else {

		if (urn.isRange()){
			CtsUrn urn1 = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeBegin()}")
			CtsUrn urn2 = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeEnd()}")
			System.err.println("\n-------------------------\n")
			System.err.println("${urn1} :: ${urn2}")

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
			System.err.println("${int1} :: ${int2}")


			listUrnsQuery = QueryBuilder.getRangeNodesQuery(int1, int2, "${urn.getUrnWithoutPassage()}")
            ctsReply =  sparql.getSparqlReply("application/json", listUrnsQuery)
            def slurper = new groovy.json.JsonSlurper()
            def parsedReply = slurper.parseText(ctsReply)
						System.err.println(parsedReply.toString())

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
										typeExtras['xmlns'] = ""
										typeExtras['nxt'] = b.nxt?.value
									}
									Map tempMap = [ 'rangeNode':rn, 'typeExtras':typeExtras ]
									System.err.println(tempMap.toString())
									responseList.add(tempMap)
								} else {
								}
							}

		} else { // must be containing element

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

	CtsUrn urn = resolveVersion(new CtsUrn (submittedUrn.reduceToNode()))
    ArrayList urns = []

	// Three Possibilities: node, container, range
	if (isLeafNode(urn)){
		urns.add(urn)
	} else {

		if (urn.isRange()){
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
    String workLevel = urn.getWorkLevel()
    if ((workLevel == 'VERSION')|(workLevel == 'EXEMPLAR')) {
	  if (urn.isRange()){
				String ref1 = urn.getRangeBegin()
				String ref2 = urn.getRangeEnd()
				if (ref1 == ref2){
					  return new CtsUrn("${urn.getUrnWithoutPassage()}${ref1.tokenize("@")[0]}")
				} else {
					  return new CtsUrn("${urn.getUrnWithoutPassage()}${ref1.tokenize("@")[0]}-${ref2.tokenize("@")[0]}")
				}

	  } else {
		  if (urn.hasSubref()){
			  return new CtsUrn("${urn.getUrnWithoutPassage()}${urn.passageComponent.tokenize("@")[0]}")
		  } else {
			  return urn
		  }
	  }

    } else {
      String version = findVersion(urn)
      if (version) {

		String resolvedStr  = "urn:cts:${urn.getCtsNamespace()}:${urn.getTextGroup()}.${urn.getWork()}.${version}:"
		if (urn.getPassageComponent() != null){
			resolvedStr += urn.getPassageComponent()
		}

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
  Ohco2Node getOhco2Node(CtsUrn requestUrn) {

	CtsUrn urn = resolveVersion(requestUrn)

    String label  = getLabel(urn)
	String nodeLang = getLanguage(urn)


    ArrayList leafNodes = getRangeNodes(urn)

    CtsUrn prev = getPrevUrn(urn)
    CtsUrn nxt = getNextUrn(urn)


    Ohco2Node ond = new Ohco2Node(urn, label, nodeLang, prev, nxt, leafNodes)
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
		  CtsUrn replyUrn = null
			  CtsUrn queryUrn = null
			  if (requestUrn.isRange()) {
					  // find first node in range and query on that
					  CtsUrn tempUrn = new CtsUrn("${requestUrn.getUrnWithoutPassage()}${requestUrn.getRangeBegin()}")
					  queryUrn  = resolveVersion(tempUrn)

			  } else {
				  queryUrn  = resolveVersion(requestUrn)
			  }

			  if (isLeafNode(queryUrn)){
				  StringBuilder reply = new StringBuilder()
					  String ctsReply =  sparql.getSparqlReply("application/json", QueryBuilder.getPrevUrnQuery(queryUrn))
					  def slurper = new groovy.json.JsonSlurper()
					  def parsedReply = slurper.parseText(ctsReply)
					  parsedReply.results.bindings.each { bndng ->
						  replyUrn = new CtsUrn(bndng.prevUrn?.value)
					  }
			  } else {
				  // Must be a container
					  Integer firstSequenceOfUrn = getFirstSequence(queryUrn)
					  String firstSeqUrn = getUrnForSequence(firstSequenceOfUrn, queryUrn.getUrnWithoutPassage())
					  replyUrn = getPrevUrn(new CtsUrn(firstSeqUrn))
			  }
		  return replyUrn
	  }


   /** Finds the URN for a given work, with a given sequence
	* Returns only one value, if there are more than one.
    * @param urn A work-level URN.
	* @param a sequence number.
    * @returns A urn, as string.
    */
	String getUrnForSequence(Integer seq, String versionUrnStr ){

        String ctsReply = sparql.getSparqlReply("application/json", QueryBuilder.urnForSequence(seq, versionUrnStr))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(ctsReply)

        String urnStr
        parsedReply.results.bindings.each { b ->
            if (b.urn) {
                urnStr = b.urn?.value
            }
        }
        return urnStr
	}

  /** Finds the next URN preceding a given URN. Returns a string.
   * If the URN is a leaf node, returns the preceeding leaf node.
   * If the URN is a non-leaf node, returns the URN of the preceeding
   * citation at that level of the hierarchy ?
   * @param urn CTS URN to test.
   * @returns The urn of the preceding citable node, as a String,
   * or a null String ("") if there is no preceding citable node.
   * @throws Exception if retrieved value is not a valid CtsUrn string.
   */

  String getNextUrnString(CtsUrn requestUrn) {
	  CtsUrn rurn = resolveVersion(requestUrn)
		  CtsUrn nxt = getNextUrn(rurn)
		  if (nxt != null) {
			  return nxt.toString()
		  } else {
			  return ""
		  }
  }

  /** Finds the next URN preceding a given URN.
   * If the URN is a leaf node, returns the preceeding leaf node.
   * If the URN is a non-leaf node, returns the URN of the preceeding
   * citation at that level of the hierarchy ?
   * @param urn CTS URN to test.
   * @returns CtsUrn of the preceding citable node.
   * @throws Exception if retrieved value is not a valid CtsUrn string.
   */
  CtsUrn getNextUrn(CtsUrn requestUrn)
	  throws Exception {
		  CtsUrn replyUrn = null
			  CtsUrn queryUrn = null
			  if (requestUrn.isRange()) {
					  // find first node in range and query on that
					  CtsUrn tempUrn = new CtsUrn("${requestUrn.getUrnWithoutPassage()}${requestUrn.getRangeEnd()}")
					  queryUrn  = resolveVersion(tempUrn)

			  } else {
				  queryUrn  = resolveVersion(requestUrn)
			  }

			  if (isLeafNode(queryUrn)){
				  StringBuilder reply = new StringBuilder()
					  String ctsReply =  sparql.getSparqlReply("application/json", QueryBuilder.getNextUrnQuery(queryUrn))
					  def slurper = new groovy.json.JsonSlurper()
					  def parsedReply = slurper.parseText(ctsReply)
					  parsedReply.results.bindings.each { bndng ->
						  replyUrn = new CtsUrn(bndng.nextUrn?.value)
					  }
			  } else {
				  // Must be a container
					  Integer lastSequenceOfUrn = getLastSequence(queryUrn)
					  String lastSeqUrn = getUrnForSequence(lastSequenceOfUrn, queryUrn.getUrnWithoutPassage())
					  replyUrn = getNextUrn(new CtsUrn(lastSeqUrn))
			  }

		  return replyUrn
	  }


  /** Gets the TextContent for a CTS Leaf node.
   * @param urn CTS URN
   * @returns String. The Text Content of the CTS Leaf Node.
   */
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



  /** Gets a label for a Cts URN
   * @param urn CTS URN
   * @returns String. A human-readable label for group, work, version, or leaf-node pointed to by the URN.
   */
  String getLabel(CtsUrn urnSubmitted)
  throws Exception {

		String workLevel = urnSubmitted.getWorkLevel()
		String labelQuery = ""
		if (urnSubmitted.passageComponent){

			CtsUrn urn = resolveVersion(urnSubmitted)
			if (urnSubmitted.isRange()){
				String tempString = ""
				labelQuery = QueryBuilder.getRdfLabel(new CtsUrn(urnSubmitted.getUrnWithoutPassage()))
				String reply = sparql.getSparqlReply("application/json", labelQuery)
				def slurper = new groovy.json.JsonSlurper()
				def parsedReply = slurper.parseText(reply)
				def bndng = parsedReply.results.bindings[0]
				if (bndng) {
				  tempString += "Range request: ${urnSubmitted.passageComponent}, from "
				  tempString +=  "${bndng.label?.value}. (${urnSubmitted})."
				  return tempString
				} else {
				  System.err.println "Failed on query:\n ${labelQuery}\n"
				  throw new Exception("CtsGraph:getLabel: no results from query on ${urn}.")
				}
			} else {
				if (isLeafNode(urn)){
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
				} else { // must be a container
					String tempString = ""
					labelQuery = QueryBuilder.getRdfLabel(new CtsUrn(urnSubmitted.getUrnWithoutPassage()))
					String reply = sparql.getSparqlReply("application/json", labelQuery)
					def slurper = new groovy.json.JsonSlurper()
					def parsedReply = slurper.parseText(reply)
					def bndng = parsedReply.results.bindings[0]
					if (bndng) {
					  tempString += "Containing-element request: ${urnSubmitted.passageComponent}, from "
					  tempString +=  "${bndng.label?.value}. (${urnSubmitted})."
					  return tempString
					} else {
					  System.err.println "Failed on query:\n ${labelQuery}\n"
					  throw new Exception("CtsGraph:getLabel: no results from query on ${urn}.")
					}
				}
			}

		} else {
				labelQuery = QueryBuilder.getRdfLabel(urnSubmitted)
				String reply = sparql.getSparqlReply("application/json", labelQuery)
				def slurper = new groovy.json.JsonSlurper()
				def parsedReply = slurper.parseText(reply)
				def bndng = parsedReply.results.bindings[0]
				if (bndng) {
				  return "${bndng.label?.value} (${urnSubmitted})."
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
        String ctsReply = sparql.getSparqlReply("application/json", firstContainedQuery)
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

    /** Finds the last URN of a version- or exemplar.
    * @param urn A version- or exemplar-URN.
    * @returns CtsUrn
    */
    CtsUrn getLastUrn(CtsUrn requestUrn) {
		CtsUrn lastUrn
		CtsUrn urn = resolveVersion(requestUrn)
		String lastUrnQuery = QueryBuilder.getLastUrnQuery(urn)
        String ctsReply = sparql.getSparqlReply("application/json", lastUrnQuery )

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(ctsReply)

        String urnStr
        parsedReply.results.bindings.each { b ->
            if (b.seq) {
                urnStr = b.urn?.value
            }
        }
		lastUrn = new CtsUrn(urnStr)
        return lastUrn
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


  /** Given a Cts URN, returns its language, as a string. The language of a Translation (or exemplar
  * derived from a Translation, or for an Edition, or Edition-exemplar, the language of the Work.
   * @param urn CTS URN
   * @returns String. The language of the text.
   */
	String getLanguage(CtsUrn urn){
		String langString
		String langQueryString = QueryBuilder.getLangQuery(urn.getUrnWithoutPassage().toString())
        String ctsReply = sparql.getSparqlReply("application/json", langQueryString )

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(ctsReply)

        parsedReply.results.bindings.each { b ->
			langString = b.l?.value
		}
		return langString
	}

  /** Given a Cts URN, returns a string identifying it as Edition or Translation.
  * If it is an Exemplar, it will return the type of the parent Version.
   * @param urn CTS URN
   * @returns String. "Edition" or "Translation".
   */
   String getVersionType(CtsUrn urn){
		String typeString = ""
		String versionTypeQueryString = QueryBuilder.getVersionTypeQuery(urn.getUrnWithoutPassage().toString())
        String ctsReply = sparql.getSparqlReply("application/json", versionTypeQueryString )

        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(ctsReply)

        parsedReply.results.bindings.each { b ->
				switch(b.t?.value) {
					case "http://www.homermultitext.org/cts/rdf/Exemplar"	:
						  break;
					case "http://www.homermultitext.org/cts/rdf/Edition"	:
						  typeString = "Edition"
							  break;
					case "http://www.homermultitext.org/cts/rdf/Translation"	:
						  typeString = "Translation"
							  break;
					default:
						  break;
				}
			}
		return typeString
   }

  /** Given a Cts URN, returns a string, "okay" if the URN successfully resolves to a version.
  * Otherwise, returns an error code.
   * @param urn CTS URN
   * @returns String. "okay", or error code.
   */
   String checkUrn(CtsUrn urn){
	    String responseString = ""

		try {
			CtsUrn resolvedUrn = resolveVersion(urn)
			responseString =  "okay"
		} catch (Exception e) {
			responseString = "Invalid URN reference: ${urn}"
		}

		return responseString
   }

  /** Overloaded method
   * @param urnString String
   * @returns ArrayList of CtsUrns
   */
   ArrayList getValidReff(String urnString){
		CtsUrn urn = new CtsUrn(urnString)
		return getValidReff(urn)
   }

  /** Overloaded method
   * @param urnString String
   * @returns ArrayList of CtsUrns
   */
   ArrayList getValidReff(String urnString, Integer level){
		CtsUrn urn = new CtsUrn(urnString)
		return getValidReff(urn, level)
   }

  /** Overloaded method
   * @param urnString String
   * @returns ArrayList of CtsUrns
   */
   ArrayList getValidReff(CtsUrn urn){
	   Integer maxLevel = getLeafDepth(urn)
	   return getValidReff(urn, maxLevel)
   }

  /** Given a Cts URN, returns a valid reffs for that urn.
   * @param urn CTS URN
   * @returns ArrayList of CtsUrns
   */
   ArrayList getValidReff(CtsUrn requestUrn, Integer level){
	   CtsUrn urn = resolveVersion(requestUrn)
	   ArrayList returnList = []
	   // 3 cases to consider:
        if (requestUrn.getPassageComponent() == null) {
            // 1. no limiting passage reference:
            returnList = getValidReffForWork(urn, level)

        } else if (urn.isRange()) {
            // 2. range request
            returnList = getValidReffForRange(urn, level)
        } else {
            // 3. single citation node (leaf or container)
            if (isLeafNode(urn)) {
			   returnList << urn.toString()
            } else {
                returnList = getValidReffForNode(urn, level)
            }
        }
	   return returnList
   }

  /** Gets valid references for a work-level, version-level, or exemplar-level
  * CTS URN, without a passage reference.
  * @param CtsUrn urn
  * @param Integer level
  * @returns ArrayList of CtsUrns
  **/
  ArrayList getValidReffForWork(CtsUrn workUrn, Integer level) {

		ArrayList reply = []
        CtsUrn urn = resolveVersion(workUrn)
        if (isLeafNode(urn)) {
            reply << "${urn}"
        } else {
    		String gvrQuery = QueryBuilder.getWorkGVRQuery(urn, level)
            String ctsReply = sparql.getSparqlReply("application/json", gvrQuery)
            JsonSlurper slurper = new groovy.json.JsonSlurper()
            def parsedReply = slurper.parseText(ctsReply)
            parsedReply.results.bindings.each { b ->
                if (b.ref) {
                    reply << "${b.ref?.value}"
                }
            }
        }
        return reply
    }

  /** Gets valid references for a work-level, version-level, or exemplar-level
  * CTS URN, with a (single) passage reference.
  * @param CtsUrn urn
  * @param Integer level
  * @returns ArrayList of CtsUrns
  **/
    ArrayList getValidReffForNode(CtsUrn urn, Integer level) {
	// two possible query forms: when level is at max depth,
    // and when is higher in citation hierarchy
        ArrayList reply = []
        String ctsReply

        ctsReply = sparql.getSparqlReply("application/json", QueryBuilder.getGVRNodeQuery(urn, level))
        JsonSlurper slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(ctsReply)
		if (parsedReply.results.bindings.size() < 1){
				  throw new Exception("CtsGraph:getGetValidReff: invalid URN. (${urn})")
		} else if ((parsedReply.results.bindings.size() == 1) && (parsedReply.results.bindings[0].ref?.value.size() < 1)  ){
				  throw new Exception("CtsGraph:getGetValidReff: invalid URN. (${urn})")
		} else {
				parsedReply.results.bindings.each { b ->
					if (b.ref) {
						reply << "${b.ref?.value}"
					}
				}
		}
        return reply
    }



    ArrayList getValidReffForRange(CtsUrn urn, Integer level) {
        ArrayList reply = []
        //reply.append getValidReffForNode(new CtsUrn("${urn.getUrnWithoutPassage()}:${urn.getRangeBegin()}"), level)

        CtsUrn urn1 = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeBegin()}")
        CtsUrn urn2 = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeEnd()}")

        Integer startAtStr
        Integer endAtStr

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
        // error check these...
        Integer int1 = startAtStr.toInteger()
        Integer int2 = endAtStr.toInteger()
        reply = getFillVR(int1, int2, level, "${urn.getUrnWithoutPassage()}")

        //reply.append getValidReffForNode(new CtsUrn("${urn.getUrnWithoutPassage()}:${urn.getRangeEnd()}"), level)

        return reply
    }


    /** Retrieves valid references filling a range.	*/
    ArrayList getFillVR(Integer start, Integer end, Integer level, String workUrnStr) {
        ArrayList reply = []
		String fillVRQuery = QueryBuilder.getFillGVRQuery(start, end, level, workUrnStr)
        String fillReply = sparql.getSparqlReply("application/json", fillVRQuery)
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(fillReply)
		if (parsedReply.results.bindings.size() < 2){
			throw new Exception ("invalid urn")
		} else {
				parsedReply.results.bindings.each { b ->
					if (b.ref) {
						reply << "${b.ref?.value}"
					}
				    return reply
				}
		}
		return reply
    }

/** Finds the maximum citation depth of CTS URNs
    * contained by a given CtsUrn.
    * @param urn The containing URN at any level
    * (work, or passage).
    * @returns Depth of citation hierarchy of this URN, or
    * null if no depth could be determined.
    */
    Integer getLeafDepth(CtsUrn requestUrn) {
        Integer deepestInt = null
        CtsUrn urn = resolveVersion(requestUrn)
        String ctsReply
        if (urn.getPassageComponent() == null) {
            ctsReply = sparql.getSparqlReply("application/json", QueryBuilder.getLeafDepthForWorkQuery(urn))
        } else {
            ctsReply = sparql.getSparqlReply("application/json", QueryBuilder.getLeafDepthQuery(urn))
        }
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(ctsReply)

        String intStr
        parsedReply.results.bindings.each { b ->
            if (b.deepest) {
                intStr = b.deepest?.value
            } else {
                System.err.println "No deepest node found for query " + QueryBuilder.getLeafDepthQuery(urn)
            }
            try {
                deepestInt = intStr.toInteger()
            } catch (Exception e) {
                System.err.println "Could not parse sequence ${intStr} as Integer: ${e}"
            }
        }
        return deepestInt
    }

	/** Finds the first citable leaf-node of a text, given a CtsUrn (at any level).
	*   If an exemplar- or version-level URN without a passage component, returns the first
	*   citable leaf-node. If a work-level URN without a passage-component, resolves the work
	*   to a version and returns the first citeable leaf node of the resolved version. If
	*   a containing element, returns the first citable node of that element. If a leaf-node,
	*   returns that. For ranges, returns values based on the first citation of the range.
	*   @param CtsUrn requestUrn
	*   @returns CtsUrn
    **/
	CtsUrn getFirstUrn(CtsUrn requestUrn){
	   String replyUrnString = "urn:cts:greekLit:dogs.cats:1.1"
	   Integer thisSequence

	   CtsUrn urn = resolveVersion(requestUrn)
	   // 3 cases to consider:
        if (urn.getPassageComponent() == null) {
            // 1. no limiting passage reference:

			thisSequence = getFirstSequence(urn)
		    replyUrnString = getUrnForSequence(thisSequence, urn.getUrnWithoutPassage())

        } else if (urn.isRange()) {
            // 2. range request
			CtsUrn urn1 = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeBegin()}")
			thisSequence = getFirstSequence(urn1)
			replyUrnString = getUrnForSequence(thisSequence, urn.getUrnWithoutPassage())

        } else {
            // 3. single citation node (leaf or container)
            if (isLeafNode(urn)) {
				// 3a. Just return it
				replyUrnString = urn.toString()

            } else {
				// 3b. Containing element
				thisSequence = getFirstSequence(urn)
			    replyUrnString = getUrnForSequence(thisSequence, urn.getUrnWithoutPassage())

            }
        }
		CtsUrn replyUrn = new CtsUrn(replyUrnString)
		return replyUrn
	}

  /** Given a range-urn defining N leaf-nodes, returns a CtsUrn, as String
   * defining the preceding N leaf-nodes, or all preceding leaf nodes if
   * the param URN starts less than N nodes from the beginning of the text.
   * @param urn CTS URN to test.
   * @returns The urn of the preceding citable node, as a String,
   * or a null String ("") if there is no preceding citable node.
   * @throws Exception if retrieved value is not a valid CtsUrn string.
   */

	String getRangePrevUrnStr(CtsUrn requestUrn){
		String replyString
		CtsUrn urn = resolveVersion(requestUrn)
		// Calculate how many leaf-nodes are defined by the range
		ArrayList rangeList = getUrnList(urn)
		Integer howManyIdeally = rangeList.size() // Close to the beginning, we may not be able to return this many
		Integer howMany // this will be number of leaf-nodes we actually return
		Integer startSeq
		Integer endSeq
		// Calculate the sequence number of the first leaf-node of the text
		CtsUrn firstUrnOfText = getFirstUrn(new CtsUrn("${urn.getUrnWithoutPassage()}"))
		Integer firstSequenceOfText = getSequence(firstUrnOfText)
		// Calculate the first sequence number of the param Urn
		CtsUrn firstUrnOfRange
		if (urn.isRange()){
			CtsUrn testFirstUrnOfRange = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeBegin()}")
			if (isLeafNode(testFirstUrnOfRange)){
				firstUrnOfRange = testFirstUrnOfRange
			} else {
				firstUrnOfRange = getFirstUrn(testFirstUrnOfRange)
			}
		} else if (isLeafNode(urn)) {
		    firstUrnOfRange = urn
		} else {
			firstUrnOfRange = getFirstUrn(urn)
		}
		Integer firstSequenceOfRange = getSequence(firstUrnOfRange)
		// Calculate how many we go back
		if ((firstSequenceOfRange - howManyIdeally) < firstSequenceOfText){
			howMany = firstSequenceOfRange - firstSequenceOfText
		} else {
			howMany = howManyIdeally
		}
		if (howMany < 1){
			replyString = ""
		} else {
			startSeq = firstSequenceOfRange - howMany
			endSeq = (startSeq) + (howMany -1)
			if (startSeq == endSeq){
				replyString = "${getUrnForSequence(startSeq,urn.getUrnWithoutPassage())}"
			} else {
				Integer leafDepth = getLeafDepth(urn)
				ArrayList fillVR =  getFillVR(startSeq, endSeq, leafDepth, "${urn.getUrnWithoutPassage()}")
				String startPassage = new CtsUrn(fillVR[0]).passageComponent
				String endPassage  = new CtsUrn(fillVR[-1]).passageComponent
				replyString = "${urn.getUrnWithoutPassage()}${startPassage}-${endPassage}"
			}
		}
		return replyString
	}

  /** Given a range-urn defining N leaf-nodes, returns a CtsUrn, as String
   * defining the next N leaf-nodes, or all followin leaf nodes if
   * the text ends fewer than N nodes after the param URN's ending citation.
   * @param urn CTS URN to test.
   * @returns The urn of the preceding citable node, as a String,
   * or a null String ("") if there is no following citable node.
   * @throws Exception if retrieved value is not a valid CtsUrn string.
   */
	String getRangeNextUrnStr(CtsUrn requestUrn){
		String replyString
		CtsUrn urn = resolveVersion(requestUrn)
		// Calculate how many leaf-nodes are defined by the range
		ArrayList rangeList = getUrnList(urn)
		Integer howManyIdeally = rangeList.size() // Close to the beginning, we may not be able to return this many
		Integer howMany // this will be number of leaf-nodes we actually return
		Integer startSeq
		Integer endSeq
		// Calculate the sequence number of the last leaf-node of the text
		CtsUrn lastUrnOfText = getLastUrn(new CtsUrn("${urn.getUrnWithoutPassage()}"))
		Integer lastSequenceOfText = getSequence(lastUrnOfText)
		// Calculate the last sequence number of the param Urn
		CtsUrn lastUrnOfRange
		if (urn.isRange()){
			CtsUrn testLastUrnOfRange = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeEnd()}")
			if (isLeafNode(testLastUrnOfRange)){
				lastUrnOfRange = testLastUrnOfRange
			} else {
				lastUrnOfRange = new CtsUrn(getUrnForSequence(getLastSequence(testLastUrnOfRange),urn.getUrnWithoutPassage()))
			}
		} else if (isLeafNode(urn)) {
		    lastUrnOfRange = urn
		} else {
			lastUrnOfRange = new CtsUrn(getUrnForSequence(getLastSequence(urn),urn.getUrnWithoutPassage()))
		}
		Integer lastSequenceOfRange = getSequence(lastUrnOfRange)
		// Calculate how many we go forward
		if ((lastSequenceOfRange + howManyIdeally) > lastSequenceOfText){
			howMany = lastSequenceOfText - lastSequenceOfRange
		} else {
			howMany = howManyIdeally
		}
		if (howMany < 1){
			replyString = ""
		} else {
			startSeq = lastSequenceOfRange + 1
			endSeq = (startSeq) + (howMany-1)
			if (startSeq == endSeq){
				replyString = "${getUrnForSequence(startSeq,urn.getUrnWithoutPassage())}"
			} else {
				Integer leafDepth = getLeafDepth(urn)
				ArrayList fillVR =  getFillVR(startSeq, endSeq, leafDepth, "${urn.getUrnWithoutPassage()}")
				String startPassage = new CtsUrn(fillVR[0]).passageComponent
				String endPassage  = new CtsUrn(fillVR[-1]).passageComponent
				replyString = "${urn.getUrnWithoutPassage()}${startPassage}-${endPassage}"
			}
		}
		return replyString
	}

	/** Given a CtsUrn and a context-integer, returns a range reflecting
	* the urn's passage, plus context
	* @param CtsUrn requestUrn
	* @param Integer context
	* @returns A range-URN as CtsUrn
	*/

	CtsUrn getRangeForContext(CtsUrn requestUrn, Integer context){
		CtsUrn urn = resolveVersion(requestUrn)

		CtsUrn responseUrn
		Integer startSeq
		Integer endSeq
		// Calculate the sequence number of the first leaf-node of the text
		CtsUrn firstUrnOfText = getFirstUrn(new CtsUrn("${urn.getUrnWithoutPassage()}"))
		Integer firstSequenceOfText = getSequence(firstUrnOfText)
		// Calculate the sequence number of the last leaf-node of the text
		CtsUrn lastUrnOfText = getLastUrn(new CtsUrn("${urn.getUrnWithoutPassage()}"))
		Integer lastSequenceOfText = getSequence(lastUrnOfText)
		// Sequences of param urn
		Integer firstSequence
		Integer lastSequence
		// Ideal first and last sequence of resulting range urn
		Integer idealFirstSequence
		Integer idealLastSequence


		// Different solutions for ranges, leaves, and containers
		if (urn.isRange()){
			CtsUrn testFirstUrnOfRange = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeBegin()}")
			CtsUrn testLastUrnOfRange = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeEnd()}")
			if (isLeafNode(testFirstUrnOfRange)){
				firstSequence = getSequence(testFirstUrnOfRange)
			} else {
				firstSequence = getFirstSequence(testFirstUrnOfRange)
			}
			if (isLeafNode(testLastUrnOfRange)){
				lastSequence = getSequence(testLastUrnOfRange)
			} else {
				lastSequence = getLastSequence(testLastSequenceOfRange)
			}
		} else if (isLeafNode(urn)) {
		   firstSequence = getSequence(urn)
		   lastSequence = firstSequence
		} else {
			firstSequence = getFirstSequence(urn)
			lastSequence = getLastSequence(urn)
		}
		idealFirstSequence = firstSequence - context
		idealLastSequence = lastSequence + context
		if (idealFirstSequence < firstSequenceOfText){
			startSeq = firstSequenceOfText
		} else {
			startSeq = idealFirstSequence
		}
		if (idealLastSequence > lastSequenceOfText){
			endSeq = lastSequenceOfText
		} else {
			endSeq = idealLastSequence
		}

	    CtsUrn firstSeqUrn = new CtsUrn(getUrnForSequence(startSeq, urn.getUrnWithoutPassage()))
	    CtsUrn lastSeqUrn = new CtsUrn(getUrnForSequence(endSeq, urn.getUrnWithoutPassage()))
		responseUrn = new CtsUrn("${urn.getUrnWithoutPassage()}${firstSeqUrn.passageComponent}-${lastSeqUrn.passageComponent}")

		return responseUrn
	}

}
