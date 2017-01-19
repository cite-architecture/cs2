package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetVersionsOfObjectIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String testUrn1  = "urn:cite2:hmt:pageroi.v1:5"
  String testUrn2  = "urn:cite2:hmt:pageroi.v1:"
  String testUrn3  = "urn:cite2:hmt:pageroi.v1:5"
  String testUrn4  = "urn:cite2:hmt:pageroi.v1:5.v1@12,12,12,12"
  String testUrn5  = "urn:cite2:hmt:pageroi.v1:5.v1-33"


  @Test
  void testTest(){
    assert true
  }

  // Simple collection example, should work
  @Test
  void testVersionsOfObject1() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(testUrn1)
    ArrayList va = cc.getVersionsOfObject(urn)
    assert va.size() == 2
    List stringArray = []
    va.each{ v ->
      stringArray << v.toString()
    }
    assert stringArray.contains("urn:cite2:hmt:pageroi.v1:5")
    assert stringArray.contains("urn:cite2:hmt:pageroi.v1:5.v2")

  }

  // Should fail, since param is a collection-level URN.
  @Test
  void testVersionsOfObject2() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(testUrn2)
    shouldFail{
      assert cc.getVersionsOfObject(urn)
    }
  }

  @Test
  void testVersionsOfObject3() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(testUrn3)
    assert cc.getVersionsOfObject(urn).size() == 2
  }

  @Test
  void testVersionsOfObject4() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(testUrn4)
    assert cc.getVersionsOfObject(urn).size() == 2
  }

  @Test
  void testVersionsOfObject5() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(testUrn5)
    assert cc.getVersionsOfObject(urn).size() == 2
  }

}
