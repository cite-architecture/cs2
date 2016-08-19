package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetValidReffIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"

// urn:cite:hmt:vaimg = 966
// urn:cite:hmt:pageroi == 20 for each version
// urn:cite:hmt:venAsign == 2906 for all.
// urn:cite:hmt:venAsign.11.v1-20.v1 == 10, no surprises
// urn:cite:hmt:msA == ordered, 10, no surprises

  @Test
  void testTest(){
    assert false
  }

  @Test
  void testBigCollection(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign")
    assert cc.getValidReff(urn).size() == 2906
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
  void testRangeOfUnOrderedNotional(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi.3-6")
    assert cc.getValidReff(urn).size() == 2
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

}
