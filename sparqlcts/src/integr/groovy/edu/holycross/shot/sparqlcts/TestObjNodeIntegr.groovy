package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestObjNodeIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)

  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
  CtsUrn wkLevelUrn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
  

  @Test
  void testLeafObject() {
    Ohco2Node onode = graph.getOhco2Node(urn)
    String expectedContent = """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="1">Μῆνιν ἄειδε θεὰ Πηληϊάδεω Ἀχιλῆος </l>"""
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.2"
    CtsUrn expectedUrn = urn
	
    groovy.util.Node expectedNode = new XmlParser().parseText(expectedContent)
    groovy.util.Node actualNode = new XmlParser().parseText(onode.leafNodes[0]['rangeNode'].textContent)
    assert actualNode // should be well-formed XML
    

    assert onode.leafNodes[0]['rangeNode'].textContent == expectedContent
    assert onode.prevUrn ==  null
    assert onode.nextUrn.toString() == expectedNext
    }

    @Test
  void testNotionalWorkLeaf() {
    Ohco2Node onode = graph.getOhco2Node(wkLevelUrn)
    String expectedContent = """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="1">Μῆνιν ἄειδε θεὰ Πηληϊάδεω Ἀχιλῆος </l>"""
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.2"
    CtsUrn expectedUrn = urn


    groovy.util.Node expectedNode = new XmlParser().parseText(expectedContent)
    groovy.util.Node actualNode = new XmlParser().parseText(onode.leafNodes[0]['rangeNode'].textContent)
    assert actualNode // should be well-formed XML
    

    assert onode.leafNodes[0]['rangeNode'].textContent == expectedContent
    assert onode.prevUrn ==  null
    assert onode.nextUrn.toString() == expectedNext

  }
  
}
