package edu.holycross.shot.graph

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn
import edu.holycross.shot.citeservlet.Sparql


class TestCtsIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/graph/query"
  Sparql sparql = new Sparql(baseUrl)
  GraphService gs = new GraphService(sparql)

  	/* Test how the database is encoded */
	@Test
	void testDatabaseEncoding(){
		String dbCtsUrn = "urn:cts:greekLit:tlg0012.tlg001.msA:1.1%40%CE%9C%E1%BF%86%CE%BD%CE%B9%CE%BD%5B1%5D"
		String regCtsUrn = "urn:cts:greekLit:tlg0012.tlg001.msA:1.1@Μῆνιν[1]"
		String dbImgUrn = "urn:cite:hmt:vaimg.VA012RN-0013%400.1642%2C0.2237%2C0.3604%2C0.0338"
		String regImgUrn = "urn:cite:hmt:vaimg.VA012RN-0013@0.1642,0.2237,0.3604,0.0338"
		CtsUrn ctsurn = new CtsUrn(regCtsUrn)
//		CiteUrn citeurn = new CiteUrn(regImgUrn)
//		println "${ctsurn.encodeSubref()}"
		assert ctsurn.encodeSubref() == dbCtsUrn
//		assert citeurn.encodeSubref() == dbImgUrn
		
	}

	/* Test some basic CTS URN stuff */
	@Test
	void testNewEncodingURN(){
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.2@ἄλγε[1]")
		String encodedUrn = urn.encodeSubref()
		String decodedUrn = urn.toString()
		assert urn.toString() == decodedUrn
		
	}


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

	/* Test finding exemplars for a version. For "urn:cts:greekLit:tlg0012.tlg001.msA:"
		in the test data we expect one exemplar. */
  @Test
  void testFindExemplarsForVersion(){
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:")
	  ArrayList al = gs.graph.exemplarsForVersion(urn)
	  assert al.size() == 1
	  assert al[0] == "urn:cts:greekLit:tlg0012.tlg001.msA.wt:"
	  CtsUrn urn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
	  ArrayList al2 = gs.graph.exemplarsForVersion(urn2)
	  assert al2.size() == 1
	  assert al2[0] == "ERROR: URN must point to a version-level URN"
  }


/* Fun Facts from the test dataset:

    urn:cts:greekLit:tlg0012.tlg001.msA: 

	urn:cts:greekLit:tlg0012.tlg001.msA:1.1 (including .wt:1.1) = 22 (+ 1 analytical exmplar from version)
	urn:cts:greekLit:tlg0012.tlg001.msA:1.2 (including .wt:1.2) = 26 (+ 1 analytical exmplar from version)
	urn:cts:greekLit:tlg0012.tlg001.msA:1.2 (including .wt:2.1) = 28 (+ 1 analytical exmplar from version)
	urn:cts:greekLit:tlg0012.tlg001.msA:1.2 (including .wt:2.2) = 28 (+ 1 analytical exmplar from version)

	urn:cts:greekLit:tlg0012.tlg001.msA:1 (including .tw:1) = 6 (+ 1 analytical exemplar) 
	urn:cts:greekLit:tlg0012.tlg001.msA:2 (including .tw:2) = 24:
		- Version: citation depth, sequence, label, 2 Iliad lines (each with a ctsSeq and a label), a "prev" (with a ctsSeq and a label)
		- Exemplar: citation depth, sequence, label, 2 iliad lines (each with a ctsSeq and a label), a "prev" (with a ctsSeq and a label)

*/

			
  @Test
  void testVersionContainerURN() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 24 
  }  


	/* Test Work-level CTS-URNs */
	/* Version msA:2.1 =  30, including labels and object-sequence info 
	   Version alignedEng:2.1 = 9
	   Exemplar wt:2.1 =  13 */

  @Test
  void testWorkLeafNodeURN() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1000 
  }  

  @Test
  void testWorkContainingURN() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1000 
  }  

  @Test
  void testWorkRangeURN1() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2.1-2.2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1000 
  }  

  @Test
  void testWorkRangeURN2() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-2.2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1000 
  }  

	/* Test Version-level CTS-URNs */

	/* We expect 20 results from the original URN dataset, yielding 30 triples */
	/* We also expect 35 analytical exemplar citations */
  @Test
  void testVersionLeafNodeURN() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 65
  }  


  
	/* We expect 20 results from the original URN dataset */
	/* We also expect 8 analytical exemplar citations */
  @Test
  void testVersionLeafRangeURN() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 28
  }  


}
