package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestPrevIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)


  @Test
  void testPrev() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.2")
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    assert  graph.getPrevUrnString(urn) == expectedPrev
  }

  @Test
  void testPrevRange() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.2-1.3")
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    assert  graph.getPrevUrnString(urn) == expectedPrev
  }

  @Test
  void testPrevRange_work() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.2-1.3")
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    assert  graph.getPrevUrnString(urn) == expectedPrev
  }

  @Test
  void testPrevRange_translation() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest:1.2-1.3")
    String expectedPrev = "urn:cts:greekLit:tlg0016.tlg001.engTest:1.1"
    assert  graph.getPrevUrnString(urn) == expectedPrev
  }

  @Test
  void testPrevRange_containers() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:2-3")
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10"
    assert  graph.getPrevUrnString(urn) == expectedPrev
  }

  @Test
  void testPrev_first() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    String expectedPrev = ""
    assert  graph.getPrevUrnString(urn) == expectedPrev
  }

  @Test
  void testPrev_first2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1")
    String expectedPrev = ""
    assert  graph.getPrevUrnString(urn) == expectedPrev
  }


  @Test
  void testPrev_exemplar1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.1.2")
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.1.1"
    assert  graph.getPrevUrnString(urn) == expectedPrev
  }

  @Test
  void testPrev_exemplar2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:2.1")
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.10.10"
    assert  graph.getPrevUrnString(urn) == expectedPrev
  }

  @Test
  void testPrev_exemplar3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:2")
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.10.10"
    assert  graph.getPrevUrnString(urn) == expectedPrev
  }


  @Test
  void testNulls() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    
    assert  graph.getPrevUrn(urn) == null
    assert  graph.getPrevUrnString(urn) == ""


  }

  
  
}