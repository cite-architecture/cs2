package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestFindVersionIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)

  
  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
  CtsUrn nonextant_urn = new CtsUrn("urn:cts:greekLit:tlgXXX.tlgXX:1.1")
  
  @Test
  void testFindVersion() {
    // because there is only one version in the test data set, we
    // can safely expect to resolve a work-level URN to the identifier
    // for that version:
    String expectedVersion = "testAllen"
    assert graph.findVersion(urn) == expectedVersion
  }

  @Test
  void testFindVersion_nonextantUrn() {
    // because there is only one version in the test data set, we
    // can safely expect to resolve a work-level URN to the identifier
    // for that version:
    String expectedVersion = "testAllen"
	try {
		expectedVersion = graph.findVesion(nonextant_urn)
	} catch (Exception e) {
	    expectedVersion = ""	
	}
    assert expectedVersion == ""
  }

  @Test
  void testMakeUrn() {
    String expectedUrnStr = "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    assert graph.resolveVersion(urn).toString() == expectedUrnStr
  }

}
