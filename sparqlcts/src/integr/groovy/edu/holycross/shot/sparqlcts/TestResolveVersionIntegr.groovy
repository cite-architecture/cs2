package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestResolveVersionIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)



  @Test
  void testResolveNotional() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    String expectedUrn = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    assert  graph.resolveVersion(urn).toString() == expectedUrn
  }

  @Test
  void testResolveVersionLevel() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    String expectedUrn = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    assert  graph.resolveVersion(urn).toString() == expectedUrn
  }

  @Test
  void testResolveVersionSubstr1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1@μῆνιν[1]")
    String expectedUrn = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    assert  graph.resolveVersion(urn).toString() == expectedUrn
  }


  @Test
  void testResolveVersionSubstr2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1@μῆνιν[1]-1.1@ἄειδε[1]")
    String expectedUrn = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    assert  graph.resolveVersion(urn).toString() == expectedUrn
  }

  @Test
  void testResolveVersionSubstr3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1-1.2@οὐλομένην[1]")
    String expectedUrn = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1-1.2"
    assert  graph.resolveVersion(urn).toString() == expectedUrn
  }

  @Test
  void testResolveVersionSubstr4() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1@μῆνιν[1]-1.2")
    String expectedUrn = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1-1.2"
    assert  graph.resolveVersion(urn).toString() == expectedUrn
  }

  @Test
  void testResolveVersionSubstr5() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1@μῆνιν[1]-2")
    String expectedUrn = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1-2"
    assert  graph.resolveVersion(urn).toString() == expectedUrn
  }
  
  
}
