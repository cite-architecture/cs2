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
				al << "groupLevel"				
				break;
			case "WORK":
				al << "workLevel"
				break;
			case "VERSION":
				al<< "versionLevel"
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

   /** Finds  data adjacent to a version-level containing (non-leaf-node) URN 
   * @param urn The URN to test.
   * @returns ArrayList of Triple objects.
   */

	ArrayList getAdjacentForVersionContainer(CtsUrn urn){
	    ArrayList replyArray = []
		ArrayList workingArray = []
        String replyText = ""
		String containerQuery = QueryBuilder.getQueryVersionLevelContaining(urn)
        String reply = sparql.getSparqlReply("application/json", containerQuery)

        JsonSlurper slurper = new groovy.json.JsonSlurper()
		def parsedReply = slurper.parseText(reply)
		workingArray = parsedJsonToTriples(parsedReply)

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

   ArrayList getSingleLeafNodeGraph(urn){
	CtsUrn requestUrn
	if (urn.hasSubref()){
		requestUrn = new CtsUrn(urn.reduceToNode())
	} else {
		requestUrn = urn
	}
	ArrayList replyArray = []
	ArrayList workingArray = []
    String replyText = ""
    String leafQuery = QueryBuilder.getSingleLeafNodeQuery(urn)    
	String reply = sparql.getSparqlReply("application/json", leafQuery)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
	workingArray = parsedJsonToTriples(parsedReply)

	replyArray = uniqueTriples(replyArray) 
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

		parsedReply.results.bindings.each{ jo ->
			tempSubject = new URI(URLEncoder.encode(jo.s.value, "UTF-8"))
				tempVerb = new URI(URLEncoder.encode(jo.v.value, "UTF-8"))

				switch (jo.o.type){
					case "uri":
						tempObject = new URI(URLEncoder.encode(jo.o.value, "UTF-8"))
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
		}

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
