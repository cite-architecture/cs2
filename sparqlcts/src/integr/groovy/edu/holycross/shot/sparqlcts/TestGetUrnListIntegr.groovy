package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestGetUrnListIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)


  

  @Test
  void testListLeafNode() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
	ArrayList expectedList = []
	expectedList.add(urn)
	ArrayList answerList = []
	answerList = graph.getUrnList(urn)

	assert expectedList.size() == 1
	assert answerList.size() == 1
    assert expectedList == answerList
    
  }

  @Test
  void testListRange() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.3-1.5")
	CtsUrn answerUrn1 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.3")
	CtsUrn answerUrn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.4")
	CtsUrn answerUrn3 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.5")

	ArrayList expectedList = []
	expectedList.add(answerUrn1)
	expectedList.add(answerUrn2)
	expectedList.add(answerUrn3)
	ArrayList answerList = []
	answerList = graph.getUrnList(urn)

	assert expectedList.size() == 3
	assert answerList.size() == 3
    assert expectedList[0].toString() == answerList[0].toString()
    assert expectedList[1].toString() == answerList[1].toString()
    assert expectedList[2].toString() == answerList[2].toString()
    
  }

  @Test
  void testListContainer() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1")
	CtsUrn answerUrnFirst = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
	CtsUrn answerUrnMiddle = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.100")
	CtsUrn answerUrnLast = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.611")

	ArrayList answerList = []
	answerList = graph.getUrnList(urn)

	//assert answerList.size() == 611 
    assert answerList[0].toString() == answerUrnFirst.toString()
    assert answerList[99].toString() == answerUrnMiddle.toString()
    assert answerList[609].toString() == answerUrnLast.toString()
    
  }

  @Test
  void testListMixedRange1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1-2.5")
	CtsUrn answerUrn1 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
	CtsUrn answerUrn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2.5")

	ArrayList answerList = []
	answerList = graph.getUrnList(urn)

	assert answerList.size() == 615
    assert answerList[0].toString() == answerUrn1.toString()
    assert answerList[614].toString() == answerUrn2.toString()
    
  }

  @Test
  void testListMixedRange2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.610-2")
	CtsUrn answerUrn1 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.610")
	CtsUrn answerUrn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2.877")

	ArrayList answerList = []
	answerList = graph.getUrnList(urn)

	assert answerList.size() == 876
    assert answerList[0].toString() == answerUrn1.toString()
    assert answerList[875].toString() == answerUrn2.toString()
    
  }



}
