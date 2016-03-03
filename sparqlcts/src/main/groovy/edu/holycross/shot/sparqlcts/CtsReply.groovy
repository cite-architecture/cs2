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
	Map getPassagePlusObject(CtsUrn requestUrn){
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
		ctsReplyMap.put('lang',o2n.nodeLang)
		ctsReplyMap.put('prev',o2n.prevUrn)
		ctsReplyMap.put('nxt',o2n.nextUrn)
		//ctsReplyMap.put('rangeNodesMap',o2n.leafNodes)

		// Insert passage component
		//    - Will be XML Formatted, if the source-text in RDF requires,
		//      otherwise will be a two-line separated list of leaf-nodes
		String xmlCtsFragString = ""
		xmlCtsFragString = formatter.buildXmlFragment(o2n.leafNodes)
		ctsReplyMap.put('passageComponent',xmlCtsFragString)

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
		Map gppObject = getPassagePlusObject(requestUrn)
		

		xmlString.append("""
			<GetPassagePlus xmlns:cts="http://chs.harvard.edu/xmlns/cts" xmlns="http://chs.harvard.edu/xmlns/cts">
			""")
		xmlString.append("""
			<cts:request>
				<requestName>GetPassagePlus</requestName>
				<requestUrn>${requestUrn}</requestUrn>
				<requestContext>0</requestContext>
			</cts:request>
			""")
		xmlString.append("""
			<cts:reply>
					<urn>${gppObject['GetPassagePlus']['reply']['urn']}</urn>
					<label> ${gppObject['GetPassagePlus']['reply']['label']} </label>
					<passage xml:lang="${gppObject['GetPassagePlus']['reply']['lang']}">
					${gppObject['GetPassagePlus']['reply']['passageComponent']}
					</passage>
					<prevnext>
						<prev>
							<urn>${gppObject['GetPassagePlus']['reply']['prev']}</urn>
						</prev>
						<next>
							<urn>${gppObject['GetPassagePlus']['reply']['nxt']}</urn>
						</next>
					</prevnext>
				</cts:reply>
			</GetPassagePlus>
			""")


		// Format for CTS Reply
		// xmlString.append(gppObject['GetPassagePlus']['reply']['passageComponent'])
		return xmlString
	}

	/**  Given a URN, constructs a getPassagePlus reply as
	 * an XML fragment, by first making a getPassagePlus reply Map.
	 * @returns ctsReply as Map
	 */
	String getPassagePlusToJSON(CtsUrn requestUrn){
        StringBuffer xmlString = new StringBuffer()
		Map gppObject = getPassagePlusReply(requestUrn)

		// Format for CTS Reply
		xmlString.append(gppObject['GetPassagePlus']['reply']['passageComponent'])
		return xmlString
	}


	

  
}
