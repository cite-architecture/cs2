package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetPropertiesForObjectIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"


  @Test
  void testTest(){
    assert true 
  }

  @Test
  void testGetProperties1(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:3")
    Map props = cc.getPropertiesForObject(urn)
    assert props["Codex"] == "urn:cite:hmt:codices.msA"
    assert props["Folio"] == "urn:cite2:hmt:msA.v1:13r"
    assert props["Label"] == "Page area of folio 13r"
    assert props["ImageRoI"] == "urn:cite:hmt:vaimg.VA013RN-0014.v1@0.0675,0.0892,0.795,0.8638"

  }


}
