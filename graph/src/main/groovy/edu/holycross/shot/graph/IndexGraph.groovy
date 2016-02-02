package edu.holycross.shot.graph

import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import groovy.json.JsonSlurper

/** A class interacting with a SPARQL endpoint to
 * to resolve SPARQL replies into objects in the abstract data 
 * model of CITE Collection objects.
 */
class IndexGraph {

  /** SPARQL endpoint object from citeservlet lib. */
  Sparql sparql

  /** We're going to be doing so much CTS work, let's just re-use CtsGraph */
  CtsGraph ctsgraph

  /** Constructor with required SPARQL endpoint object */  
  IndexGraph(Sparql endPoint) {
    sparql = endPoint
	ctsgraph = new CtsGraph(sparql)
  }

  /** Overloaded method. Find all nodes at one degree of 
   * relation to the object identified by
   * a CITE urn.
   * @param urn CITE Object to find in the graph.
   * @returns ArrayList of Triple objects.
   */
	ArrayList findAdjacent(CiteUrn urn) {
		ArrayList al = []
		al << "dogs"
		al << "cats"
	} 

  /** Overloaded method. Find all nodes at one degree of 
   * relation to the object identified by
   * a CITE urn.
   * @param urn CTS Object to find in the graph.
   * @returns ArrayList of Triple objects.
   */
	ArrayList findAdjacent(CtsUrn urn) {
		ArrayList al = []
		CtsUrn testUrn = new CtsUrn(urn.reduceToNode())
		String workLevel = testUrn.getWorkLevel()

		/* Possibilities:
			I.  Version- or Exemplar-level URN
					a. leaf-node
					b. containing element
					c. range (including ranges of containing elements)
			II. Work-level URN
					a. leaf-node
					b. containing element
					c. range
			III. Group-level URN

			n.b. Drop sub-refs from parameter URN, but find all triple-subject with the same citation-value,
				 including those with sub-references.

		*/

		switch (workLevel){
			case "GROUP":
				al = getAdjacentForTextGroup(urn)
				break;
			case "WORK":
				al << "workLevel"
				break;
			case "VERSION":
				/* Can be range or not */
				if (urn.isRange()){
					al << "version; range"
				} else {
					/* Can be leaf-node */
					if (ctsgraph.isLeafNode(urn)){
						al = getSingleLeafNodeGraph(urn)
					} else {
						/* is non-leaf-node citation */
						al = getAdjacentForVersionContainer(urn)
					}
				}
				break;
			case "EXEMPLAR":
				al << "exemplarLevel"
				break;
			default: 
				al << "error ${workLevel}"		


		}

			/*
		} else if (urn.isRange()){
			al << "isRange"
			CtsUrn rangeStartUrn = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeBegin()}")	
			CtsUrn rangeEndUrn = new CtsUrn("${urn.getUrnWithoutPassage()}${urn.getRangeEnd()}")	
			if ((ctsgraph.isLeafNode(rangeStartUrn)) && (ctsgraph.isLeafNode(rangeEndUrn))){
				al << "leafNodeRange"
			} else if ((ctsgraph.isLeafNode(rangeStartUrn)) || (ctsgraph.isLeafNode(rangeEndUrn))) {
				al << "mixedRange"
			}
		} else {
			al << "isnotrange"
			if (ctsgraph.isLeafNode(testUrn)){
				if (workLevel == "VERSION"){
					al = getSingleLeafNodeGraph(testUrn)
			    } else {
					ArrayList versionUrns = getVersionsForNotionalUrn(testUrn)
					al = getAdjacentForNotionalLeaf(testUrn,versionUrns)
				}
			} else {
				if (workLevel == "VERSION"){
					al = getAdjacentForVersionContainer(urn)
				} else {
					al = getAdjacentForWorkLevelContainer(urn)
				}
			}
		}
		*/
	    return al
	} 


/** Finds data adjacent to a TextGroup-level URN.
* @param urn The URN to test.
* @returns ArrayList of Triple objects.
*/
ArrayList getAdjacentForTextGroup(CtsUrn urn){
	    ArrayList replyArray = []
		ArrayList workingArray = []
        String replyText = ""
		String textgroupQuery = QueryBuilder.getTextGroupAdjacentQuery(urn.encodeSubref())
        String reply = sparql.getSparqlReply("application/json", textgroupQuery)

        JsonSlurper slurper = new groovy.json.JsonSlurper()
		def parsedReply = slurper.parseText(reply)
		workingArray = parsedJsonToTriples(parsedReply)
		replyArray = uniqueTriples(workingArray) 
		return replyArray
}

   /** Finds  data adjacent to a version-level containing (non-leaf-node) URN 
   * We want anything indexed to the citation itself, all contained leaf-node citations,
   * and the same for any exemplars.
   * @param urn The URN to test.
   * @returns ArrayList of Triple objects.
   */

	ArrayList getAdjacentForVersionContainer(CtsUrn urn){
		ArrayList exemplarArray = []
	    ArrayList replyArray = []
		ArrayList workingArray = []

	
		// Get Exemplars
		String versionUrnString = urn.getUrnWithoutPassage()
		String passageString = urn.getPassageNode()

		CtsUrn bareVersionUrn = new CtsUrn(versionUrnString)
		exemplarArray = exemplarsForVersion(bareVersionUrn)
			
		// Get adjacents for this version

        String replyText = ""
		String containerQuery = QueryBuilder.getQueryVersionLevelContaining(urn.encodeSubref())
        String reply = sparql.getSparqlReply("application/json", containerQuery)

        JsonSlurper slurper = new groovy.json.JsonSlurper()
		def parsedReply = slurper.parseText(reply)

		parsedJsonToTriples(parsedReply).each { workingArray << it }

		
		// Get adjacents for exemplars

		exemplarArray.each{ exemInstance ->
			CtsUrn exemplarUrn = new CtsUrn("${exemInstance}${passageString}")
			replyText = ""
		    containerQuery = QueryBuilder.getQueryVersionLevelContaining(exemplarUrn.encodeSubref())
            reply = sparql.getSparqlReply("application/json", containerQuery)

			slurper = new groovy.json.JsonSlurper()
			parsedReply = slurper.parseText(reply)

		    parsedJsonToTriples(parsedReply).each { workingArray << it }
		}

		
		// Assemble	

		replyArray = uniqueTriples(workingArray) 
		return replyArray

	}

/** Finds  data adjacent to a work-level containing (non-leaf-node) URN 
   * @param urn The URN to test.
   * @returns ArrayList of Triple objects.
   */

	ArrayList getAdjacentForWorkLevelContainer(CtsUrn urn){
		ArrayList versionArray = []
		ArrayList workingArray = []
	    ArrayList replyArray = []
		ArrayList tempArray = []
		versionArray = getVersionsForNotionalUrn(urn)

        String replyText = ""
		String generalQuery = QueryBuilder.generalQuery(urn)
        String reply = sparql.getSparqlReply("application/json", generalQuery)

        JsonSlurper slurper = new groovy.json.JsonSlurper()
		def parsedReply = slurper.parseText(reply)
		workingArray = parsedJsonToTriples(parsedReply)

		String tempQuery = ""

		versionArray.each { u ->
			tempArray = getAdjacentForVersionContainer(new CtsUrn("${u}${urn.passageComponent}"))	
			tempArray.each { ttt ->
				workingArray << ttt
			}
		}


		replyArray = uniqueTriples(workingArray) 

		return replyArray
	}


  /** Find all nodes at one degree of 
   * relation to the object identified by
   * a CTS leaf-node, version- or exemplar-level urn.
   * @param urn CTS Object to find in the graph.
   * @returns ArrayList of Triple objects.
   */

   ArrayList getSingleLeafNodeGraph(CtsUrn urn){
	CtsUrn requestUrn
	ArrayList exemplarArray = []
	ArrayList replyArray = []
	ArrayList workingArray = []

	println "${urn} is at ${urn.getWorkLevel()} level."

	if (urn.hasSubref()){
		requestUrn = new CtsUrn(urn.reduceToNode())
	} else {
		requestUrn = urn
	}

    // Get Exemplars
	String versionUrnString = requestUrn.getUrnWithoutPassage()
	String passageString = requestUrn.getPassageNode()

	CtsUrn bareVersionUrn = new CtsUrn(versionUrnString)
	exemplarArray = exemplarsForVersion(bareVersionUrn)

	// Get adjacents for this version, minus any subref
	
    String replyText = ""
    String leafQuery = QueryBuilder.getSingleLeafNodeQuery(requestUrn.encodeSubref())    
	String reply = sparql.getSparqlReply("application/json", leafQuery)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
	workingArray = parsedJsonToTriples(parsedReply)

	// Get adjacents for this version, with subref

	replyText = ""
    leafQuery = QueryBuilder.getSingleLeafNodeQuery(requestUrn.encodeSubref())    
	reply = sparql.getSparqlReply("application/json", leafQuery)
    slurper = new groovy.json.JsonSlurper()
    parsedReply = slurper.parseText(reply)
	if (parsedReply.results.size() > 0 ){
		parsedJsonToTriples(parsedReply).each { workingArray << it }
	}


    // Get adjacents for exemplars

	exemplarArray.each{ exemInstance ->
		CtsUrn exemplarUrn = new CtsUrn("${exemInstance}${passageString}")
		replyText = ""
		String containerQuery = QueryBuilder.getSingleLeafNodeQuery(exemplarUrn.encodeSubref())
		reply = sparql.getSparqlReply("application/json", containerQuery)

		slurper = new groovy.json.JsonSlurper()
		parsedReply = slurper.parseText(reply)

		parsedJsonToTriples(parsedReply).each { workingArray << it }
	}

	println "workingArray.size() = ${workingArray.size()}"
	replyArray = uniqueTriples(workingArray) 
	println "replyArray.size() = ${replyArray.size()}"
	return replyArray

   }

  /** Given a work-level URN with a citation
   * return an ArrayList of version-level URNs.
   * @param urn CTS Object to find in the graph.
   * @returns ArrayList of Triple objects.
   */
   ArrayList getVersionsForNotionalUrn(urn){
	CtsUrn requestUrn
	requestUrn = new CtsUrn(urn.getUrnWithoutPassage())
	ArrayList versionArray = []

    String versionQuery = QueryBuilder.getVersionsForWork(requestUrn)
    String reply = sparql.getSparqlReply("application/json", versionQuery)
    JsonSlurper versionSlurper = new groovy.json.JsonSlurper()
    def versionParsedReply = versionSlurper.parseText(reply)
    versionParsedReply.results.bindings.each{ jo ->
		if (jo.version){
			versionArray << jo.version?.value // work to be done here!
		}
	}
	return versionArray
   }


  /** Given a work-level URN with a citation
   * and an ArrayList of Version-level URNs of that work, return adjacent nodes
   * @param urn CTS Object to find in the graph.
   * @param versionArray An ArrayList of Version-level urns.
   * @returns ArrayList of Triple objects.
   */
	
   ArrayList getAdjacentForNotionalLeaf(CtsUrn urn, ArrayList versionArray) {

	   String notionalQuery = QueryBuilder.getQueryNotionalCitation(urn, versionArray)  
	   String reply = sparql.getSparqlReply("application/json", notionalQuery)
	   JsonSlurper slurper = new groovy.json.JsonSlurper()
	   def parsedReply = slurper.parseText(reply)

	   ArrayList replyArray = []
	   ArrayList workingArray = []

	   workingArray = parsedJsonToTriples(parsedReply)
	   replyArray = uniqueTriples(replyArray) 

	   return replyArray

   }

 /** Given a JSON reply from SPARQL
   * construct a ListArray of Triple objects, 
   * including the work of sorting out object-types, and
   * catching labels on URI objects, and making them into Triples, too.
   * @param parsedReply Object, the parsed JSON text
   * @returns ArrayList of Triple objects.
   */
	ArrayList parsedJsonToTriples(Object parsedReply){
		ArrayList replyArray = []

		URI tempSubject
		URI tempVerb
		Object tempObject
		String tempString = ""
	
		if (parsedReply?.results && (parsedReply?.results.bindings.size() > 0)){	
			println "got here."
			parsedReply.results.bindings.each{ jo ->
				tempString = URLDecoder.decode(jo.s.value,"UTF-8")
				tempSubject = new URI(URLEncoder.encode(tempString, "UTF-8")) // decode the URL encoding from Fuseki
					tempVerb = new URI(URLEncoder.encode(jo.v.value, "UTF-8")) // re-encode as URI

					switch (jo.o.type){
						case "uri":
							tempString = URLDecoder.decode(jo.o.value,"UTF-8") // decode the URL encoding from Fuseki
							tempObject = new URI(URLEncoder.encode(tempString, "UTF-8")) // Encode as URI
								break;
						case "literal":
							tempObject = jo.o.value
								break;
						case "typed-literal":
							tempObject = jo.o.value.toInteger()
								break;
					}

				Triple tempTriple = new Triple(tempSubject, tempVerb, tempObject)
					replyArray << tempTriple 
					// We also want rdf:labels for all URI objects, to be nice
					if (jo.label){
						tempVerb = new URI("rdf:label")
							tempSubject = tempObject
							tempTriple = new Triple(tempSubject,tempVerb,jo.label?.value)	
							replyArray << tempTriple
					}
					// We also want cts:hasSequence for all URI objects, to be nice
					if (jo.ctsSeq){
						tempVerb = new URI("cts:hasSequence")
							tempSubject = tempObject
							tempTriple = new Triple(tempSubject,tempVerb,jo.ctsSeq?.value)	
							replyArray << tempTriple
					}
					// We also want olo:item sequencing for all URI objects, to be nice
					if (jo.objSeq){
						tempVerb = new URI("olo:item")
							tempSubject = tempObject
							tempTriple = new Triple(tempSubject,tempVerb,jo.objSeq?.value)	
							replyArray << tempTriple
					}
			}
		}

		return replyArray
	}

	/** Given a version-level URN, returns all exemplar-level URNs
	* @param urn CTS-URN
	* @returns ArrayList of CTS-URNs
	*/
	ArrayList exemplarsForVersion(CtsUrn urn){
	    ArrayList replyArray = []
        String replyText = ""
		String exemplarQuery = QueryBuilder.getQueryExemplarsForVersion(urn.encodeSubref())
        String reply = sparql.getSparqlReply("application/json", exemplarQuery)
		if ("${urn.getWorkLevel()}" == "VERSION"){	

			JsonSlurper slurper = new groovy.json.JsonSlurper()
			def parsedReply = slurper.parseText(reply)
			parsedReply.results.bindings.each{ jo ->
				replyArray << jo.o?.value		
			}
		} else {
			replyArray << "ERROR: URN must point to a version-level URN"	
		}

		return replyArray
	}

	/** Given a work-level URN, returns all version and exemplar-level URNs
	* @param urn CTS-URN
	* @returns ArrayList of CTS-URNs
	*/
	ArrayList versionsAndExemplarForWork(CtsUrn urn){
		ArrayList replyArray = []
		replyArray << "Not implemented"
		return replyArray
	}


   /** Given an ArrayList of Triple objects
   * eliminate duplicates and return a new ArrayList
   * @param al ArrayList of Triple object
   * @returns ArrayList of Triple objects.
   */

	ArrayList uniqueTriples(ArrayList al){
		def tripleComparator = [
			equals: { delegate.equals(it) },
			compare: { first, second ->
			first.toString() <=> second.toString()
			}
		] as Comparator
	    def tsub = al.unique(tripleComparator)
		return tsub
	}


		
}
