package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn

class TestXmlFormatter extends GroovyTestCase {


  @Test
  void testOpen() {
    String openXPath = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']"
    String xmlNs = "http://www.tei-c.org/ns/1.0"
	String xmlNsAbbr = "tei"
    String expectedXml = "<tei:TEI xmlns:tei='http://www.tei-c.org/ns/1.0'><tei:text><tei:body><tei:div n = '1'>"
    // conversion of XPath -> XML works:
    assert  XmlFormatter.openAncestors(openXPath, xmlNs, xmlNsAbbr) == expectedXml
  }


  @Test
  void testClose() {
    String openXPath = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']"
    String expectedXml = "</tei:div></tei:body></tei:text></tei:TEI>"
    // conversion of XPath -> XML works:
    assert  XmlFormatter.closeAncestors(openXPath) == expectedXml
  }

  @Test
  void testClose2() {
    String openXPath = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:l[@n = '2']"
    String expectedXml = "</tei:l></tei:div></tei:body></tei:text></tei:TEI>"
    // conversion of XPath -> XML works:
    assert  XmlFormatter.closeAncestors(openXPath) == expectedXml
  }

  @Test
  void testTrimClose2(){
		String xpAncestor = "/tei:TEI/tei:text/tei:body/tei:div[@n='2']/tei:l[@n='5']"
		String xpTemplate = "/tei:TEI/tei:text/tei:body/tei:div[@n='?']/tei:l[@n='?']/tei:w[@n='?']"
		Integer limit = 2
		String expected = "</tei:l>"
		assert XmlFormatter.trimClose(xpAncestor, xpTemplate, limit) == expected	
  }
  @Test
  void testTrimClose3(){
		String xpAncestor = "/tei:TEI/tei:text/tei:body/tei:div[@n='2']"
		String xpTemplate = "/tei:TEI/tei:text/tei:body/tei:div[@n='?']/tei:l[@n='?']"
		Integer limit = 1
		String expected = "</tei:div>"
		assert XmlFormatter.trimClose(xpAncestor, xpTemplate, limit) == expected	
  }
  @Test
  void testTrimClose4(){
		String xpAncestor = "/tei:TEI/tei:text/tei:body/tei:div[@n='2']/tei:l[@n='1']"
		String xpTemplate = "/tei:TEI/tei:text/tei:body/tei:div[@n='?']/tei:l[@n='?']/tei:w[@n='?']"
		Integer limit = 1
		String expected = "</tei:l></tei:div>"
		assert XmlFormatter.trimClose(xpAncestor, xpTemplate, limit) == expected	

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
    String xpTemplate = "/tei:TEI/tei:text/tei:body/tei:div[@n = '?']/tei:div[@n = '?']/tei:div[@n = '?']/tei:l[@n = '?']"
    String xp1 = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:div[@n = '1']/tei:div[@n = '1']/tei:l[@n = '1']"

    // every XPath matches itself:
    assert XmlFormatter.findDifferingCitationLevel(xp1, xp1, xpTemplate) == 0

    String xp2 = "/tei:TEI/tei:text/tei:body/tei:div[@n = 'X']/tei:div[@n = '1']/tei:div[@n = '1']/tei:l[@n = '1']"
    // xp1 and xp2 differ on the second citation value:
    assert XmlFormatter.findDifferingCitationLevel(xp1, xp2, xpTemplate) == 1

    String xp3 = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:div[@n = 'X']/tei:div[@n = '1']/tei:l[@n = '1']"
    // xp1 and xp3 differ on the second citation value:
    assert XmlFormatter.findDifferingCitationLevel(xp1, xp3, xpTemplate) == 2

    String xp4 = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:div[@n = '1']/tei:div[@n = 'X']/tei:l[@n = '1']"
    // xp1 and xp3 differ on the third citation value:
    assert XmlFormatter.findDifferingCitationLevel(xp1, xp4, xpTemplate) == 3

    String xp5 = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:div[@n = '1']/tei:div[@n = '1']/tei:l[@n = 'X']"
    // xp1 and xp3 differ on the last citation value, BUT WE DON'T CARE!!!:
    assert XmlFormatter.findDifferingCitationLevel(xp1, xp5, xpTemplate) == 0

    String xpX = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:div[@n = '1']/tei:div[@n = '1']"
    // xp1 and xp4 are not the same depth:
    assert shouldFail {
      assert XmlFormatter.findDifferingCitationLevel(xp1, xpX, xpTemplate) == 1
    }
    
  }

  @Test
  void testCitationIndexing2() {
    String xpTemplate = "/tei:TEI/tei:text/tei:body/tei:div[@n = '?']/tei:l[@n = '?']/tei:w[@n = '?']"
    // citation patterns are in 4th and 5th elements:
    ArrayList expectedIndices = [4,5,6]
    assert XmlFormatter.citationIndices(xpTemplate) == expectedIndices
  }

  @Test
  void testDifferingLevels2() {
    String xpTemplate = "/tei:TEI/tei:text/tei:body/tei:div[@n = '?']/tei:l[@n = '?']/tei:w[@n = '?']"
    String xp1 = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:l[@n = '10']/tei:w[@n = '10']"

    // every XPath matches itself:
    assert XmlFormatter.findDifferingCitationLevel(xp1, xp1, xpTemplate) == 0

    String xp2 = "/tei:TEI/tei:text/tei:body/tei:div[@n = 'X']/tei:l[@n = '10']/tei:w[@n = '10']"
    // xp1 and xp2 differ on the second citation value:
    assert XmlFormatter.findDifferingCitationLevel(xp1, xp2, xpTemplate) == 1

    String xp3 = "/tei:TEI/tei:text/tei:body/tei:div[@n = '2']/tei:l[@n = '1']/tei:w[@n = '1']"
    // xp1 and xp2 differ on the second citation value:
    assert XmlFormatter.findDifferingCitationLevel(xp1, xp2, xpTemplate) == 1
    
  }

  @Test
  void testOneLevelStuff(){
    String xpTemplate = "/tei:TEI/tei:text/tei:body/tei:div/tei:l[@n = '?']"
  }
  
}
