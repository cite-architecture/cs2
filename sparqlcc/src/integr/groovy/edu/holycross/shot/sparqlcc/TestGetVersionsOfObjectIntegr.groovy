package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetVersionsOfObjectIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String testUrn1  = "urn:cite:hmt:venAsign.3"
  String testUrn2  = "urn:cite:hmt:venAsign"
  String testUrn3  = "urn:cite:hmt:venAsign.3.v1"
  String testUrn4  = "urn:cite:hmt:venAsign.3.v1@12,12,12,12"


  @Test
  void testTest(){
    assert true
  }

  // Simple collection example, should work
  @Test
  void testVersionsOfObject() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(testUrn1)
    assert cc.getVersionsOfObject(urn) == ["v1","v2"]
  }


}
