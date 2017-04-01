package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
//import edu.holycross.shot.citeservlet.Sparql
//import edu.holycross.shot.sparqlcts.CtsGraph


class TestGetRangeForContextIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)


  @Test
  void test1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.5")
	Integer context = 1
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.4-1.6"
    assert  graph.getRangeForContext(urn,context).toString() == expectedPrev
  }

  @Test
  void test2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:2")
	Integer context = 1
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10-3.1"
    assert  graph.getRangeForContext(urn,context).toString() == expectedPrev
  }
  @Test
  void test3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1")
	Integer context = 1
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1-2.1"
    assert  graph.getRangeForContext(urn,context).toString() == expectedPrev
  }
  @Test
  void test4() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10")
	Integer context = 3
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.7-2.3"
    assert  graph.getRangeForContext(urn,context).toString() == expectedPrev
  }
  @Test
  void test5() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:3.10")
	Integer context = 3
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.7-3.10"
    assert  graph.getRangeForContext(urn,context).toString() == expectedPrev
  }
  @Test
  void test6() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:3")
	Integer context = 3
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen:2.8-3.10"
    assert  graph.getRangeForContext(urn,context).toString() == expectedPrev
  }




}
