package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn

class TestXmlFormatter extends GroovyTestCase {


  @Test
  void testOpen() {
    String openXPath = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']"
    String expectedXml = "<tei:TEI><tei:text><tei:body><tei:div n = '1'>"
    // conversion of XPath -> XML works:
    assert  XmlFormatter.openAncestors(openXPath) == expectedXml
  }

  @Test
  void testCitationIndexing() {
    String xpTemplate = "/tei:TEI/tei:text/tei:body/tei:div[@n = '?']/tei:l[@n = '?']"
    // citation patterns are in 4th and 5th elements:
    ArrayList expectedIndices = [4,5]
    assert XmlFormatter.citationIndices(xpTemplate) == expectedIndices
  }

}