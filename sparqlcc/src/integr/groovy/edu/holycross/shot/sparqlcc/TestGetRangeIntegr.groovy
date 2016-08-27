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
    assert false
  }

  @Test
  void testRange1(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Unordered collection, should get just first- and last-identified object
    CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi.3.v1-24.v1")
    CCOSet ccos = cc.getRange(urn)
    assert ccos
  }

  @Test
  void testRange2(){
    // Ordered collection, should include 10 objects
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.2601.v1-2610.v1")

  }

  @Test
  void testBadRange1(){
    // With an Ordered collection, you can't do this
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.2610.v1-2601.v1")
  }





}
