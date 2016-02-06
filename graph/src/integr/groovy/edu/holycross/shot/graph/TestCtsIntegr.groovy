package edu.holycross.shot.graph

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn
import edu.holycross.shot.citeservlet.Sparql



/* Fun Facts from the test dataset:


*/

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
		assert ctsurn.encodeSubref() == dbCtsUrn
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
	void testTextGroup() {
	  println "Testing testTextGroup"
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


	@Test
	void testFindExemplarsForVersion_nonexantUrn(){
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002.test:")
	  ArrayList al = gs.graph.exemplarsForVersion(urn)
	  assert al.size() == 0
	  CtsUrn urn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002:")
	  ArrayList al2 = gs.graph.exemplarsForVersion(urn2)
	  assert al2.size() == 1
	  assert al2[0] == "ERROR: URN must point to a version-level URN"
	}


	/* Test containing URN, e.g. "Book 2", at the version-level */		

	@Test
	void testVersionContainer() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 24 
	}  



	@Test
	void testVersionContainer_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002.test:2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 0
	}  




	/* Test Version-level CTS-URNs */

	/* We expect 20 results from the original URN dataset, yielding 30 triples */
	/* We also expect 35 analytical exemplar citations */

  @Test
  void testVersionLeaf() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 65
  }  



  @Test
  void testVersionLeaf_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002.test:2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 0
  }  



  /* -------------------------------------
     FAILING
	 ------------------------------------- */

	/* Test Work-level CTS-URNs */
	/* Version msA:2.1 =  30, including labels and object-sequence info 
	   Version alignedEng:2.1 = 12, including labels and object-sequence info
	   Exemplar wt:2.1 =  35, including labels and sequence info */



	@Test
	void testWorkLeaf() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 77
	}  



	@Test
	void testWorkLeaf_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002:2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 0
	}  

	@Test
	void testWorkWithoutPassage() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1000000 
	}

	@Test
	void testWorkWithoutPassage_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002:")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1000000 
	}

	@Test
	void testOneOffCtsUrn1() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.192")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1
	}

	@Test
	void testOneOffCtsUrn2() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002:1.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 3 // One scholion, which has one label ond one sequence
	}

/*
	@Test
	void testWorkContainer() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1000 
	}  
	*/

/*
	@Test
	void testWorkContainer_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002:2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 0 
	}  
	*/

/*
	@Test
	void testWorkRange1() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2.1-2.2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1000 
	}  
	*/

/*
	@Test
	void testWorkRange2() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-2.2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1000 
	}  
	*/

/*
	@Test
	void testExemplarRange1() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.wt:1.1.4-1.2.2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1000 
	}  
*/

/*
	@Test
	void testExemplarRange2() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.wt:1.1.1-2.1.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1000 
	}  
	
	@Test
	void testExemplarLeaf() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.wt:2.1.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 11
	}  

	void testExemplarLeaf_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002.wt:2.1.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 0  
	}  

	@Test
	void testExemplarContainer() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.wt:2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 1000
	}  

	void testExemplarContainer_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002.wt:2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 0
	}  
	*/


	/* We expect 20 results from the original URN dataset */
	/* We also expect 8 analytical exemplar citations */
	/*
	@Test
	void testVersionLeafRange() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 28
	}  
	*/

}
