package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import groovy.json.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph
import edu.holycross.shot.sparqlcts.CtsReply


class TestGetFirstUrnIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)
  CtsReply reply = new CtsReply( sparql, graph)

  @Test
  void testSetup1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:")

	CtsUrn testReply = graph.getFirstUrn(urn)

	assert testReply.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    
  }


  @Test
  void testVersion1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest:")

	CtsUrn testReply = graph.getFirstUrn(urn)

	assert testReply.toString() == "urn:cts:greekLit:tlg0016.tlg001.engTest:1.0"
    
  }

  @Test
  void testVersion2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest:3.1")

	CtsUrn testReply = graph.getFirstUrn(urn)

	assert testReply.toString() == "urn:cts:greekLit:tlg0016.tlg001.engTest:3.1"
    
  }

  @Test
  void testVersion3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest:2")

	CtsUrn testReply = graph.getFirstUrn(urn)

	assert testReply.toString() == "urn:cts:greekLit:tlg0016.tlg001.engTest:2.1"
    
  }

  @Test
  void testWork1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")

	CtsUrn testReply = graph.getFirstUrn(urn)

	assert testReply.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    
  }

  @Test
  void testWork2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.5")

	CtsUrn testReply = graph.getFirstUrn(urn)

	assert testReply.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.5"
    
  }

  @Test
  void testWork3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.5-2.5")

	CtsUrn testReply = graph.getFirstUrn(urn)

	assert testReply.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.5"
    
  }

  @Test
  void testWork4() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2-2.5")

	CtsUrn testReply = graph.getFirstUrn(urn)

	assert testReply.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:2.1"
    
  }


  @Test
  void testExemplar1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:")

	CtsUrn testReply = graph.getFirstUrn(urn)

	assert testReply.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.1.1"
    
  }


  @Test
  void testExemplar2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1")

	CtsUrn testReply = graph.getFirstUrn(urn)

	assert testReply.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.1.1"
    
  }

  @Test
  void testExemplar3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:2")

	CtsUrn testReply = graph.getFirstUrn(urn)

	assert testReply.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:2.1.1"
    
  }


}
