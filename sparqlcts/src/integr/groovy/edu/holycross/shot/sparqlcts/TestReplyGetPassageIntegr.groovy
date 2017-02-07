package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import groovy.json.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
//import edu.holycross.shot.citeservlet.Sparql
//import edu.holycross.shot.sparqlcts.CtsGraph
//import edu.holycross.shot.sparqlcts.CtsReply


class TestReplyGetPassageIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)
  CtsReply reply = new CtsReply( sparql, graph)

  @Test
  void testSetup1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:3.4")
	Integer context = 0

	Map testReply = reply.getPassageObject(urn,context)

	assert testReply
	println new JsonBuilder(testReply).toPrettyString()

  }

  @Test
  void testSetup2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:3.4")
	Integer context = 0

	String testReply = reply.getPassageToXML(urn)

	assert testReply
	println "-----------------------------------------"
	println testReply

  }

  @Test
  void testSetup3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2")
	Integer context = 0

	String testReply = reply.getPassageToXML("urn:cts:greekLit:tlg0012.tlg001:3.4",context)

	assert testReply
	println "-----------------------------------------"
	println testReply

  }

  @Test
  void testSetup4() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.3")
	Integer context = 1

	String testReply = reply.getPassageToXML("urn:cts:greekLit:tlg0012.tlg001:3.4",context)

	assert testReply
	println "-----------------------------------------"
	println testReply

  }

  @Test
  void testSetup5() {
  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2")
	Integer context = 5

	String testReply = reply.getPassageToXML(urn,context)

	assert testReply
	println "-----------------------------------------"
	println testReply

  }

	@Test
	void testNonXML1() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0007.tlg012:1.1.1")

		String testReply = reply.getPassageToXML(urn)

		assert testReply
		println "-----------------------------------------"
		println testReply
	}

	@Test
	void testNonXML2() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0007.tlg012:1.1.1-1.1.3")

		String testReply = reply.getPassageToXML(urn)

		assert testReply
		println "-----------------------------------------"
		println testReply
	}

	@Test
	void testNonXML3() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0007.tlg012:1.1")

		String testReply = reply.getPassageToXML(urn)

		assert testReply
		println "-----------------------------------------"
		println testReply
	}

	@Test
	void testNonXML4() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0007.tlg012:1.1-2.1")

		String testReply = reply.getPassageToXML(urn)

		assert testReply
		println "-----------------------------------------"
		println testReply
	}

}
