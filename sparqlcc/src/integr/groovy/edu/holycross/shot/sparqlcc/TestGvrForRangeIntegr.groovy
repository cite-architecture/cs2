package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGvrForRangeIntegr extends GroovyTestCase {


// urn:cite:hmt:vaimg = 966
// urn:cite:hmt:pageroi == 20 for each version
// urn:cite:hmt:venAsign == 2906 for all.
// urn:cite:hmt:venAsign.11.v1-20.v1 == 10, no surprises
// urn:cite:hmt:msA == ordered, 10, no surprises

  String baseUrl = "http://localhost:8080/fuseki/cc/query"


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testRangeOfOrdered(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.11.v1-20.v1")
    assert cc.getValidReff(urn).size() == 10
  }

  @Test
  void testRangeOfOrderedNotional(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.11-20")
    assert cc.getValidReff(urn).size() == 10
  }

  @Test
  void testRangeOfOrderedBadRange1(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.20-11")
    shouldFail{
      assert cc.getValidReff(urn)
    }
  }

  @Test
  void testRangeOfUnOrdered(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi.3.v1-6.v1")
    assert cc.getValidReff(urn).size() == 2
  }

  @Test
  void testRangeOfUnOrdered2(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi.3.v2-6.v2")
    assert cc.getValidReff(urn).size() == 2
  }

  @Test
  void testCollectionWithVersionString1(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi")
    String vString = "v1"
    assert cc.getValidReff(urn, vString).size() == 20
  }

  @Test
  void testCollectionWithVersionString2(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi.3.v1")
    String vString = "v2"
    assert cc.getValidReff(urn, vString).size() == 20
  }

  @Test
  void testContents1(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.10-14")
    ArrayList correct = [
      "urn:cite:hmt:venAsign.10.v1",
      "urn:cite:hmt:venAsign.11.v1",
      "urn:cite:hmt:venAsign.12.v1",
      "urn:cite:hmt:venAsign.13.v1",
      "urn:cite:hmt:venAsign.14.v1" ]
    assert cc.getValidReff(urn) == correct
  }

  @Test
  void testContents2(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.10.v1-14.v1")
    ArrayList correct = [
      "urn:cite:hmt:venAsign.10.v1",
      "urn:cite:hmt:venAsign.11.v1",
      "urn:cite:hmt:venAsign.12.v1",
      "urn:cite:hmt:venAsign.13.v1",
      "urn:cite:hmt:venAsign.14.v1" ]
    assert cc.getValidReff(urn) == correct
  }

  @Test
  void testContents3(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi.4-5")
    ArrayList correct = [
      "urn:cite:hmt:pageroi.4.v1",
      "urn:cite:hmt:pageroi.4.v2",
      "urn:cite:hmt:pageroi.5.v1",
      "urn:cite:hmt:pageroi.5.v2"]
    assert cc.getValidReff(urn) == correct
  }



    @Test
    void testRangeOfUnOrderedNotional(){
      Sparql sparql = new Sparql(baseUrl)
      CcGraph cc = new CcGraph(sparql)
      CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi.3-6")
      assert cc.getValidReff(urn).size() == 4
    }

}
