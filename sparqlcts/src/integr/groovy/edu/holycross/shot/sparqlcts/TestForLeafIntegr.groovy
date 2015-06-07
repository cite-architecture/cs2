package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph



/**
 * Integration test to see if a URN refers to a leaf node or not.
 */
class TestForLeafIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)

  CtsUrn lineUrn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
  CtsUrn bookUrn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1")
  

  @Test
  void testDummy() {
    assert  graph.isLeafNode(lineUrn)
    assertFalse(graph.isLeafNode(bookUrn) )
  }
  
}