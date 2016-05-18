package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestGetUrnListIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)


  @Test
  void testListLeafNode() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    ArrayList expectedList = []
    expectedList.add(urn)
    ArrayList answerList = []
    answerList = graph.getUrnList(urn)

    assert expectedList.size() == 1
    assert answerList.size() == 1
	println "expectedList[0] class = ${expectedList[0].getClass()}"
	println "answerList[0] class = ${answerList[0].getClass()}"
    assert expectedList[0].toString() == answerList[0].toString()
    
  }

  @Test
  void testListLeafNodeSubref() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1@μῆνιν[1]")
    ArrayList expectedList = []
    expectedList.add(new CtsUrn(urn.reduceToNode()))
    ArrayList answerList = []
    answerList = graph.getUrnList(urn)

    assert expectedList.size() == 1
    assert answerList.size() == 1
	println "expectedList[0] class = ${expectedList[0].getClass()}"
	println "answerList[0] class = ${answerList[0].getClass()}"
    assert expectedList[0].toString() == answerList[0].toString()
    
  }

  @Test
  void testListRange() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.3-1.5")
    CtsUrn answerUrn1 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.3")
    CtsUrn answerUrn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.4")
    CtsUrn answerUrn3 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.5")

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
  void testListRangeSubref() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.3@ψυχὰς[1]-1.5@πᾶσι[1]")
    CtsUrn answerUrn1 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.3")
    CtsUrn answerUrn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.4")
    CtsUrn answerUrn3 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.5")

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
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1")
    CtsUrn answerUrnFirst = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    CtsUrn answerUrnMiddle = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.5")
    CtsUrn answerUrnLast = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10")

    ArrayList answerList = []
    answerList = graph.getUrnList(urn)

    assert answerList.size() == 10
    assert answerList[0].toString() == answerUrnFirst.toString()
    assert answerList[4].toString() == answerUrnMiddle.toString()
    assert answerList[9].toString() == answerUrnLast.toString()
  }

  @Test
  void testListMixedRange1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1-2.5")
    CtsUrn answerUrn1 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    CtsUrn answerUrn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:2.5")

    ArrayList answerList = []
    answerList = graph.getUrnList(urn)

    assert answerList.size() == 15
    assert answerList[0].toString() == answerUrn1.toString()
    assert answerList[14].toString() == answerUrn2.toString()
    
  }

  @Test
  void testListMixedRangePlusSubref() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1-2.5@α[2]")
    CtsUrn answerUrn1 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    CtsUrn answerUrn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:2.5")

    ArrayList answerList = []
    answerList = graph.getUrnList(urn)

    assert answerList.size() == 15
    assert answerList[0].toString() == answerUrn1.toString()
    assert answerList[14].toString() == answerUrn2.toString()
    
  }

  @Test
  void testListMixedRange2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10-2")
    CtsUrn answerUrn1 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10")
    CtsUrn answerUrn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:2.10")

    ArrayList answerList = []
    answerList = graph.getUrnList(urn)
    
    assert answerList.size() == 11
    assert answerList[0].toString() == answerUrn1.toString()
    assert answerList[10].toString() == answerUrn2.toString()
    
  }

  @Test
  void testListRangeNotional() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.3-1.5")
    CtsUrn answerUrn1 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.3")
    CtsUrn answerUrn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.4")
    CtsUrn answerUrn3 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.5")

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


}
