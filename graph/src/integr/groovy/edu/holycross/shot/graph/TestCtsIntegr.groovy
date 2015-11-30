package edu.holycross.shot.graph

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql


class TestCtsIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/graph/query"
  Sparql sparql = new Sparql(baseUrl)
  GraphService gs = new GraphService(sparql)

  /* Test Group-level CTS URNs */


	/* We expect 16 hits from this dataset */
  @Test
  void testTextGroupURN() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012:")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 16
  }  


   /* We expect to find one work, one edition, one translation, and one exemplar */
  @Test
	  void testTextGroupURN2() {
		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012:")
			  ArrayList al = gs.graph.findAdjacent(urn)
			  String failedUrns = ""
			  String testUriString = "urn:cts:greekLit:tlg0012.tlg001:"
			  URI testUri = new URI(URLEncoder.encode(testUriString, "UTF-8"))
			  Boolean uriPresent = false
			  al.each{ 
				if (it.subj == testUri){ uriPresent = true }
			  }
			  if (uriPresent == false){
				failedUrns += testUriString
			  }
			  testUriString = "urn:cts:greekLit:tlg0012.tlg001.msA:"
			  testUri = new URI(URLEncoder.encode(testUriString, "UTF-8"))
			  uriPresent = false
			  al.each{ 
				if (it.subj == testUri){ uriPresent = true }
			  }
			  if (uriPresent == false){
				failedUrns += testUriString
			  }
			  testUriString = "urn:cts:greekLit:tlg0012.tlg001.alignedEng:"
			  testUri = new URI(URLEncoder.encode(testUriString, "UTF-8"))
			  uriPresent = false
			  al.each{ 
				if (it.subj == testUri){ uriPresent = true }
			  }
			  if (uriPresent == false){
				failedUrns += testUriString
			  }
			  testUriString = "urn:cts:greekLit:tlg0012.tlg001.msA.wt:"
			  testUri = new URI(URLEncoder.encode(testUriString, "UTF-8"))
			  uriPresent = false
			  al.each{ 
				if (it.subj == testUri){ uriPresent = true }
			  }
			  if (uriPresent == false){
				failedUrns += testUriString
			  }
		  assert failedUrns == ""
	  }  

	/* Test Work-level CTS-URNs */

	/* Test Version-level CTS-URNs */

	/* We expect 20 results from the original URN dataset */
	/* We also expect 8 analytical exemplar citations */
  @Test
  void testVersionLeafNodeURN() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 28
  }  

/* Fun Facts from the test dataset:
	urn:cts:greekLit:tlg0012.tlg001.msA:1.1 (including .wt:1.1) = 22 (+ 1 analytical exmplar from version)
	urn:cts:greekLit:tlg0012.tlg001.msA:1.2 (including .wt:1.2) = 26 (+ 1 analytical exmplar from version)
	urn:cts:greekLit:tlg0012.tlg001.msA:1.2 (including .wt:2.1) = 28 (+ 1 analytical exmplar from version)
	urn:cts:greekLit:tlg0012.tlg001.msA:1.2 (including .wt:2.2) = 28 (+ 1 analytical exmplar from version)

	urn:cts:greekLit:tlg0012.tlg001.msA:1 (including .tw:1) = 6 (+ 1 analytical exemplar) 
	urn:cts:greekLit:tlg0012.tlg001.msA:2 (including .tw:2) = 6 (+ 1 analytical exemplar) 
*/


  
	/* We expect 20 results from the original URN dataset */
	/* We also expect 8 analytical exemplar citations */
  @Test
  void testVersionLeafRangeURN() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 28
  }  


}
