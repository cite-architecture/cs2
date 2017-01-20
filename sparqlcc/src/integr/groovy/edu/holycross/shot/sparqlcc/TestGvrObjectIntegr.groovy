package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGvrObjectIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"

// urn:cite:hmt:vaimg = 966
// urn:cite2:hmt:pageroi.v1: == 20 for each version
// urn:cite2:hmt:venAsign.v1: == 2906 for all.
// urn:cite2:hmt:venAsign.v1:11.v1-20.v1 == 10, no surprises
// urn:cite2:hmt:msA.v1: == ordered, 10, no surprises

  @Test
  void testTest(){
    assert true
  }




  @Test
  void testSingleObjectVersioned(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:3")
    assert cc.getValidReff(urn)['urns'].size() == 1
    assert cc.getValidReff(urn)['urns'][0].toString() == urn.toString()
  }

  @Test
  void testSingleObjectNotional(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi:3")
    assert cc.getValidReff(urn)['urns'].size() == 1
    assert cc.getValidReff(urn)['urns'][0].toString() == "urn:cite2:hmt:pageroi.v1:3"
  }










}
