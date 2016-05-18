package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestGetRangeNodesIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)


  @Test
  void testGetRangeNodesLeaf() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
	ArrayList response = graph.getRangeNodes(urn)

	assert response.size() == 1
	// response is [node,typeExtras]
	assert response[0]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
	assert response[0]['rangeNode'].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="1">Μῆνιν ἄειδε θεὰ Πηληϊάδεω Ἀχιλῆος </l>""" 
	}    

  @Test
  void testGetRangeNodesRange() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1-1.10")
	ArrayList response = graph.getRangeNodes(urn)

	assert response.size() == 10
	assert response[0]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
	assert response[0]['rangeNode'].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="1">Μῆνιν ἄειδε θεὰ Πηληϊάδεω Ἀχιλῆος </l>""" 
	assert response[9]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10"
	assert response[9]['rangeNode'].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="10">νοῦσον ἀνὰ στρατὸν ὄρσε κακήν, ὀλέκοντο δὲ λαοί, </l>"""
 
	}    

  @Test
  void testGetRangeNodesContainer() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1")
	ArrayList response = graph.getRangeNodes(urn)

	assert response.size() == 10 
	assert response[0]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
	assert response[0]['rangeNode'].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="1">Μῆνιν ἄειδε θεὰ Πηληϊάδεω Ἀχιλῆος </l>""" 
	assert response[9]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10"
	assert response[9]['rangeNode'].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="10">νοῦσον ἀνὰ στρατὸν ὄρσε κακήν, ὀλέκοντο δὲ λαοί, </l>"""
 
	}    

  @Test
	  void testGetRangeNodesMixedRange1() {
		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10-2")
			  ArrayList response = graph.getRangeNodes(urn)

			  assert response.size() == 11 
			  assert response[0]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10"
		      assert response[0]['rangeNode'].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="10">νοῦσον ἀνὰ στρατὸν ὄρσε κακήν, ὀλέκοντο δὲ λαοί, </l>"""

			  assert response[10]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:2.10"
			  assert response[10]['rangeNode'].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="10">πάντα μάλ᾽ ἀτρεκέως ἀγορευέμεν ὡς ἐπιτέλλω· </l>""" 

	  }

  @Test
	  void testGetRangeNodesMixedRange2() {
		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1-2.5")

			  ArrayList response = graph.getRangeNodes(urn)

			  assert response.size() == 15 
			  assert response[0]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
			  assert response[0]['rangeNode'].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="1">Μῆνιν ἄειδε θεὰ Πηληϊάδεω Ἀχιλῆος </l>""" 
			  assert response[14]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:2.5"
			  assert response[14]['rangeNode'].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="5">ἧδε δέ οἱ κατὰ θυμὸν ἀρίστη φαίνετο βουλή, </l>""" 

	  }

}
