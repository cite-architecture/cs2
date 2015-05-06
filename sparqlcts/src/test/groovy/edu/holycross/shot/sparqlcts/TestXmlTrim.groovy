package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn

class TestXmlTrim extends GroovyTestCase {


  @Test
  void testTrimOpen() {
    String xpTemplate = "/tei:TEI/tei:text/tei:body/tei:div[@n = '?']/tei:div[@n = '?']/tei:p[@n = '?']"
    String openXPath = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:div[@n = '1']/tei:p[@n = '1']"

    // Just right. Trim to 1 citation level:
    String expectedOneLevel = "<tei:TEI><tei:text><tei:body><tei:div n = '1'>"
    assert  XmlFormatter.trimOpen(openXPath, xpTemplate, 1) == expectedOneLevel



    // Just right. Trim to 2 citation levels:
    String expectedTwoLevel = "<tei:TEI><tei:text><tei:body><tei:div n = '1'><tei:div n = '1'>"
    assert  XmlFormatter.trimOpen(openXPath, xpTemplate, 2) == expectedTwoLevel


    // Just right. Trim to 3 citation levels:
    String expectedThreeLevel = "<tei:TEI><tei:text><tei:body><tei:div n = '1'><tei:div n = '1'><tei:p n = '1'>"
    assert  XmlFormatter.trimOpen(openXPath, xpTemplate, 3) == expectedThreeLevel


  }


  

  @Test
  void testTrimClose() {
    String xpTemplate = "/tei:TEI/tei:text/tei:body/tei:div[@n = '?']/tei:div[@n = '?']/tei:p[@n = '?']"
    String openXPath = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:div[@n = '1']/tei:p[@n = '1']"
    String expectedOneLevel = "</tei:div></tei:body></tei:text></tei:TEI>"
    
    // too few levels
    assert  shouldFail {
      XmlFormatter.trimClose(openXPath, xpTemplate, 0) == expectedOneLevel
    }

    // too many levels
    assert  shouldFail {
      XmlFormatter.trimClose(openXPath, xpTemplate, 7) == expectedOneLevel
    }

    

    // Just right. Trim to 1 citation level:

    assert  XmlFormatter.trimClose(openXPath, xpTemplate, 1) == expectedOneLevel


    // Trim to 2 citation levels:
    String expectedTwoLevels = "</tei:div></tei:div></tei:body></tei:text></tei:TEI>"
    assert  XmlFormatter.trimClose(openXPath, xpTemplate, 2) == expectedTwoLevels


    // Trim to 3 citation levels:
    String expectedThreeLevels = "</tei:p></tei:div></tei:div></tei:body></tei:text></tei:TEI>"
    assert  XmlFormatter.trimClose(openXPath, xpTemplate, 3) == expectedThreeLevels

    // XPath too short
    String tooShort = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:div[@n = '1']"
    assert shouldFail {
      assert  XmlFormatter.trimClose(tooShort, xpTemplate, 1) == expectedThreeLevels
    }


    // XPath too long
    String tooLong = "/tei:TEI/tei:text/tei:body/tei:div[@n = '1']/tei:div[@n = '1']/tei:div[@n = '1']/tei:div[@n = '1']/tei:div[@n = '1']"
    assert shouldFail {
      assert  XmlFormatter.trimClose(tooLong, xpTemplate, 1) == expectedThreeLevels
    }


  }


  
}