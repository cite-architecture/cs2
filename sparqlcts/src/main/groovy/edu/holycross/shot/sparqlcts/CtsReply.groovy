package edu.holycross.shot.sparqlcts

import edu.holycross.shot.citeservlet.Sparql

import edu.harvard.chs.cite.CtsUrn
import groovy.json.JsonSlurper
import edu.holycross.shot.sparqlcts.CtsGraph

/** A class interacting with CtsGraph
 * to construct abstract data objects for CTS replies
 */
class CtsReply {

  /** SPARQL endpoint object from citeservlet lib. */
  Sparql sparql
  CtsGraph graph


  /** Constructor with required SPARQL endpoint object */  
  CtsReply(Sparql endPoint, CtsGraph graphObject) {
    sparql = endPoint
	graph = graphObject
  }

  
/* ******************************************
* GetPassage Replies
   ****************************************** */

	Map getPassagePlusReply(CtsUrn urn){
		Map ctsReply = [:]
		Map ctsReplyObject = [:]
		Map ctsRequestMap = [:]
		Map ctsReplyMap = [:]

		ArrayList rangeNodesMap = graph.getRangeNodes(urn)

		
		ctsRequestMap.put('request','GetPassagePlus')
		ctsRequestMap.put('urn',urn.toString())
		ctsReplyMap.put('urn',graph.resolveVersion(urn).toString())
		ctsReplyMap.put('label',graph.getLabel(urn))
		ctsReplyMap.put('rangeNodesMap',rangeNodesMap)

		ctsReplyObject.put('request',ctsRequestMap)
		ctsReplyObject.put('reply',ctsReplyMap)	
		ctsReply.put('GetPassagePlus',ctsReplyObject)

		return ctsReply
	}


   
  
}
