package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetRangeIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testRange1(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Unordered collection, should get just first- and last-identified object
    CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi.3.v1-24.v1")
    ArrayList ccos = cc.getRange(urn)
    assert ccos.size() == 2
  }

  @Test
  void testRange2(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Ordered collection, should include 10 objects
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.2601.v1-2610.v1")
    ArrayList ccos = cc.getRange(urn)
    assert ccos.size() == 10

  }

  @Test
  void testRange3(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Should contain 1 object
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.2601.v1")
    ArrayList ccos = cc.getRange(urn)
    assert ccos.size() == 1

  }

  @Test
  void testBadRange1(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // With an Ordered collection, you can't do this
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.2610.v1-2601.v1")
    shouldFail {
      ArrayList ccos = cc.getRange(urn)
      assert ccos
    }
  }

/* We passed this once, but it is commented out because it takes forever */
/*
  @Test
  void testRangeWholeColl(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Ordered collection, should include 10 objects
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign")
    ArrayList ccos = cc.getRange(urn)
    assert ccos.size() == 2903

  }
  */




}
