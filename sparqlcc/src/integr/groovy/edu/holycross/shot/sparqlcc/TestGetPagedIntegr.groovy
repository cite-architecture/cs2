package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetPagedIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"


  @Test
  void testTest(){
    assert false
  }


  @Test
  void testPaged1(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Paged for a whole collection
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign")
    CCOSet ccos = cc.getPaged(urn,1,10)
    assert ccos.urn.toString() == "urn:cite:hmt:pageroi.1.v1-10.v1"
    assert ccos.startUrn.toString() == "urn:cite:hmt:pageroi.1.v1"
    assert ccos.endUrn.toString() == "urn:cite:hmt:pageroi.10.v1"
    assert ccos.ccos.size() == 10
  }



}
