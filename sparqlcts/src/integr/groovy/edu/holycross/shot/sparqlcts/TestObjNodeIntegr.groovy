package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestObjNodeIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)

  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
  CtsUrn wkLevelUrn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
  

  @Test
  void testLeafObject() {
    Ohco2Node onode = graph.getLeafNodeObject(urn)
    String expectedContent = """<tei:TEI  xmlns:tei='http://www.tei-c.org/ns/1.0' ><tei:text><tei:body><tei:div n = '1'><l xmlns="http://www.tei-c.org/ns/1.0" n="1"> Μῆνιν ἄειδε θεὰ <persName n="urn:cite:hmt:pers.pers1"> Πηληϊάδεω Ἀχιλῆος</persName></l></tei:div></tei:body></tei:text></tei:TEI>"""
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.msA:1.2"
    CtsUrn expectedUrn = null


    groovy.util.Node expectedNode = new XmlParser().parseText(expectedContent)
    groovy.util.Node actualNode = new XmlParser().parseText(onode.textContent)
    assert actualNode // should be well-formed XML
    

    assert onode.textContent == expectedContent
    assert onode.prevUrn ==  expectedUrn
    assert onode.nextUrn.toString() == expectedNext
    }

    @Test
  void testNotionalWorkLeaf() {
    Ohco2Node onode = graph.getLeafNodeObject(wkLevelUrn)
    String expectedContent = """<tei:TEI  xmlns:tei='http://www.tei-c.org/ns/1.0' ><tei:text><tei:body><tei:div n = '1'><l xmlns="http://www.tei-c.org/ns/1.0" n="1"> Μῆνιν ἄειδε θεὰ <persName n="urn:cite:hmt:pers.pers1"> Πηληϊάδεω Ἀχιλῆος</persName></l></tei:div></tei:body></tei:text></tei:TEI>"""
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.msA:1.2"
    CtsUrn expectedUrn = null


    groovy.util.Node expectedNode = new XmlParser().parseText(expectedContent)
    groovy.util.Node actualNode = new XmlParser().parseText(onode.textContent)
    assert actualNode // should be well-formed XML
    

    assert onode.textContent == expectedContent
    assert onode.prevUrn ==  expectedUrn
    assert onode.nextUrn.toString() == expectedNext

  }
  
}
