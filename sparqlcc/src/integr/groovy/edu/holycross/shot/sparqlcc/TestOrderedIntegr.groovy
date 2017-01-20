package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestOrderedIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String orderedUrn1 = "urn:cite2:hmt:venAsign.v1:14"
  String orderedUrn2 = "urn:cite2:hmt:venAsign.v1:15"
  String orderedUrn3 = "urn:cite2:hmt:venAsign.v1:14-15"
  String unorderedUrn1 = "urn:cite2:hmt:vaimg:VA082RN_0083"
  String unorderedUrn2 = "urn:cite2:hmt:vaimg.v1:VA082RN_0083"
  String unorderedUrn3 = "urn:cite2:hmt:vaimg:"

  @Test
  void testTest(){
    assert true
  }

  // Simple object example, should work
  @Test
  void testOrdered1() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedUrn2)
    assert cc.isOrdered(urn)

  }

  // Simple object example, should work
  @Test
  void testOrdered2() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedUrn2)
    assert cc.isOrdered(urn)

  }

  // Simple object example, should work
  @Test
  void testOrdered3() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedUrn2)
    assert cc.isOrdered(urn)

  }

  // Simple object example, should work
  @Test
  void testUnOrdered1() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(unorderedUrn2)
    assert cc.isOrdered(urn) == false

  }

  // Simple object example, should work
  @Test
  void testUnOrdered2() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(unorderedUrn2)
    assert cc.isOrdered(urn) == false

  }

  // Simple object example, should work
  @Test
  void testUnOrdered3() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(unorderedUrn2)
    assert cc.isOrdered(urn) == false

  }

}
