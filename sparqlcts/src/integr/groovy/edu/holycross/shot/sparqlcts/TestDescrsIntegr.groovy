package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestDescrsIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)


  

  @Test
  void testVersionDescr() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    String expectedLabel = "Homeric Epic, Iliad (Allen's Iliad (test ed.)): 1.1 (urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1)"
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")
    
  }

  @Test
  void testWorkDescr() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    String expectedLabel = "Homeric Epic, Iliad (Allen's Iliad (test ed.)): 1.1 (urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1)"
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")

    //println "For ${urn}: " + actualLabel
  }

  @Test
  void testJustWorkDescr() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    String expectedLabel = "Homeric Epic, Iliad (Allen's Iliad (test ed.)): 1.1 (urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1)"
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")

    println "For ${urn}: " + actualLabel
  }

  @Test
  void testRangeDescr1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-1.3")
    String expectedLabel = "Homeric Epic, Iliad (Allen's Iliad (test ed.)): 1.1 (urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1)"
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")
	assert false

    println "For ${urn}: " + actualLabel
  }

  @Test
  void testRangeDescr2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1-1.3")
    String expectedLabel = "Homeric Epic, Iliad (Allen's Iliad (test ed.)): 1.1-1.3 (urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1)"
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")
	assert false

    println "For ${urn}: " + actualLabel
  }



}
