package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetVersionsOfObjectIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String testUrn1  = "urn:cite:hmt:pageroi.5"
  String testUrn2  = "urn:cite:hmt:pageroi"
  String testUrn3  = "urn:cite:hmt:pageroi.5.v1"
  String testUrn4  = "urn:cite:hmt:pageroi.5.v1@12,12,12,12"
  String testUrn5  = "urn:cite:hmt:pageroi.5.v1-33.v1"


  @Test
  void testTest(){
    assert true
  }

  // Simple collection example, should work
  @Test
  void testVersionsOfObject1() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(testUrn1)
    ArrayList va = cc.getVersionsOfObject(urn)
    assert va.size() == 2
    List stringArray = []
    va.each{ v ->
      stringArray << v.toString()
    }
    assert stringArray.contains("urn:cite:hmt:pageroi.5.v1")
    assert stringArray.contains("urn:cite:hmt:pageroi.5.v2")

  }

  // Should fail, since param is a collection-level URN.
  @Test
  void testVersionsOfObject2() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(testUrn2)
    shouldFail{
      assert cc.getVersionsOfObject(urn)
    }
  }

  @Test
  void testVersionsOfObject3() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(testUrn3)
    assert cc.getVersionsOfObject(urn).size() == 2
  }

  @Test
  void testVersionsOfObject4() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(testUrn4)
    assert cc.getVersionsOfObject(urn).size() == 2
  }

  @Test
  void testVersionsOfObject5() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(testUrn5)
    assert cc.getVersionsOfObject(urn).size() == 2
  }

}
