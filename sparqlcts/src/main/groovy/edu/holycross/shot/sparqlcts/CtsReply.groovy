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
		ctsReplyMap.put('next',o2n.nextUrn.toString())
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
							<urn>${gppObject['GetPassagePlus']['reply']['next']}</urn>
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

/* *****************************************
GetPrevNext Request
***************************** */

  /**  Given a URN, constructs a getPassagePlus reply data-object
   * @returns ctsReply as Map
   */
	Map getPrevNextObject(CtsUrn requestUrn){
		Map ctsReply = [:]
		Map ctsReplyObject = [:]
		Map ctsRequestMap = [:]
		Map ctsReplyMap = [:]

		CtsUrn urn = graph.resolveVersion(requestUrn)

		
		ctsRequestMap.put('request','GetPrevNextUrn')
		ctsRequestMap.put('urn',requestUrn.toString())

		ctsReplyMap.put("prev",graph.getPrevUrn(urn))
		ctsReplyMap.put("next",graph.getNextUrn(urn))

		ctsReplyObject.put('request',ctsRequestMap)
		ctsReplyObject.put('reply',ctsReplyMap)	
		ctsReply.put('GetPrevNextUrn',ctsReplyObject)


		return ctsReply
	}
		
		/**  Overloaded function. Turn a urn-string into a CTS-URN, and return
	 * an XML fragment, by first making a getPrevNext reply Map.
	 * @returns String
	 */
	String getPrevNextToXML(String requestUrn){
			CtsUrn urn = new CtsUrn(requestUrn)
			return getPrevNextToXML(urn)
	}

	/**  Given a URN, constructs a getPrevNextUrn reply as
	 * an XML fragment, by first making a getPrevNext reply Map.
	 * @returns String
	 */
	String getPrevNextToXML(CtsUrn requestUrn){
        StringBuffer xmlString = new StringBuffer()
		Map gpnObject = getPrevNextObject(requestUrn)
		

		xmlString.append("""
			<GetPrevNextUrn xmlns:cts="http://chs.harvard.edu/xmlns/cts" xmlns="http://chs.harvard.edu/xmlns/cts">
			""")
		xmlString.append("""
			<cts:request>
				<requestName>GetPrevNextUrn</requestName>
				<requestUrn>${requestUrn}</requestUrn>
			</cts:request>
			""")
		xmlString.append("""
			<cts:reply>
					<prevnext>
						<prev>
							<urn>${gpnObject['GetPrevNextUrn']['reply']['prev']}</urn>
						</prev>
						<next>
							<urn>${gpnObject['GetPrevNextUrn']['reply']['next']}</urn>
						</next>
					</prevnext>
				</cts:reply>
			</GetPrevNextUrn>
			""")


		// Format for CTS Reply
		// xmlString.append(gpnObject['GetPassagePlus']['reply']['passageComponent'])
		return xmlString
	}

	/**  Overloaded function. Turn a urn-string into a CTS-URN, and return
	 * a JSON fragment, by first making a getPrevNext reply Map.
	 * @returns String
	 */
	String getPrevNextToJSON(String requestUrn){
			CtsUrn urn = new CtsUrn(requestUrn)
			return getPrevNextToJSON(urn)
	}

	/**  Given a URN, constructs a getPrevNext reply as
	 * an XML fragment, by first making a getPrevNext reply Map.
	 * @returns ctsReply as Map
	 */
	String getPrevNextToJSON(CtsUrn requestUrn){
		Map gpnObject = getPrevNextObject(requestUrn)
	    return new JsonBuilder(gpnObject).toPrettyString()
	}

/* *****************************************
GetValidReff Request
***************************** */

  /**  Overloaded method. 
  * @param CtsUrn requestUrn
   * @returns ctsReply as Map
   */
   Map getValidReffObject(CtsUrn requestUrn){
		return getValidReffObject(requestUrn, null)
   }

  /**  Given a URN, constructs a getValidReff reply data-object
  * @param CtsUrn requestUrn
  * @param Integer requestLevel
   * @returns ctsReply as Map
   */
	Map getValidReffObject(CtsUrn requestUrn, Integer requestLevel){
		Integer level = requestLevel
		Map ctsReply = [:]
		Map ctsReplyObject = [:]
		Map ctsRequestMap = [:]
		Map ctsReplyMap = [:]

		CtsUrn urn = graph.resolveVersion(requestUrn)

		
		ctsRequestMap.put('request','GetValidReff')
		ctsRequestMap.put('urn',requestUrn.toString())
	
		if ((level != null) || (level > 1)){
			ctsReplyMap.put("reff",graph.getValidReff(urn,level))
		} else {
			ctsReplyMap.put("reff",graph.getValidReff(urn))
		}

		ctsReplyObject.put('request',ctsRequestMap)
		ctsReplyObject.put('reply',ctsReplyMap)	
		ctsReply.put('GetValidReff',ctsReplyObject)


		return ctsReply
	}

/**  Overloaded function. Turn a urn-string into a CTS-URN, and return
	 * an XML fragment, by first making a getValidReff reply Map.
	 * @returns String
	 */
	String getValidReffToXML(String requestUrn){
			CtsUrn urn = new CtsUrn(requestUrn)
			return getValidReffToXML(urn, null)
	}

/**  Overloaded function. Turn a urn-string into a CTS-URN, and return
	 * an XML fragment, by first making a getValidReff reply Map.
	 * @returns String
	 */
	String getValidReffToXML(CtsUrn requestUrn){
			return getValidReffToXML(requestUrn, null)
	}


	/**  Overloaded function. Turn a urn-string into a CTS-URN, and return
	 * an XML fragment, by first making a getValidReff reply Map.
	 * @returns String
	 */
	String getValidReffToXML(String requestUrn,Integer level){
			CtsUrn urn = new CtsUrn(requestUrn)
			return getValidReffToXML(urn, level)
	}
  
	/**  Given a URN, constructs a getValidReff reply as
	 * an XML fragment, by first making a getValidReffObject reply Map.
	 * @returns String
	 */
	String getValidReffToXML(CtsUrn requestUrn,Integer level){
        StringBuffer xmlString = new StringBuffer()
		Map gvrObject = getValidReffObject(requestUrn, level)
		String reff = ""
		gvrObject['GetValidReff']['reply']['reff'].each { rrr ->
			reff += "<urn>${rrr}</urn>\n"
		}
		

		xmlString.append("""
			<GetValidReff xmlns:cts="http://chs.harvard.edu/xmlns/cts" xmlns="http://chs.harvard.edu/xmlns/cts">
			""")
		xmlString.append("""
			<cts:request>
				<requestName>GetValidReff</requestName>
				<requestUrn>${requestUrn}</requestUrn>
				<level>${level}</level>
			</cts:request>
			""")
		xmlString.append("""
			<cts:reply>
					<reff>
					${reff}
					</reff>
				</cts:reply>
			</GetValidReff>
			""")


		// Format for CTS Reply
		// xmlString.append(gpnObject['GetPassagePlus']['reply']['passageComponent'])
		return xmlString
	}
	/**  Overloaded function. Turn a urn-string into a CTS-URN, and return
	 * a JSON fragment, by first making a getValidRefft reply Map.
	 * @returns String
	 */
	String getValidReffToJSON(String requestUrn,Integer level){
			CtsUrn urn = new CtsUrn(requestUrn)
			return getValidReffToJSON(urn,level)
	}

	/**  Overloaded function. Turn a urn-string into a CTS-URN, and return
	 * a JSON fragment, by first making a getValidRefft reply Map.
	 * @returns String
	 */
	String getValidReffToJSON(String requestUrn){
			CtsUrn urn = new CtsUrn(requestUrn)
			return getValidReffToJSON(urn,null)
	}

	/**  Overloaded function. Turn a urn-string into a CTS-URN, and return
	 * a JSON fragment, by first making a getValidRefft reply Map.
	 * @returns String
	 */
	String getValidReffToJSON(CtsUrn requestUrn){
			return getValidReffToJSON(requestUrn,null)
	}

	/**  Given a URN, constructs a getValidRefft reply as
	 * an XML fragment, by first making a getValidRefft reply Map.
	 * @returns ctsReply as Map
	 */
	String getValidReffToJSON(CtsUrn requestUrn,Integer level){
		Map gvrObject = getValidReffObject(requestUrn,level)
	    return new JsonBuilder(gvrObject).toPrettyString()
	}
}
