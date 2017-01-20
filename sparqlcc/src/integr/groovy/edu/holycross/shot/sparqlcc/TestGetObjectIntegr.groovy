package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetObjectIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testGetObjectUnordered(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:3")

    CiteCollectionObject cco = cc.getObject(urn)
    assert cco
  }

  @Test
  void testGetObjectOrdered(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:3")

    CiteCollectionObject cco = cc.getObject(urn)
    assert cco
    System.err.println("cco.urn: ${cco.urn}")
    System.err.println("cco.objectProperties: ${cco.objectProperties}")
  }

  @Test
  void testGetObjectRange(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:3-5")
		shouldFail{
	    CiteCollectionObject cco = cc.getObject(urn)
	    assert cco
		}
  }


}
