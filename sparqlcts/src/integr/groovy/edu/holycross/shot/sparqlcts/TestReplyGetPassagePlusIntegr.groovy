package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import groovy.json.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
//import edu.holycross.shot.citeservlet.Sparql
//import edu.holycross.shot.sparqlcts.CtsGraph
//import edu.holycross.shot.sparqlcts.CtsReply


class TestReplyGetPassagePlusIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)
  CtsReply reply = new CtsReply( sparql, graph)

  @Test
  void testSetup() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:3.4")

	Map testReply = reply.getPassagePlusObject(urn)

	assert testReply
	//println new JsonBuilder(testReply).toPrettyString()
    
  }

  @Test
  void testXML_context_1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.2.3")
	Integer context = 1

	String testReply = reply.getPassagePlusToXML(urn,1)

	assert testReply
	//println testReply
    
  }

  @Test
  void testXML_context_2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:2.3-2.8")
	Integer context = 1

	String testReply = reply.getPassagePlusToXML(urn,1)

	assert testReply
	println testReply
    
  }


  @Test
  void testXML1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.2.3")

	String testReply = reply.getPassagePlusToXML(urn)

	assert testReply
	//println testReply
    
  }

  @Test
  void testXML2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.2")

	String testReply = reply.getPassagePlusToXML(urn)

	assert testReply
	//println testReply
    
  }

  @Test
  void testXML3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1")

	String testReply = reply.getPassagePlusToXML(urn)

	assert testReply
	//println testReply
    
  }


  @Test
  void testXML4() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1")

	String testReply = reply.getPassagePlusToXML(urn)

	assert testReply
	//println testReply
    
  }

  @Test
  void testXML5() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10-2.1")

	String testReply = reply.getPassagePlusToXML(urn)

	assert testReply
	//println testReply
    
  }

  @Test
  void testXML6() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10-3.1")

	String testReply = reply.getPassagePlusToXML(urn)

	assert testReply
	//println testReply
    
  }

  @Test
  void testXML7() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest:1.2-1.3")

	String testReply = reply.getPassagePlusToXML(urn)

	assert testReply
	//println testReply
    
  }

  @Test
  void testXML8() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest.wt:1.2.1-1.2.5")

	String testReply = reply.getPassagePlusToXML(urn)

	assert testReply
	//println testReply
    
  }

  @Test
  void testXML_OneLevel(){
    CtsUrn urn = new CtsUrn("urn:cts:hmtDemo:goethe.erlkoenig.deu:3")

	String testReply = reply.getPassagePlusToXML(urn)

	assert testReply
	println testReply
  }

}
