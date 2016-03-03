package edu.holycross.shot.sparqlcts

import edu.holycross.shot.citeservlet.Sparql

import groovy.json.*
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
		ctsReplyMap.put('prev',o2n.prevUrn.toString())
		ctsReplyMap.put('nxt',o2n.nextUrn.toString())
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


	/**  Overloaded function. Turn a urn-string into a CTS-URN, and return
	 * an XML fragment, by first making a getPassagePlus reply Map.
	 * @returns String
	 */
	String getPassagePlusToXML(String requestUrn){
			CtsUrn urn = new CtsUrn(requestUrn)
			return getPassagePlusToXML(urn)
	}

	/**  Given a URN, constructs a getPassagePlus reply as
	 * an XML fragment, by first making a getPassagePlus reply Map.
	 * @returns String
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

	/**  Overloaded function. Turn a urn-string into a CTS-URN, and return
	 * a JSOH fragment, by first making a getPassagePlus reply Map.
	 * @returns String
	 */
	String getPassagePlusToJSON(String requestUrn){
			CtsUrn urn = new CtsUrn(requestUrn)
			return getPassagePlusToJSON(urn)
	}

	/**  Given a URN, constructs a getPassagePlus reply as
	 * an XML fragment, by first making a getPassagePlus reply Map.
	 * @returns ctsReply as Map
	 */
	String getPassagePlusToJSON(CtsUrn requestUrn){
		Map gppObject = getPassagePlusObject(requestUrn)
	    return new JsonBuilder(gppObject).toPrettyString()
	}


	

  
}
