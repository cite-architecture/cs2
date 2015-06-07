package edu.holycross.shot.sparqlcts


import edu.harvard.chs.cite.CtsUrn


/**
* An abstract class providing static methods for working with XPath expressions and 
* XPath templates for citation. Methods of this class can compose XML strings 
* for the opening and closing elements of well-formed XML corresponding 
* to given XPath expressions and templates.
*/
abstract class XmlFormatter {

  /**
   * Converts an XPath expression for the ancestors of a node
   * to the opening XML markup of that node in its XML serialization.
   * @param xpAncestor A XPath String for a citable node of a document.
   * @returns A containing XML string corresponding to the given XPath
   * expression.
   */
  static String openAncestors (String xpAncestor, String xmlNs) {
    StringBuilder formatted = new StringBuilder()
    def pathParts = xpAncestor.split(/\//)

    pathParts.each { pth ->
      if (pth != "") {
	formatted.append("<" + filtersToAttrs(pth) + ">")
      }
    }
    println "INSERT " + xmlNs + " into current formatted " + formatted.toString()


    return formatted.toString().replaceFirst(">", " ${xmlNs}>")
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
      Integer lastIndex = citeIndex[citeIndex.size() - 1].toInteger()

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
   * to the opening XML markup of that node in its XML serialization
   * down to a given number of citation levels defined in an
   * XPath template.
   * @param xpAncestor XPath String for a citable node of a document.
   * @param xpTemplate Citation template for this document.
   * @param limit Number of citation levels to include.
   * @returns An opening XML string for well-formed containing markup.
   */
  static String trimOpen(String xpAncestor, String xpTemplate, Integer limit) {
    ArrayList pathParts = xpAncestor.split(/\//)
    ArrayList templateParts = xpTemplate.split(/\//)
    if (pathParts.size() != templateParts.size()){
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

    // limit - 1 because values of citationElements are 1-origin,
    // but we're working with 0-origin arrays
    Integer firstIndex = citationElements[limit - 1].toInteger()
    for (i in 1 .. firstIndex)  {
      formatted.append("<" + filtersToAttrs(pathParts[i]) + ">")
    }
    return formatted.toString()
  }





  
  /**
   * Converts an XPath expression for the ancestors of a node
   * to the closing XML markup of that node in its XML serialization,
   * down to a given number of citation levels defined in an
   * XPath template.
   * @param xpAncestor XPath String for a citable node of a document.
   * @param xpTemplate Citation template for this document.
   * @param limit Number of citation levels to include.
   * @returns A closing XML string for well-formed containing markup.
   */
  static String trimClose(String xpAncestor, String xpTemplate, Integer limit) {
    ArrayList pathParts = xpAncestor.split(/\//)
    ArrayList templateParts = xpTemplate.split(/\//)
    if (pathParts.size() != templateParts.size()){
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
    


    // limit - 1 because values of citationElements are 1-origin,
    // but we're working with 0-origin arrays
    Integer firstIndex = citationElements[limit - 1].toInteger()
    println "Limit " + limit + " is at 0-origin element " + firstIndex
    
    for (i in firstIndex .. 1 )  {
      println "Element ${i} " + templateParts[i]
      formatted.append("</" + stripFilters(templateParts[i]) + ">")
    }
    return formatted.toString()
  }
  
}
