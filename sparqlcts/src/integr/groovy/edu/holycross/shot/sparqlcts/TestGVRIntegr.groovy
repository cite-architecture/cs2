package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
//import edu.holycross.shot.citeservlet.Sparql
//import edu.holycross.shot.sparqlcts.CtsGraph


class TestGVRIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)

  @Test
  void testGetLeafDepth1(){
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:")
	Integer expectedDepth = 2
	Integer actualDepth = graph.getLeafDepth(urn)
	assert expectedDepth == actualDepth

  }

  @Test
  void testGetLeafDepth2(){
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:")
	Integer expectedDepth = 3
	Integer actualDepth = graph.getLeafDepth(urn)
	assert expectedDepth == actualDepth

  }

  @Test
  void testGVR_overload1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:")
    ArrayList expectedGVR = []
	Integer numRefs = 3
    ArrayList actualGVR = graph.getValidReff(urn,1)
	assert actualGVR.size() == numRefs
  }

  @Test
  void testGVR_overload2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:")
    ArrayList expectedGVR = []
	Integer numRefs = 30
    ArrayList actualGVR = graph.getValidReff(urn)
	assert actualGVR.size() == numRefs
  }

  @Test
  void testGVR_overload3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:")
    ArrayList expectedGVR = []
	Integer numRefs = 30
    ArrayList actualGVR = graph.getValidReff(urn.toString(),2)
	assert actualGVR.size() == numRefs
  }

  @Test
  void testGVR_overload4() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:")
    ArrayList expectedGVR = []
	Integer numRefs = 30
    ArrayList actualGVR = graph.getValidReff(urn.toString())
	assert actualGVR.size() == numRefs
  }

  @Test
  void testGVR_version() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    ArrayList expectedGVR = []
	expectedGVR << urn.toString()
    ArrayList actualGVR = graph.getValidReff(urn.toString())
	assert expectedGVR == actualGVR
  }

  @Test
  void testGVR_work() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    ArrayList expectedGVR = []
	expectedGVR << "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    ArrayList actualGVR = graph.getValidReff(urn.toString())
	assert expectedGVR == actualGVR
  }

  @Test
  void testGVR_range1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-1.10")
    ArrayList expectedGVR = []
	Integer numRefs = 10
	expectedGVR << "urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1"
    ArrayList actualGVR = graph.getValidReff(urn.toString())
	assert actualGVR.size() == numRefs
  }

  @Test
  void testGVR_range2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1-2")
    ArrayList expectedGVR = []
	Integer numRefs = 20
    ArrayList actualGVR = graph.getValidReff(urn.toString())
	assert actualGVR.size() == numRefs
  }

@Test
void testGVR_container() {
  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1")
  ArrayList expectedGVR = []
  Integer numRefs = 10
  ArrayList actualGVR = graph.getValidReff(urn.toString())
  assert actualGVR.size() == numRefs
}

@Test
void testGVR_container2() {
  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.10-2.1")
  ArrayList expectedGVR = []
  Integer numRefs = 18
  ArrayList actualGVR = graph.getValidReff(urn.toString())
  assert actualGVR.size() == numRefs
}

@Test
void testGVR_notional() {
  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001:")
  ArrayList expectedGVR = []
  Integer numRefs = 3
  ArrayList actualGVR = graph.getValidReff(urn,1)
  assert actualGVR.size() == numRefs
}



}
