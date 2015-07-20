package edu.holycross.shot.graph

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql


class TestCtsIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/graph/query"
  Sparql sparql = new Sparql(baseUrl)
  GraphService gs = new GraphService(sparql)

  /* Some CTS URNs */

  String ctsSubjGroup = "urn:cts:greekLit:tlg0012:"
  String ctsSubjWork = "urn:cts:greekLit:tlg0012.tlg001:"
  String ctsSubjVersion = "urn:cts:greekLit:tlg0012.tlg001.msA:"

  String ctsSubjLeaf = "urn:cts:greekLit:tlg0012.tlg001.msA:1.2"
  String ctsSubjContaining = "urn:cts:greekLit:tlg0012.tlg001.msA:1"
  String ctsSubjLeafRange = "urn:cts:greekLit:tlg0012.tlg001.msA:1.2-1.10"
  String ctsSubjContainingRange = "urn:cts:greekLit:tlg0012.tlg001.msA:1-2"
  String ctsSubjMixedRange1 = "urn:cts:greekLit:tlg0012.tlg001.msA:1-2.10"
  String ctsSubjMixedRange2 = "urn:cts:greekLit:tlg0012.tlg001.msA:1.2-2"
  
  String ctsSubjNotionalLeaf = "urn:cts:greekLit:tlg0012.tlg001:1.2"
  String ctsSubjNotionalContaining = "urn:cts:greekLit:tlg0012.tlg001:1"
  String ctsSubjNotionalLeafRange = "urn:cts:greekLit:tlg0012.tlg001:1.2-1.10"
  String ctsSubjNotionalContainingRange = "urn:cts:greekLit:tlg0012.tlg001:1-2"
  String ctsSubjNotionalMixedRange1 = "urn:cts:greekLit:tlg0012.tlg001:1-2.10"
  String ctsSubjNotionalMixedRange2 = "urn:cts:greekLit:tlg0012.tlg001:1.2-2"

  String ctsSubjLeafWithSubstr = "urn:cts:greekLit:tlg0012.tlg001.msA:1.15@πάντας[1]"

  /* End Sample URNs */


/*  @Test
  void testVersionLeaf() {
    CtsUrn urn = new CtsUrn(ctsSubjLeaf)
 	println "${urn}: ${gs.graph.findAdjacent(urn)}"
  }

   @Test
  void testVersionLeafWithSubstring() {
    CtsUrn urn = new CtsUrn(ctsSubjLeafWithSubstr)
 	println "${urn}: ${gs.graph.findAdjacent(urn)}"
  }


  @Test
  void testVersionContainer() {
    CtsUrn urn = new CtsUrn(ctsSubjContaining)
 	println "${urn}: ${gs.graph.findAdjacent(urn)}"   
  }

  @Test
  void testVersionLeafRange() {
    CtsUrn urn = new CtsUrn(ctsSubjLeafRange)
 	println gs.graph.findAdjacent(urn)   
  }
  
  @Test
  void testGroup() {
    CtsUrn urn = new CtsUrn(ctsSubjGroup)
 	println gs.graph.findAdjacent(urn)   
  } */

  @Test
  void testNotionalLeaf() {
    CtsUrn urn = new CtsUrn(ctsSubjNotionalLeaf)
 	println gs.graph.findAdjacent(urn)   
  } 


}
