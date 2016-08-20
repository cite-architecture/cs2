package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGvrForCollectionIntegr  extends GroovyTestCase {

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
void testBigCollection(){
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)
  CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign")
  assert cc.getValidReff(urn).size() == 2906
}

@Test
void testCollectionsWithVersion(){
  ArrayList replyArray = []
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)
  CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi")
  replyArray = cc.getValidReff(urn)
  assert replyArray.size() == 40
  assert replyArray[0] == "urn:cite:hmt:pageroi.1.v1"
  assert replyArray[1] == "urn:cite:hmt:pageroi.1.v2"
}

@Test
void testOrderedCollection(){
  ArrayList replyArray = []
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)
  CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign")
  replyArray = cc.getValidReff(urn)
  assert replyArray.size() == 2906
  assert replyArray[0] == "urn:cite:hmt:venAsign.1.v1"
  assert replyArray[2905] == "urn:cite:hmt:venAsign.2906.v1"
}

}
