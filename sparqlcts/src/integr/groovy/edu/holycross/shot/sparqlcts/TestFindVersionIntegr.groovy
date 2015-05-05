package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestFindVersionIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)

  
  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
  
  @Test
  void testFindVersion() {
    // because there is only one version in the test data set, we
    // can safely expect to resolve a work-level URN to the identifier
    // for that version:
    String expectedVersion = "msA"
    assert graph.findVersion(urn) == expectedVersion
  }

  @Test
  void testMakeUrn() {
    String expectedUrnStr = "urn:cts:greekLit:tlg0012.tlg001.msA:1.1"
    assert graph.resolveVersion(urn).toString() == expectedUrnStr
  }

}