package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestDescrsIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
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
    String expectedLabel = "Iliad (urn:cts:greekLit:tlg0012.tlg001:)."
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")

    println "For ${urn}: " + actualLabel
  }

  @Test
  void testJustGroupDescr() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012:")
    String expectedLabel = "Homeric Epic (urn:cts:greekLit:tlg0012:)."
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")

    println "For ${urn}: " + actualLabel
  }

  @Test
  void testRangeDescr1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-1.3")
    String expectedLabel = "Range request: 1.1-1.3, from Iliad. (urn:cts:greekLit:tlg0012.tlg001:1.1-1.3)."
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")

    println "For ${urn}: " + actualLabel
  }

  @Test
  void testRangeDescr2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1-1.3")
    String expectedLabel = "Range request: 1.1-1.3, from Allen's Iliad (test ed.). (urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1-1.3)."
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")

    println "For ${urn}: " + actualLabel
  }

  @Test
  void testContainerDescr1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1")
    String expectedLabel = "Containing-element request: 1, from Allen's Iliad (test ed.). (urn:cts:greekLit:tlg0012.tlg001.testAllen:1)."
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")

    println "For ${urn}: " + actualLabel
  }

  @Test
  void testContainerDescr2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1")
    String expectedLabel = "Containing-element request: 1, from Iliad. (urn:cts:greekLit:tlg0012.tlg001:1)."
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")

    println "For ${urn}: " + actualLabel
  }

  @Test
  void testExemplarDescr1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:")
    String expectedLabel = "Allen. Iliad. Greek. 3 books of 10 lines. Word-tokens wrapped and citable. (urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:)."
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")

    println "For ${urn}: " + actualLabel
  }

  @Test
  void testExemplarDescr2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.1.1")
    String expectedLabel = "Homeric Epic, Iliad (Allen. Iliad. Greek. 3 books of 10 lines. Word-tokens wrapped and citable.): 1.1.1 (urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.1.1)"
    String actualLabel = graph.getLabel(urn)
    assert actualLabel.replaceAll("\\s","") == expectedLabel.replaceAll("\\s","")

    println "For ${urn}: " + actualLabel
  }


}
