package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import groovy.json.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph
import edu.holycross.shot.sparqlcts.CtsReply


class TestReplyGetValidReffIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)
  CtsReply reply = new CtsReply( sparql, graph)

  @Test
  void testSetup() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:")

	Map testReply = reply.getValidReffObject(urn,2)

	assert testReply
	println new JsonBuilder(testReply).toPrettyString()
    
  }

  @Test
  void testObject1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest:")

	Map testReply = reply.getValidReffObject(urn,1)

	assert testReply
	println new JsonBuilder(testReply).toPrettyString()
    
  }

  @Test
  void testObject2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")

	Map testReply = reply.getValidReffObject(urn,1)

	assert testReply
	println new JsonBuilder(testReply).toPrettyString()
    
  }

  @Test
  void testObject3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")

	Map testReply = reply.getValidReffObject(urn)

	assert testReply
	println new JsonBuilder(testReply).toPrettyString()
    
  }

  @Test
  void testXML1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest:")

	String testReply = reply.getValidReffToXML(urn,1)

	assert testReply
	println testReply
  }

  @Test
  void testXML2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")

	String testReply = reply.getValidReffToXML(urn,1)

	assert testReply
	println testReply
  }

  @Test
  void testXML3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")

	String testReply = reply.getValidReffToXML(urn)

	assert testReply
	println testReply
  }

  @Test
  void testJSON1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest:")

	String testReply = reply.getValidReffToJSON(urn,1)

	assert testReply
	println testReply
  }

  @Test
  void testJSON2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")

	String testReply = reply.getValidReffToJSON(urn,1)

	assert testReply
	println testReply
  }

  @Test
  void testJSON3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")

	String testReply = reply.getValidReffToJSON(urn)

	assert testReply
	println testReply
  }


}
