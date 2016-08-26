package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetCollectionSizeIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"

  String urn1 = "urn:cite:hmt:msA" // 10
  String urn2 = "urn:cite:hmt:venAsign" // 2906
  String urn3 = "urn:cite:hmt:pageroi" // .v1 = 20
  String urn4 = "urn:cite:hmt:venAsign.2902.v1" // 2903
  String urn5 = "urn:cite:hmt:pageroi.30.v2" // 20

  @Test
  void testTest(){
    assert true
  }

  @Test
  void testCount1(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)

    CiteUrn testUrn = new CiteUrn(urn1)
    assert cc.getCollectionSize(testUrn) == 10
  }

  @Test
  void testCount2(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn testUrn = new CiteUrn(urn2)
    assert cc.getCollectionSize(testUrn) == 2903
  }

  @Test
  void testCount3(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn testUrn = new CiteUrn(urn3)
    assert cc.getCollectionSize(testUrn) == 20
  }

  @Test
  void testCount4(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn testUrn = new CiteUrn(urn4)
    assert cc.getCollectionSize(testUrn) == 2903
  }

  @Test
  void testCount5(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn testUrn = new CiteUrn(urn5)
    assert cc.getCollectionSize(testUrn) == 20
  }

  @Test
  void testCount6(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn testUrn = new CiteUrn(urn3)
    assert cc.getCollectionSize(testUrn,'v1') == 20
  }

  @Test
  void testCount7(){
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteUrn testUrn = new CiteUrn(urn3)
    assert cc.getCollectionSize(testUrn,'v2') == 20
  }


}
