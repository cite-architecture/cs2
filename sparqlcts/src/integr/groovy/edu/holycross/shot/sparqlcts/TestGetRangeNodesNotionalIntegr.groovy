package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestGetRangeNodesNotionalIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)


  @Test
  void testGetRangeNodesLeaf() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
	ArrayList response = graph.getRangeNodes(urn)

	assert response.size() == 1
	assert response[0].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.msA:1.1"
	assert response[0].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" n="1"> Μῆνιν ἄειδε θεὰ <persName n="urn:cite:hmt:pers.pers1"> Πηληϊάδεω Ἀχιλῆος</persName></l>""" 
	}    

  @Test
  void testGetRangeNodesRange() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-1.10")
	ArrayList response = graph.getRangeNodes(urn)

	assert response.size() == 10
	assert response[0].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.msA:1.1"
	assert response[0].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" n="1"> Μῆνιν ἄειδε θεὰ <persName n="urn:cite:hmt:pers.pers1"> Πηληϊάδεω Ἀχιλῆος</persName></l>""" 
	assert response[9].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.msA:1.10"
	assert response[9].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" n="10"> νοῦσον ἀνὰ στρατὸν ὦρσε κακήν· ὀλέκοντο δὲ λαοὶ. </l>"""
 
	}    

  @Test
  void testGetRangeNodesContainer() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1")
	ArrayList response = graph.getRangeNodes(urn)

	assert response.size() == 610 
	assert response[0].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.msA:1.1"
	assert response[0].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" n="1"> Μῆνιν ἄειδε θεὰ <persName n="urn:cite:hmt:pers.pers1"> Πηληϊάδεω Ἀχιλῆος</persName></l>""" 
	assert response[609].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.msA:1.611"
	assert response[609].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" n="611"> ἔνθα κὰθεῦδ' ἀναβὰς, παρ δὲ χρυσόθρονος <persName n="urn:cite:hmt:pers.pers116"> Ἥρη</persName> . </l>""" 
 
	}    
  @Test
	  void testGetRangeNodesMixesRange1() {
		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.610-2")
			  ArrayList response = graph.getRangeNodes(urn)

			  assert response.size() == 876
			  assert response[0].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.msA:1.610"
			  assert response[0].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" n="610"> ἔνθα πάρος κοιμᾶθ' ὅτε μιν γλυκὺς ὕπνος ἱ̈κάνοι· </l>""" 

			  assert response[875].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.msA:2.877"
			  assert response[875].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" n="877"> τηλόθεν ἐκ <placeName n="urn:cite:hmt:place.place75"> Λυκίης</placeName> <placeName n="urn:cite:hmt:place.place93"> Ξάνθου</placeName> ἄπο δινήεντος· </l>""" 

	  }

  @Test
	  void testGetRangeNodesMixesRange2() {
		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1-2.5")

			  ArrayList response = graph.getRangeNodes(urn)

			  assert response.size() == 615 
			  assert response[0].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.msA:1.1"
			  assert response[0].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" n="1"> Μῆνιν ἄειδε θεὰ <persName n="urn:cite:hmt:pers.pers1"> Πηληϊάδεω Ἀχιλῆος</persName></l>""" 
			  assert response[614].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.msA:2.5"
			  assert response[614].textContent == """<l xmlns="http://www.tei-c.org/ns/1.0" n="5"> ἧδε, δέ οι κατα θυμὸν ἀρίστη φαίνετο βουλῆ· </l>""" 

	  }


}
