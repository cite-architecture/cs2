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
  static String openAncestors (String xpAncestor) {
    StringBuilder formatted = new StringBuilder()
    def pathParts = xpAncestor.split(/\//)
    pathParts.each {
      if (it != "") {  formatted.append("<" + filtersToAttrs(it) + ">") }
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
   * Find the deepest (right-most) level at which two XPath statements differ
   * in their values for a citation scheme modelled in an XPath template.
   * The two XPaths must be of the same depth.  A value of 0 means
   * the two XPaths have identical citation values.
   *
   * E.g. XPaths for these citation values would result in:
   *      XPath for 1     XPath for 2     Result
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
  static Integer levelDiff ( String xp1, String xp2, String xpTemplate)
    throws Exception {
      def pathParts1 = xp1.split(/\//)
      def pathParts2 = xp2.split(/\//)

      if (pathParts1.size() != pathParts2.size()){
	throw new Exception("XmlFormatter:levelDiff: xpaths not same depth ${xp1} and ${xp2}")
      }


      String temp1 = ""
      String temp2 = ""
      Integer counter
      def retVal = 0
      
      /*      
      def citeIndex = citationIndices(xpTemplate)
      Integer howMany = citeIndex.size()
      Integer lastIndex = citeIndex[howMany - 1].toInteger()
      Integer firstIndex = citeIndex[0].toInteger()
      lastIndex--
      firstIndex--
      counter = 0
      for (i in firstIndex .. lastIndex){
	if (pathParts1[i] != pathParts2[i]){
	  retVal = counter
	  break
	}
	counter++
	  }
      if (counter > lastIndex){ retVal = 0 }
      }
      return retVal
      */
    }




   String trimAncestors(String xpAncestor, String xpt, Integer limit) {
    StringBuffer formatted = new StringBuffer()
    def pathParts = xpAncestor.split(/\//)
    def citeIndex = citationIndices(xpt) 
    def limitIndex = citeIndex[limit-1] //  
    def pathMax = citeIndex[citeIndex.size() - 2]
    for (i in pathMax .. limitIndex) {
      formatted.insert(0,"<" + filtersToAttrs(pathParts[i]) + ">")
    }
    return formatted.toString()
  }

  
  
  String trimClose(String xpAncestor, String xpt, Integer limit) {
    if (limit < 1) { limit = 1 }
    StringBuffer formatted = new StringBuffer()
    def pathParts = xpt.split(/\//)
    def citeIndex = citationIndices(xpt)
    if (limit >= citeIndex.size()){ limit = (citeIndex.size() - 1) }
    def limitIndex = citeIndex[limit-1]
    def pathMax = citeIndex[citeIndex.size() - 2]
    for (i in pathMax .. limitIndex) {
      formatted.append("</" + stripFilters(pathParts[i]) + ">")
    }
    return formatted.toString()
  }
  




  /** Converts an XPath expression for the ancestors of a node
   * to the opening XML markup of that node in the XML serialization of 
   * a citable node of text, limited to a specified level of the
   * citation hierarchy.
   * @param ancestorPath A String specifying an XPath with citation values 
   * for a citable node of a document.
   * @param citationTemplate An XPath String with placeholders for citation 
   * values marked by question marks.
   * @returns A String with the XML of the opening element markup
   * of ancestor elements given in xpTemplate.
   * @limit Number of levels of the citation scheme that the resulting 
   * XML should be limited to.
   * @throws Exception if limit is greater than the number of levels
   * of the citation scheme, or if limit is negative.
   */
  String openAncestors (String ancestorPath, String citationTemplate, int limit) 
  throws Exception {
    StringBuffer formatted = new StringBuffer()
    def citationIndexes = citationIndices(citationTemplate)
    // validate limit parameter:
    if (limit == citationIndexes.size() ) {
      // valid request:  but needs no wrapper, just the
      // citation node
      return "" 

    } else if (limit == 0) {
      // use whole ancestor path:
      return openAncestors(ancestorPath)

    } else  if (limit > citationIndexes.size() -1) {
      // invalid: requested limit > number of citation elements!
      throw new Exception("XmlFormatter exception:  citation template has smaller size (${citationIndexes.size()}) than requested limit (${limit})")
      
    } else if (limit < 0) {
      throw new Exception("XmlFormatter exception:  requested limit (${limit}) cannot be negative.")
    }
    
    def limitIndex = citationIndexes[limit-1]
    // The citation template includes the leaf node, which we want to skip,
    // so the maximum index of the last element to look it will *not*
    // be size() - 1, but size() - 2
    def pathMax = citationIndexes[citationIndexes.size() - 2]
    def pathParts = ancestorPath.split(/\//)
    for (i in pathMax .. limitIndex) {
      formatted.insert(0, "<" + filtersToAttrs(pathParts[i]) + ">")
    }
    return formatted.toString()
  }



  /**
   * @param ancestorPath An XPath expression representing the full,
   * explicit path of the nodeset's ancestors.
   * @returns A String with the XML of the closing element markup
   * of ancestor elements given in ancestorPath
   */
  String closeAncestors (String ancestorPath) {
    StringBuffer formatted = new StringBuffer()
    //println "AP: ${ancestorPath}"

    def pathParts = ancestorPath.split(/\//)
    pathParts.reverse().each {
      if (it != "") {formatted.append("</" + stripFilters(it) + ">")}
    }
    return formatted.toString()
  }


  String closeAncestors (String ancestorPath, String citationTemplate, int limit) 
  throws Exception {
    StringBuffer formatted = new StringBuffer()
    def citationIndexes = citationIndices(citationTemplate)
    // validate limit parameter:
    if (limit == citationIndexes.size() ) {
      // valid request:  but needs no wrapper, just the
      // citation node
      return "" 
      
    } else if (limit == 0) {
      // use whole ancestor path:
      return closeAncestors(ancestorPath)

    } else  if (limit > citationIndexes.size() -1) {
      // invalid: requested limit > number of citation elements!
      throw new Exception("XmlFormatter exception:  citation template has smaller size (${citationIndexes.size()}) than requested limit (${limit})")

    } else if (limit < 0) {
      throw new Exception("XmlFormatter exception:  requested limit (${limit}) cannot be negative.")
    }

    def limitIndex = citationIndexes[limit-1]
    // The citation template includes the leaf node, which we want to skip,
    // so the maximum index of the last element to look it will *not*
    // be size() - 1, but size() - 2
    def pathMax = citationIndexes[citationIndexes.size() - 2]
    def pathParts = ancestorPath.split(/\//)
    for (i in pathMax..limitIndex) {
      formatted.append( "</" + stripFilters(pathParts[i]) + ">")
    }

    return formatted.toString()
  }



  /** Removes all filter expressions from an XPath String.
   * @param xpStr An XPath expression, as a String, from which 
   * to remove all filter expressions.
   * @returns An XPath String with no filter expressions.
   */
  String stripFilters(String xpStr) {
    def emptyStr = ""
    return xpStr.replaceAll(/\[[^\]]+\]/, emptyStr)
  }

}
