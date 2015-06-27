package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestPrevNextNotionalIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)



  @Test
  void testNotionalPrev() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.2")
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.msA:1.1"
    assert  graph.getPrevUrnString(urn).toString() == expectedPrev
  }



  @Test
  void testNotionalNext() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.2")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.msA:1.3"
    assert  graph.getNextUrnString(urn).toString() == expectedNext
  }



  @Test
  void testNotionalNulls() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.msA:1.2"
    
    assert  graph.getNextUrnString(urn) == expectedNext
    assert  graph.getPrevUrn(urn) == null
    assert  graph.getPrevUrnString(urn).toString() == ""


  }

  
  
}
