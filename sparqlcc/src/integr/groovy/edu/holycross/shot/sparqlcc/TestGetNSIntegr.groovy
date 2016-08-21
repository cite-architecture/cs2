package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetNSIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String orderedColl = "urn:cite:hmt:venAsign.1"

  @Test
  void testTest(){
    assert true 
  }

  // Simple collection example, should work
  @Test
  void testNS() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedColl)
    assert cc.getCollectionNamespace(urn)["abbr"] == "hmt"
    assert cc.getCollectionNamespace(urn)["full"] == "http://www.homermultitext.org/datans"
  }

}
