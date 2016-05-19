package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestNextIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)

  @Test
  void testPrev() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.2")
    String expectedPrev = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    assert  graph.getPrevUrnString(urn) == expectedPrev
  }

  @Test
  void testNext() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.2")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.3"
    assert  graph.getNextUrnString(urn) == expectedNext
  }

  @Test
  void testNextRange() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.2-1.3")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.4"
    assert  graph.getNextUrnString(urn) == expectedNext
  }

  @Test
  void testNextRange_work() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.2-1.3")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.4"
    assert  graph.getNextUrnString(urn) == expectedNext
  }

  @Test
  void testNextRange_translation() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest:1.2-1.3")
    String expectedNext = "urn:cts:greekLit:tlg0016.tlg001.engTest:1.4"
    assert  graph.getNextUrnString(urn) == expectedNext
  }

  @Test
  void testNextRange_containers() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1-2")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.1"
    assert  graph.getNextUrnString(urn) == expectedNext
  }

  @Test
  void testNext_last() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:3.10")
    String expectedNext = ""
    assert  graph.getNextUrnString(urn) == expectedNext
  }


  @Test
  void testNext_exemplar1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.1.1")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.1.2"
    assert  graph.getNextUrnString(urn) == expectedNext
  }

  @Test
  void testNext_exemplar2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.1")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.2.1"
    assert  graph.getNextUrnString(urn) == expectedNext
  }

  @Test
  void testNext_exemplar3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:2")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:3.1.1"
    assert  graph.getNextUrnString(urn) == expectedNext
  }


  @Test
  void testNulls() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.2"
    
    assert  graph.getNextUrnString(urn) == expectedNext
    assert  graph.getPrevUrn(urn) == null
    assert  graph.getPrevUrnString(urn) == ""


  }

 
 /* test RangeNext methods */ 

  @Test
  void testRangeNext1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.4-1.6")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.7-1.9"
    assert  graph.getRangeNextUrnStr(urn) == expectedNext
  }

  @Test
  void testRangeNext2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.5-1.9")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10-2.4"
    assert  graph.getRangeNextUrnStr(urn) == expectedNext
  }


  @Test
  void testRangeNext3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:3.5-3.8")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.9-3.10"
    assert  graph.getRangeNextUrnStr(urn) == expectedNext
  }

  @Test
  void testRangeNext4() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:2.1-2.10"
    assert  graph.getRangeNextUrnStr(urn) == expectedNext
  }
 
  @Test
  void testRangeNext5() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:3.1-3.10")
    String expectedNext = ""
    assert  graph.getRangeNextUrnStr(urn) == expectedNext
  }
  
  @Test
  void testRangeNext6() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:3.1-3.9")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.10"
    assert  graph.getRangeNextUrnStr(urn) == expectedNext
  }

  @Test
  void testRangeNext7() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1-2.1")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen:2.2-3.2"
    assert  graph.getRangeNextUrnStr(urn) == expectedNext
  }

  @Test
  void testRangeNext8() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.2-1.3")
    String expectedNext = "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.4.1-1.5.7"
    assert  graph.getRangeNextUrnStr(urn) == expectedNext
  }


  
}
