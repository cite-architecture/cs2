package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestPrevNextVersionedIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String orderedUrn1 = "urn:cite2:hmt:venAsign.v1:14"
  String orderedUrn2 = "urn:cite2:hmt:venAsign.v1:15"
  String orderedUrn3 = "urn:cite2:hmt:venAsign.v1:16"
  String orderedUrn4 = "urn:cite2:hmt:venAsign.v1:17"
  String orderedUrn5 = "urn:cite2:hmt:venAsign:15-16"
  String firstUrn = "urn:cite2:hmt:venAsign.v1:1"
  String lastUrn = "urn:cite2:hmt:venAsign.v1:2906"

  @Test
  void testTest(){
    assert true
  }

  // Simple object example, should work
  @Test
  void testGetPrev1() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedUrn2)
		System.err.println("-------- getPrev1-----")
    assert cc.getPrevUrn(urn)['prevUrn'].toString() == orderedUrn1
  }
  // Un-versioned range
  @Test
  void testGetPrev2() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedUrn5)
		System.err.println("-------- getPrev2-----")
    assert cc.getPrevUrn(urn)['prevUrn'].toString() == orderedUrn1
  }

  // Simple object example, should work
  @Test
  void testGetNext1() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedUrn2)
		System.err.println("-------- getNext1-----")
		System.err.println("cc.getNextUrn(urn): ${cc.getNextUrn(urn)}")
    assert cc.getNextUrn(urn)['nextUrn'].toString() == orderedUrn3
  }

  // Unversioned range.
  @Test
  void testGetNext2() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(orderedUrn5)
		System.err.println("-------- getNext2----")
    assert cc.getNextUrn(urn)['nextUrn'].toString() == orderedUrn4
  }

  // GetPrev on first urn
  @Test
  void testGetPrevOnFirst() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(firstUrn)
		System.err.println("-------- getPrevOnFirst-----")
    assert cc.getPrevUrn(urn)['prevUrn'] == null
  }

  // GetNext on last
  @Test
  void testGetNextOnLast() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(lastUrn)
		System.err.println("-------- getNextOnLast-----")
    assert cc.getNextUrn(urn)['nextUrn'] == null
  }

}
