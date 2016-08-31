package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetLastIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String orderedColl = "urn:cite:hmt:venAsign" // urn:cite:hmt:venAsign.2906.v1
  String orderedUrn = "urn:cite:hmt:venAsign.3" // urn:cite:hmt:venAsign.2906.v1
  String orderedColl2 = "urn:cite:hmt:msA" // urn:cite:hmt:msA.5v.v1
  String orderedRange = "urn:cite:hmt:venAsign.14-15" // urn:cite:hmt:venAsign.2906.v1
  String unOrderedColl = "urn:cite:hmt:vaimg" // unordered; will fail

  @Test
  void testTest(){
    assert true
  }

  // Simple collection example, should work
  @Test
  void testLastUrn1() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedColl)
    assert cc.getLastUrn(urn)['lastUrn'].toString() == "urn:cite:hmt:venAsign.2906.v1"
  }
  // Object example
  @Test
  void testLastUrn2() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedUrn)
    assert cc.getLastUrn(urn)['lastUrn'].toString() == "urn:cite:hmt:venAsign.2906.v1"
  }

  // Collection with multiple versions
  @Test
  void testLastUrn3() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedColl2)
    assert cc.getLastUrn(urn)['lastUrn'].toString() == "urn:cite:hmt:msA.5v.v1"
  }

  // Range
  @Test
  void testLastUrn4() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedRange)
    assert cc.getLastUrn(urn)['lastUrn'].toString() == "urn:cite:hmt:venAsign.2906.v1"
  }

  // Unordered collection. Should fail
  @Test
  void testLastUrn5() {
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(unOrderedColl)
    shouldFail {
      String test = cc.getLastUrn(urn)['lastUrn'].toString()
    }
  }

}
