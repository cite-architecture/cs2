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


}