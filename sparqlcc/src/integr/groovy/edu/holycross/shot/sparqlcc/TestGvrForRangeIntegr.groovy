package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGvrForRangeIntegr extends GroovyTestCase {


// urn:cite:hmt:vaimg = 966
// urn:cite2:hmt:pageroi.v1: == 20 for each version
// urn:cite2:hmt:venAsign.v1: == 2906 for all.
// urn:cite2:hmt:venAsign.v1:11.v1-20.v1 == 10, no surprises
// urn:cite2:hmt:msA.v1: == ordered, 10, no surprises

  String baseUrl = "http://localhost:8080/fuseki/cc/query"


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testRangeOfOrdered(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:11-20")
    assert cc.getValidReff(urn)['urns'].size() == 10
  }

  @Test
  void testRangeOfOrderedNotional(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:11-20")
    assert cc.getValidReff(urn)['urns'].size() == 10
  }

  @Test
  void testRangeOfOrderedBadRange1(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:20-11")
//    shouldFail{
      assert cc.getValidReff(urn)['urns']
//    }
  }

  @Test
  void testRangeOfUnOrdered(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:3-6")
    assert cc.getValidReff(urn)['urns'].size() == 2
  }

  @Test
  void testRangeOfUnOrdered2(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:3-6")
    assert cc.getValidReff(urn)['urns'].size() == 2
  }

  @Test
  void testCollectionWithVersionString1(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
    assert cc.getValidReff(urn)['urns'].size() == 20
  }

  @Test
  void testCollectionWithVersionString2(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:3")
    String vString = "v2"
    assert cc.getValidReff(urn)['urns'].size() == 20
  }

  @Test
  void testContents1(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:10-14")
    ArrayList correct = [
      "urn:cite2:hmt:venAsign.v1:10",
      "urn:cite2:hmt:venAsign.v1:11",
      "urn:cite2:hmt:venAsign.v1:12",
      "urn:cite2:hmt:venAsign.v1:13",
      "urn:cite2:hmt:venAsign.v1:14" ]
    assert cc.getValidReff(urn)['urns'] == correct
  }

  @Test
  void testContents2(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:10-14")
    ArrayList correct = [
    "urn:cite2:hmt:venAsign.v1:10",
    "urn:cite2:hmt:venAsign.v1:11",
    "urn:cite2:hmt:venAsign.v1:12",
    "urn:cite2:hmt:venAsign.v1:13",
    "urn:cite2:hmt:venAsign.v1:14" ]
    assert cc.getValidReff(urn)['urns'] == correct
  }

  @Test
  void testContents3(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:4-5")
    ArrayList correct = [
      "urn:cite2:hmt:pageroi.v1:4",
      "urn:cite2:hmt:pageroi.v1:4.v2",
      "urn:cite2:hmt:pageroi.v1:5",
      "urn:cite2:hmt:pageroi.v1:5.v2"]
    assert cc.getValidReff(urn)['urns'] == correct
  }



    @Test
    void testRangeOfUnOrderedNotional(){
      Sparql sparql = new Sparql(baseUrl)
      CcGraph cc = new CcGraph(sparql)
      Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:3-6")
      assert cc.getValidReff(urn)['urns'].size() == 4
    }

}
