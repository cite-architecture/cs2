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
   * @param urn CITE Object to find in the graph.
   * @returns ArrayList of Triple objects.
   */
	ArrayList findAdjacent(CtsUrn urn) {
		ArrayList al = []
		String workLevel = urn.getWorkLevel()

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

        if (workLevel == "GROUP"){
			al << "groupLevel"	
			/* group-level URNs have no passage component */
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
			if (ctsgraph.isLeafNode(urn)){
				if (workLevel == "VERSION"){
					al = getSingleLeafNodeGraph(urn)
			    } else {
					ArrayList versionUrns = getVersionsForNotionalUrn(urn)
					al = versionUrns
				}
			} else {
				al << "container"
			}
		}
	    return al
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
		requestUrn = new CtsUrn(urn.getUrnWithoutSubreference())
	} else {
		requestUrn = urn
	}
	ArrayList replyArray = []
    String replyText = ""
    String leafQuery = QueryBuilder.getSingleLeafNodeQuery(urn)
    String reply = sparql.getSparqlReply("application/json", leafQuery)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
    parsedReply.each{ jo ->
		replyArray << jo // work to be done here!
	}


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
	ArrayList replyArray = []

    String versionQuery = QueryBuilder.getVersionsForWork(requestUrn)
    String reply = sparql.getSparqlReply("application/json", versionQuery)
    JsonSlurper versionSlurper = new groovy.json.JsonSlurper()
    def versionParsedReply = versionSlurper.parseText(reply)
    versionParsedReply.results.bindings.each{ jo ->
		if (jo.version){
			versionArray << jo.version?.value // work to be done here!
		}
	}

    String notionalQuery = QueryBuilder.getQueryNotionalCitation(urn, versionArray)  
    reply = sparql.getSparqlReply("application/json", notionalQuery)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
	replyArray << "${urn}"
	replyArray << "${urn.passageComponent}"
    parsedReply.each{ jo ->
		replyArray << jo // work to be done here!
	}

	return replyArray

   }

		

}
