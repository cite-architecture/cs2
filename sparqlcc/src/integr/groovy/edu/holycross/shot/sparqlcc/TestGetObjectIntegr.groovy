package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
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
    CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi.3.v1")

    CiteCollectionObject cco = cc.getObject(urn)
    assert cco
    System.err.println(cco.urn)
    System.err.println(cco.objectProperties)
  }

  @Test
  void testGetObjectOrdered(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.3.v1")

    CiteCollectionObject cco = cc.getObject(urn)
    assert cco
    System.err.println(cco.urn)
    System.err.println(cco.objectProperties)
  }

  @Test
  void testGetObjectRange(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.3.v1-5.v1")

    shouldFail {
      CiteCollectionObject cco = cc.getObject(urn)
      assert cco
      System.err.println(cco.urn)
      System.err.println(cco.objectProperties)
    }
  }


}
