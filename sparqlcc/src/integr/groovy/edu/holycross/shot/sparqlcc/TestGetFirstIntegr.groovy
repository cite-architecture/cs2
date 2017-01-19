package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetFirstUrnIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String orderedColl = "urn:cite2:hmt:venAsign.v1:"
  String orderedUrn = "urn:cite2:hmt:venAsign.v1:3"
  String orderedColl2 = "urn:cite2:hmt:msA.v1:"
  String orderedRange = "urn:cite2:hmt:venAsign.v1:14-15"
  String unOrderedColl = "urn:cite:hmt:vaimg"


  @Test
  void testTest(){
    assert true
  }

  // Simple collection example, should work
  @Test
  void testFirstUrn1() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedColl)
    assert cc.getFirstUrn(urn)['firstUrn'].toString() == "urn:cite2:hmt:venAsign.v1:1"
  }
  // Object example
  @Test
  void testFirstUrn2() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedUrn)
    assert cc.getFirstUrn(urn)['firstUrn'].toString() == "urn:cite2:hmt:venAsign.v1:1"
  }

  // Collection with multiple versions
  @Test
  void testFirstUrn3() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedColl2)
    assert cc.getFirstUrn(urn)['firstUrn'].toString() == "urn:cite2:hmt:msA.v1:1r"
  }

  // Range
  @Test
  void testFirstUrn4() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedRange)
    assert cc.getFirstUrn(urn)['firstUrn'].toString() == "urn:cite2:hmt:venAsign.v1:1"
  }

  // Unordered collection. Should fail
  @Test
  void testFirstUrn5() {
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(unOrderedColl)
    shouldFail {
      String test = cc.getFirstUrn(urn)['firstUrn'].toString()
    }
  }

}
