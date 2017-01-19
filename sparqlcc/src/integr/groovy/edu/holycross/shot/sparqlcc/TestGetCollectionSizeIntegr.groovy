package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetCollectionSizeIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"

  String urn1 = "urn:cite2:hmt:msA.v1:" // 10
  String urn2 = "urn:cite2:hmt:venAsign.v1:" // 2906
  String urn3 = "urn:cite2:hmt:pageroi.v1:" // .v1 = 20
	String urn4 = "urn:cite2:hmt:venAsign.v1:2902" // 2903
  String urn5 = "urn:cite2:hmt:pageroi.v1:30" // 20

  @Test
  void testTest(){
    assert true
  }

  @Test
  void testCount1(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)

    Cite2Urn testUrn = new Cite2Urn(urn1)
    assert cc.getCollectionSize(testUrn)['size'] == 10
  }

  @Test
  void testCount2(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn testUrn = new Cite2Urn(urn2)
    assert cc.getCollectionSize(testUrn)['size'] == 2903
  }

  @Test
  void testCount3(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn testUrn = new Cite2Urn(urn3)
    assert cc.getCollectionSize(testUrn)['size'] == 20
  }

  @Test
  void testCount4(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn testUrn = new Cite2Urn(urn4)
    assert cc.getCollectionSize(testUrn)['size'] == 2903
  }

  @Test
  void testCount5(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn testUrn = new Cite2Urn(urn5)
    assert cc.getCollectionSize(testUrn)['size'] == 20
  }

  @Test
  void testCount6(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn testUrn = new Cite2Urn(urn3)
    assert cc.getCollectionSize(testUrn,'v1')['size'] == 20
  }

  @Test
  void testCount7(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    Cite2Urn testUrn = new Cite2Urn(urn3)
    assert cc.getCollectionSize(testUrn,'v2')['size'] == 20
  }


}
