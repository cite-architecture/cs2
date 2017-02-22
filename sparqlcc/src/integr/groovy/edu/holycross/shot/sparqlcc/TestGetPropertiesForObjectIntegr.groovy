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
    assert props["Codex"] == "urn:cite2:hmt:codices:msA"
    assert props["Folio"] == "urn:cite2:hmt:msA:13r"
    assert props["Label"] == "Page area of folio 13r"
    assert props["ImageRoI"] == "urn:cite2:hmt:vaimg.v1:VA013RN-0014%400.0675%2C0.0892%2C0.795%2C0.8638" // url encoded! Problem?

  }


}
