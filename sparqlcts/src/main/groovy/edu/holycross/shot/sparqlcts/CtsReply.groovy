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
		String xmlString = "holding"
			Map gppObject = getPassagePlusReply(requestUrn)
			xmlString = getFillText(gppObject['GetPassagePlus']['reply']['rangeNodesMap'])
			return xmlString
	}

	/**  Crazy complicated invokating on XmlFormatter, to 
	* ensure a well-formed XML fragment. Highly implementation-specific.
	* Fragile as hell. Don't mess withis lightly.
	 * @returns String
	 */
	String getFillText(ArrayList leafNodes){
        StringBuffer passageString = new StringBuffer()

		// Check for XML
		Boolean properXML = true
		leafNodes.each{ b ->
			properXML = (properXML && (b['typeExtras']['type'] == 'xml'))
			properXML = (properXML && (b['typeExtras']['anc']))
			properXML = (properXML && (b['typeExtras']['nxt']))
			properXML = (properXML && (b['typeExtras']['xpt']))
		}

		if (properXML){

		//	String currentWrapper = leafNodes[0]['typeExtras']['anc']
			String currentWrapper = leafNodes[0]['typeExtras']['anc']
			// We need to grab this, because when texts have different sections with different depths,
			// we can't count on the next section having the same structure as the previous one.
			String currentXpt = "" 
			String currentNext = ""
			def citeDiffLevel = 0
			String tempText = ""
			Boolean firstNode = true

			leafNodes.each { b ->

				if (b['typeExtras']['nxt'] != currentNext){
					
					currentNext = b['typeExtras']['nxt']
					if ((b['typeExtras']['anc'] != currentWrapper)||(firstNode)) {
						citeDiffLevel = formatter.findDifferingCitationLevel(b['typeExtras']['anc'], currentWrapper, b['typeExtras']['xpt'])
						if (firstNode) {
								firstNode = false
								passageString.append(formatter.openAncestors(b['typeExtras']['anc'],b['typeExtras']['xmlns'],b['typeExtras']['xmlnsabbr']))
						} else  {
								if (citeDiffLevel < 0){
								passageString.append(formatter.trimClose(b['typeExtras']['anc'], currentXpt,1))
								passageString.append(formatter.trimAncestors(b['typeExtras']['anc'], b['typeExtras']['xpt'], 1))
								} else {
								// We might need to change 'b.xpt?.value' in the line below to 'currentXpt'
								passageString.append(formatter.trimClose(b['typeExtras']['anc'], b['typeExtras']['xpt'],citeDiffLevel))
								passageString.append(formatter.trimAncestors(b['typeExtras']['anc'], b['typeExtras']['xpt'], citeDiffLevel))
								}
						}
						currentWrapper = b['typeExtras']['anc']
						currentXpt = b['typeExtras']['xpt']
					}
                    if (b['rangeNode']['textContent']) {
						//Here we are going to wrap the leaf-node in an element, 
						//with the URN as an attribute
						tempText = """<cts:node urn="${b['rangeNode']['nodeUrn']}">${b['rangeNode']['textContent']}</cts:node>"""
						
						//passageString.append(b.txt?.value)
						passageString.append(tempText)
                    }
				}
			}
				passageString.append(formatter.closeAncestors(currentWrapper))

		} else {
			leafNodes.each { b ->
				passageString.append("${b['rangeNode']['textContent']}\n\n")
			}
		}

		
	
		return passageString	

    }

  
}
