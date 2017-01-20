package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetLastIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String orderedColl = "urn:cite2:hmt:venAsign.v1:" // urn:cite2:hmt:venAsign.v1:2906.v1
  String orderedUrn = "urn:cite2:hmt:venAsign.v1:3" // urn:cite2:hmt:venAsign.v1:2906.v1
  String notionalUrn = "urn:cite2:hmt:venAsign:3" // urn:cite2:hmt:venAsign.v1:2906.v1
  String orderedColl2 = "urn:cite2:hmt:msA.v1:" // urn:cite2:hmt:msA.v1:6v.v1
  String orderedRange = "urn:cite2:hmt:venAsign.v1:14-15" // urn:cite2:hmt:venAsign.v1:2906.v1
  String unOrderedColl = "urn:cite2:hmt:vaimg:" // unordered; will fail

  @Test
  void testTest(){
    assert true
  }

  // Simple collection example, should work
  @Test
  void testLastUrn1() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedColl)
    assert cc.getLastUrn(urn)['lastUrn'].toString() == "urn:cite2:hmt:venAsign.v1:2906"
  }
  // Object example
  @Test
  void testLastUrn2() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedUrn)
    assert cc.getLastUrn(urn)['lastUrn'].toString() == "urn:cite2:hmt:venAsign.v1:2906"
  }

  // Collection with multiple versions
  @Test
  void testLastUrn3() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedColl2)
    assert cc.getLastUrn(urn)['lastUrn'].toString() == "urn:cite2:hmt:msA.v1:6v"
  }

  // Range
  @Test
  void testLastUrn4() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedRange)
    assert cc.getLastUrn(urn)['lastUrn'].toString() == "urn:cite2:hmt:venAsign.v1:2906"
  }


  // Unordered collection. Should fail
  @Test
  void testLastUrn5() {
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(unOrderedColl)
    shouldFail {
      String test = cc.getLastUrn(urn)['lastUrn'].toString()
    }
  }

  // Testing resolvedUrn
  @Test
  void testLastUrn6() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(notionalUrn)
    assert cc.getLastUrn(urn)['resolvedUrn'].toString() == "urn:cite2:hmt:venAsign.v1:"
  }

}
