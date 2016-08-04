package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import groovy.json.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
//import edu.holycross.shot.citeservlet.Sparql
//import edu.holycross.shot.sparqlcts.CtsGraph
//import edu.holycross.shot.sparqlcts.CtsReply


class TestReplyGetDescriptionIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)
  CtsReply reply = new CtsReply( sparql, graph)

  @Test
  void testSetup1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")

	Map testReply = reply.getDescriptionObject(urn)

	assert testReply

	println new JsonBuilder(testReply).toPrettyString()
    
  }

   @Test
  void testXml1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")

	String testReply = reply.getDescriptionToXML(urn)
	
	assert testReply

	println testReply
    
  }




}
