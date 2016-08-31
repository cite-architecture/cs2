package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestPrevNextVersionedIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String orderedUrn1 = "urn:cite:hmt:venAsign.14.v1"
  String orderedUrn2 = "urn:cite:hmt:venAsign.15.v1"
  String orderedUrn3 = "urn:cite:hmt:venAsign.16.v1"
  String orderedUrn4 = "urn:cite:hmt:venAsign.17.v1"
  String orderedUrn5 = "urn:cite:hmt:venAsign.15-16"
  String firstUrn = "urn:cite:hmt:venAsign.1.v1"
  String lastUrn = "urn:cite:hmt:venAsign.2906.v1"

  @Test
  void testTest(){
    assert true
  }

  // Simple object example, should work
  @Test
  void testGetPrev1() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedUrn2)
    assert cc.getPrevUrn(urn)['prevUrn'].toString() == orderedUrn1
  }
  // Un-versioned range
  @Test
  void testGetPrev2() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedUrn5)
    assert cc.getPrevUrn(urn)['prevUrn'].toString() == orderedUrn1
  }

  // Simple object example, should work
  @Test
  void testGetNext1() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedUrn2)
    assert cc.getNextUrn(urn)['nextUrn'].toString() == orderedUrn3
  }

  // Unversioned range.
  @Test
  void testGetNext2() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(orderedUrn5)
    assert cc.getNextUrn(urn)['nextUrn'].toString() == orderedUrn4
  }

  // GetPrev on first urn
  @Test
  void testGetPrevOnFirst() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(firstUrn)
    assert cc.getPrevUrn(urn)['prevUrn'] == null
  }

  // GetNext on last
  @Test
  void testGetNextOnLast() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(lastUrn)
    assert cc.getNextUrn(urn)['nextUrn'] == null
  }

}
