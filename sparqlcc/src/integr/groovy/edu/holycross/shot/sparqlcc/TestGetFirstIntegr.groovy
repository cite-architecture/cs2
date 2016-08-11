package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetFirstUrnIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String orderedColl = "urn:cite:hmt:venAsign"
  String orderedUrn = "urn:cite:hmt:venAsign.3"
  String orderedColl2 = "urn:cite:hmt:msA"
  String orderedRange = "urn:cite:hmt:venAsign.14-15"
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
    CiteUrn urn = new CiteUrn(orderedColl)
    assert cc.getFirstUrn(urn).toString() == "urn:cite:hmt:venAsign.1.v1"
  }
  // Object example
  @Test
  void testFirstUrn2() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedUrn)
    assert cc.getFirstUrn(urn).toString() == "urn:cite:hmt:venAsign.1.v1"
  }

  // Collection with multiple versions
  @Test
  void testFirstUrn3() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedColl2)
    assert cc.getFirstUrn(urn).toString() == "urn:cite:hmt:msA.1r.v1"
  }

  // Range
  @Test
  void testFirstUrn4() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedRange)
    assert cc.getFirstUrn(urn).toString() == "urn:cite:hmt:venAsign.1.v1"
  }

  // Unordered collection. Should fail
  @Test
  void testFirstUrn5() {
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(unOrderedColl)
    shouldFail {
      String test = cc.getFirstUrn(urn).toString()
    }
  }

  // Collection with multiple versions
  @Test
  void testFirstUrn6() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedColl2,'v2')
    assert cc.getFirstUrn(urn).toString() == "urn:cite:hmt:msA.5v.v2"
  }
  // Collection with multiple versions
  @Test
  void testFirstUrn7() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedColl2,'v1')
    assert cc.getFirstUrn(urn).toString() == "urn:cite:hmt:msA.1r.v1"
  }


}
