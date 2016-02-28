package edu.holycross.shot.sparqlcts


import edu.harvard.chs.cite.CtsUrn


/**
* An abstract class providing static methods for working with XPath expressions and 
* XPath templates for citation. Methods of this class can compose XML strings 
* for the opening and closing elements of well-formed XML corresponding 
* to given XPath expressions and templates.
*/
class XmlFormatter {

  /**
   * Converts an XPath expression for the ancestors of a node
   * to the opening XML markup of that node in its XML serialization.
   * N.b. We are NOT interested in the last, leaf-node element!
   * @param xpAncestor A XPath String for a citable node of a document.
   * @returns A containing XML string corresponding to the given XPath
   * expression.
   */
  static String openAncestors (String xpAncestor, String xmlNs, String xmlNsAbbr) {
    StringBuilder formatted = new StringBuilder()
    def pathParts = xpAncestor.split(/\//)

    pathParts.each { pth ->
      if (pth != "") {
	formatted.append("<" + filtersToAttrs(pth) + ">")
      }
    }

    return formatted.toString().replaceFirst(">", " xmlns:${xmlNsAbbr}='${xmlNs}'>")
  }

  /**
   * Converts an XPath expression for the ancestors of a node
   * to the CLOSING XML markup of that node in its XML serialization.
   * N.b. We are NOT interested in the last, leaf-node element!
   * @param xpAncestor A XPath String for a citable node of a document.
   * @returns A containing XML string corresponding to the given XPath
   * expression.
   */
  String trimAncestors(String xpAncestor, String xpt, Integer limit) {
	  StringBuffer formatted = new StringBuffer()
	  def pathParts = xpAncestor.split(/\//)
	  def citeIndex = citationIndices(xpt) 
	  def limitIndex = citeIndex[limit-1] //  
	  // pathMax, because we are not closing ALL the hierarchy, just the bits that need closing.
	  def pathMax = citeIndex[citeIndex.size() - 2]
	  for (i in pathMax .. limitIndex) {
		  formatted.insert(0,"<" + filtersToAttrs(pathParts[i]) + ">")
	  }
	  return formatted.toString()
  }


  /**
   * Converts an XPath expression for the ancestors of a node
   * to the closing XML markup of that node in its XML serialization.
   * @param ancestorPath An XPath expression representing the full,
   * explicit path of the nodeset's ancestors.
   * @returns A String with the XML of the closing element markup
   * of ancestor elements given in ancestorPath.
   */
  static String closeAncestors (String ancestorPath) {
    StringBuilder formatted = new StringBuilder()
    def pathParts = ancestorPath.split(/\//)
    pathParts.reverse().each {
      if (it != "") {formatted.append("</" + stripFilters(it) + ">")}
    }
    return formatted.toString()
  }


  /**
   * Converts XPath filter expressions to attribute tags for an XML document. 
   * E.g., an expression like "div[@n = '1']" becomes "div n='1'".
   * @param xpStr The XPath expression to convert.
   * @returns A String with XML corresponding to the given XPath expression.
   */
  static String  filtersToAttrs(String xpStr) {
    def returnStr = xpStr
    def emptyStr = ""
    // Hairy reg.exp. to pluck the element name and bracketed filter
    // expressions from an XPath expression:
    return xpStr.replaceAll(/([^\[]+)\[(.+)\]/) { wholePattern, elName, filter ->
      def attrs = filter.replaceAll(/@/,emptyStr)
      attrs = attrs.replaceAll(" and ", " ")
      returnStr = "${elName} ${attrs}"
    }
    return returnStr
  }	


  /** Removes all filter expressions from an XPath String.
   * @param xpStr An XPath expression, as a String, from which 
   * to remove all filter expressions.
   * @returns An XPath String with no filter expressions.
   */
  static String stripFilters(String xpStr) {
    def emptyStr = ""
    return xpStr.replaceAll(/\[[^\]]+\]/, emptyStr)
  }

  
  /** Indexes which elements in the hierarchy of an XPath are
   * citation templates.
   * @param citationTemplate The XPath template to examine.
   * @returns A list of 1-origin indices into the XPath's
   * hierarchy of document elements.
   */
  static ArrayList citationIndices(String citationTemplate) {
    def templateParts = citationTemplate.split(/\//)
    def citationIndexes = []
    def max = templateParts.size() - 1
    def startCounting = false
    for (i in 1..max) {
      if (templateParts[i] =~ /\?/) {
	startCounting = true
      }
      if (startCounting){ 
	citationIndexes.add(i)
      }
    }
    return citationIndexes
  }

  /**
   * Finds a 1-origin count of the deepest citation level at which two XPath statements
   * differ in their values for a citation scheme modelled in an XPath template.
   * The resulting value can then be used to trim wrapping XML markup to appropriate
   * depth to create well-formed XML. The two XPaths must be of the same depth.  
   * A value of 0 means the two XPaths have identical citation values.
   * Examples: XPaths corresponding to these citations would result in:
   *       citation 1     citation 2      result
   *      '1.1.1.1.5' and '1.1.1.1.5' --> 0
   *      '1.1.1.2.1' and '1.1.1.1.1' --> 4
   *      '1.1.1.1.1' and '1.1.2.1.1' --> 3
   *      '1.1.1.1.1' and '1.2.1.1.1' --> 2
   *
   * N.b. We are NOT interested in the last, leaf-node element!
   *
   * @param xp1 The first XPath to compare.
   * @param xp2 The second XPath to compare.
   * @param xpTemplate An XPath template
   * @returns 0 for identical paths, otherwise an integer indicating the 
   * level where they differ.
   * @throws Exception if two XPaths have different number of levels.
   */
  static Integer findDifferingCitationLevel ( String xp1, String xp2, String xpTemplate)
	  throws Exception {
		  def pathParts1 = xp1.split(/\//)
		  def pathParts2 = xp2.split(/\//)
		  if (pathParts1.size() != pathParts2.size()){
			  throw new Exception("XmlFormatter:findDifferingCitationLevel: xpaths not same depth ${xp1} and ${xp2}")
		  }
		  Integer differingLevel = 0

		  // Index of elements containing citation values:
		  ArrayList citeIndex = citationIndices(xpTemplate)

		  Integer firstIndex = citeIndex[0].toInteger()
		  Integer lastIndex = citeIndex[citeIndex.size() - 2].toInteger() // -2 because we are ignoring the leaf-node element.

		  Integer counter = 0
		  for (i in firstIndex .. lastIndex) {
			  if (pathParts1[i] != pathParts2[i]){
				  differingLevel = counter
					  break
			  }
			  counter++;
			  if (i == lastIndex) {
				  // then they matched all the way to the end,
				  // so are identical:
				  differingLevel = -1
			  }
		  }
		  // save 0 value for identical match, and
		  // report level as 1-origin array:
		  return differingLevel + 1
	  }
  
  /**
   * Converts an XPath expression for the ancestors of a node
   * to the closing XML markup of that node in its XML serialization,
   * down to a given number of citation levels defined in an
   * XPath template.
   * N.b. We are NOT interested in the last, leaf-node element!
   * @param xpAncestor XPath String for a citable node of a document.
   * @param xpTemplate Citation template for this document.
   * @param limit Number of citation levels to include.
   * @returns A closing XML string for well-formed containing markup.
   */
  static String trimClose(String xpAncestor, String xpTemplate, Integer limit) {
    ArrayList pathParts = xpAncestor.split(/\//)
    ArrayList templateParts = xpTemplate.split(/\//)
    if (pathParts.size() != (templateParts.size()-1)){
	throw new Exception("XmlFormatter:trimClose: xpath ${xpAncestor} not same depth as template ${xpTemplate}")
    }
    if (limit < 1) {
      throw new Exception("XmlFormatter:trimClose: must include at least one citation level (limit requested was ${limit}")
    }

    StringBuilder formatted = new StringBuilder()
    ArrayList citationElements = citationIndices(xpTemplate)

    if (limit > citationElements.size()) {
      throw new Exception("XmlFormatter:trimClose: ${limit} levels requested, but only ${citationElements.size()} citation elements in this scheme.")
    }
    
	// pathMax is because we are not closing the whoe hierarchy up to the root node, but only the ones that need closing.
	Integer pathMax = citationElements[citationElements.size() - 2]
    // limit - 1 because values of citationElements are 1-origin,
    // but we're working with 0-origin arrays
    Integer firstIndex = citationElements[limit - 1].toInteger()
    
    for (i in firstIndex .. pathMax )  {
      formatted.append("</" + stripFilters(templateParts[i]) + ">")
    }
    return formatted.toString()
  }

	/**  Crazy complicated invokating on XmlFormatter, to 
	* ensure a well-formed XML fragment. Highly implementation-specific.
	* Fragile as hell. Don't mess withis lightly.
	 * @returns String
	 */
	String buildXmlFragment(ArrayList leafNodes){
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
						citeDiffLevel = findDifferingCitationLevel(b['typeExtras']['anc'], currentWrapper, b['typeExtras']['xpt'])
						if (firstNode) {
								firstNode = false
								passageString.append(openAncestors(b['typeExtras']['anc'],b['typeExtras']['xmlns'],b['typeExtras']['xmlnsabbr']))
						} else  {
								if (citeDiffLevel < 0){
								passageString.append(trimClose(b['typeExtras']['anc'], currentXpt,1))
								passageString.append(trimAncestors(b['typeExtras']['anc'], b['typeExtras']['xpt'], 1))
								} else {
								// We might need to change 'b.xpt?.value' in the line below to 'currentXpt'
								passageString.append(trimClose(b['typeExtras']['anc'], b['typeExtras']['xpt'],citeDiffLevel))
								passageString.append(trimAncestors(b['typeExtras']['anc'], b['typeExtras']['xpt'], citeDiffLevel))
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
				passageString.append(closeAncestors(currentWrapper))

		} else {
			leafNodes.each { b ->
				passageString.append("${b['rangeNode']['textContent']}\n\n")
			}
		}
	
		return passageString	

    }
  
}
