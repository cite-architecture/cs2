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

  @Test
  void testDifferingLevels() {
    String xpTemplate = "/tei:TEI/tei:text/tei:body/tei:div[@n = '?']/tei:l[@n = '?']"
    String xp1 = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:l[@n = '1']"

    // every XPath matches itself:
    assert XmlFormatter.findDifferingCitationLevel(xp1, xp1, xpTemplate) == 0

    String xp2 = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:l[@n = '2']"
    // xp1 and xp2 differ on the second citation value:
    assert XmlFormatter.findDifferingCitationLevel(xp1, xp2, xpTemplate) == 2

    String xp3 = "/tei:TEI/tei:text/tei:body/tei:div[@n = '2']/tei:l[@n = '1']"
    // xp1 and xp3 differ on the first citation value:
    assert XmlFormatter.findDifferingCitationLevel(xp1, xp3, xpTemplate) == 1


    String xp4 = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']"
    // xp1 and xp4 are not the same depth:
    assert shouldFail {
      assert XmlFormatter.findDifferingCitationLevel(xp1, xp4, xpTemplate) == 1
    }
    
    
  }

  
}