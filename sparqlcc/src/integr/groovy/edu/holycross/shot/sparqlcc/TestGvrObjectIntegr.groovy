package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGvrObjectIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"

// urn:cite:hmt:vaimg = 966
// urn:cite:hmt:pageroi == 20 for each version
// urn:cite:hmt:venAsign == 2906 for all.
// urn:cite:hmt:venAsign.11.v1-20.v1 == 10, no surprises
// urn:cite:hmt:msA == ordered, 10, no surprises

  @Test
  void testTest(){
    assert true 
  }




  @Test
  void testSingleObjectVersioned(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi.3.v1")
    assert cc.getValidReff(urn).size() == 1
    assert cc.getValidReff(urn)[0] == urn.toString()
  }

  @Test
  void testSingleObjectNotional(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi.3")
    assert cc.getValidReff(urn).size() == 2
    assert cc.getValidReff(urn).contains("urn:cite:hmt:pageroi.3.v1")
    assert cc.getValidReff(urn).contains("urn:cite:hmt:pageroi.3.v2")
  }










}
