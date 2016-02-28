package edu.holycross.shot.sparqlcts

import edu.holycross.shot.citeservlet.Sparql

import edu.harvard.chs.cite.CtsUrn
import groovy.json.JsonSlurper
import edu.holycross.shot.sparqlcts.CtsGraph

/** A class interacting with CtsGraph
 * to construct abstract data objects for CTS replies
 */
class CtsReply {

  Sparql sparql 
  CtsGraph graph 
  XmlFormatter formatter
  

  /** Constructor with required sparql and graph services */  
  CtsReply( Sparql endPoint,  CtsGraph myGraph ) {
   	sparql = endPoint
    graph = myGraph	
	formatter = new XmlFormatter()
  }

  
/* ******************************************
* GetPassage Replies
   ****************************************** */

  /**  Given a URN, constructs a getPassagePlus reply data-object
   * @returns ctsReply as Map
   */
	Map getPassagePlusReply(CtsUrn requestUrn){
		Map ctsReply = [:]
		Map ctsReplyObject = [:]
		Map ctsRequestMap = [:]
		Map ctsReplyMap = [:]

		CtsUrn urn = graph.resolveVersion(requestUrn)

		Ohco2Node o2n = graph.getOhco2Node(urn)


		
		ctsRequestMap.put('request','GetPassagePlus')
		ctsRequestMap.put('urn',requestUrn.toString())
		ctsReplyMap.put('urn',urn.toString())
		ctsReplyMap.put('label',o2n.nodeLabel)
		ctsReplyMap.put('rangeNodesMap',o2n.leafNodes)

		ctsReplyObject.put('request',ctsRequestMap)
		ctsReplyObject.put('reply',ctsReplyMap)	
		ctsReply.put('GetPassagePlus',ctsReplyObject)

		return ctsReply
	}

	/**  Given a URN, constructs a getPassagePlus reply as
	 * an XML fragment, by first making a getPassagePlus reply Map.
	 * @returns ctsReply as Map
	 */
	String getPassagePlusToXML(CtsUrn requestUrn){
        StringBuffer xmlString = new StringBuffer()
		String xmlCtsFragString = ""
			Map gppObject = getPassagePlusReply(requestUrn)
			
			xmlCtsFragString = formatter.buildXmlFragment(gppObject['GetPassagePlus']['reply']['rangeNodesMap'])
			// Format for CTS Reply
			xmlString.append(xmlCtsFragString)
			return xmlString
	}


	

  
}
