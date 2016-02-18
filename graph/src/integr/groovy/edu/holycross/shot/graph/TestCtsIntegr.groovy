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
	  ArrayList al = gs.graph.getExemplarsForVersion(urn)
	  assert al.size() == 1
	  assert al[0] == "urn:cts:greekLit:tlg0012.tlg001.msA.wt:"
	  CtsUrn urn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
	  ArrayList al2 = gs.graph.getExemplarsForVersion(urn2)
	  assert al2.size() == 1
	  assert al2[0] == "ERROR: URN must point to a version-level URN"
	}

	@Test
	void testFindExemplarsForVersion2(){
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
	  ArrayList al = gs.graph.getExemplarsForVersion(urn)
	  assert al.size() == 1
	  assert al[0] == "urn:cts:greekLit:tlg0012.tlg001.msA.wt:"
	  CtsUrn urn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
	  ArrayList al2 = gs.graph.getExemplarsForVersion(urn2)
	  assert al2.size() == 1
	  assert al2[0] == "ERROR: URN must point to a version-level URN"
	}


	@Test
	void testFindExemplarsForVersion_nonexantUrn(){
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002.test:")
	  ArrayList al = gs.graph.getExemplarsForVersion(urn)
	  assert al.size() == 0
	  CtsUrn urn2 = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002:")
	  ArrayList al2 = gs.graph.getExemplarsForVersion(urn2)
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


	@Test
	void testWorkLeaf() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 77
	}  


	@Test
	void testNonExtantVersionLeaf(){
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.test:1.1")
		ArrayList al = gs.graph.findAdjacent(urn)
		assert al.size() == 0
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
	  assert al.size() == 10
	}

	@Test
	void testWorkWithoutPassage_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.NOWORK:")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 0 
	} 

	@Test
	void testVersionWithoutPassage() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 20
	}

	@Test
	void testVersionWithoutPassage_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002.NOVERSION:")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 0 
	} 

	@Test
	void testExemplarWithoutPassage() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.wt:")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 87
	}

	@Test
	void testExemplarWithoutPassage_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002.msA.NOEXEMPLAR:")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 0 
	} 

	@Test
	void testOneOffCtsUrn1() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.192")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 3 // One scholion, which has one label ond one sequence
	} 

	@Test
	void testOneOffCtsUrn2() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.192")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 3 // One scholion, which has one label ond one sequence
	} 

	@Test
	void testOneOffCtsUrn3() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002:1.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 3 // One scholion, which has one label ond one sequence
	} 

	/* Results for testWorkContainer with <urn:cts:greekLit:tlg0012:tlg001:2>:
		for .msA:2 			=	12
		for .alignedEng:2   =  	7	
		for .msA.wt:2		=	12
	*/

	@Test
	void testWorkContainer() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 31
	}  


	@Test
	void testWorkContainer_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002:2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 0 
	}  

	@Test
	void testExemplarLeaf() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.wt:2.2.2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 18
	}  

    @Test
	void testExemplarLeaf_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002.msA.wt:2.1.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 0  
	} 


	/*
	Expected Results from .wt:1.1.4-1.2.2

	Range itself	1
	1.1.4			18
	1.1.5			18	
	1.2.1			18
	1.2.2			18
	-------------------
					72
	Uniqued		   -11 (labels, sequences for containers, etc.)
	-------------------
	Total			62		
	*/

	 @Test
	void testExemplarRange1() {
	  println "Starting testExemplarRange"
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.wt:1.1.4-1.2.2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  println "al.size() = ${al.size()}"
	  assert al.size() == 62
	} 

	/*
	Expected Results from .wt:1.1.1-2.1.1 
	Range itself	0
	1.1.1			15 (as first leaf-node, lacks <prev>, <prev>'s label and sequence)
	1.1.2			18
	1.1.3			18
	1.1.4			18
	1.1.5			18	
	1.2.1			18	
	1.2.2			18	
	1.2.3			18	
	1.2.4			18	
	1.2.5			18	
	1.2.6			18	
	1.2.1			18
	-------------------
					213
	Uniqued		    -49 (labels, sequences for containers, etc.)
	-------------------
	Total		   164	
	*/

	@Test
	void testExemplarRange2() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.wt:1.1.1-2.1.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 164
	}  
	

	/* Should produce the same results as ….wt:1.1.1-2.1.1 */
	@Test
	void testExemplarRange3() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.wt:1.1-2.1.1")
			ArrayList al = gs.graph.findAdjacent(urn)
			assert al.size() == 164
	}  

	/*
	Expected Results from .wt:1.1-2
	Range itself	0
	1.1.1			15 (as first leaf-node, lacks <prev>, <prev>'s label and sequence)
	1.1.2-2.2.7		18 each x 25 = 450
	2.2.8			15
	-------------------
					480
	Uniqued		   -122 (labels, sequences for containers, etc.)
	-------------------
	Total		    358
	*/
	@Test
	void testExemplarRange4() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.wt:1.1-2")
			ArrayList al = gs.graph.findAdjacent(urn)
			assert al.size() == 358
	}  

	@Test
	void testExemplarRange6() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.wt:1-2")
			ArrayList al = gs.graph.findAdjacent(urn)
			assert al.size() == 358
	}  

	/*
	msA:1.1 = 25
	msA:1.2 = 31
	msA:2.1 = 30

	msA.wt:1.1 = 32
	msA.wt:1.2 = 29
	msA.wt:2.1 = 35

	Total: 182
	Uniqued: 163

	*/

	@Test
	void testVersionLeafRange() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 162
	}  

	/* for tlg001:2.1-2.2
	.msA:2.1 = 30
	.msA:2.2 = 22
	.msA.wt:2.1 = 35
	.msA.wt:2.2 = 32
	.alignedEng:2.1 = 12
	.alignedEng:2.2 = 12
	-----------------------
	= 143

	Uniqued =  134


	*/

	@Test
	void testWorkRange1() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2.1-2.2")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 134
	}  

	@Test
	void testExemplarContainer() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.wt:2.1")
		println "started... ${urn}"
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 35
	}  
	

    /* Expected Results
	for :1.1-1.2 = 124
	for :2.1 = 77
	Total: 201
	Uniqued: 194
	*/
	@Test
	void testWorkRange2() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1-2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 194
	}  

	void testExemplarContainer_nonextantUrn() {
	  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002.wt:2.1")
	  ArrayList al = gs.graph.findAdjacent(urn)
	  assert al.size() == 0
	}  

	void testExemplarRange_nonextantUrn() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg002.msA.wt:1-2")
			ArrayList al = gs.graph.findAdjacent(urn)
			assert al.size() == 0
	}  

	void testVersionRange_fubaredUrn1() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2-1")
			ArrayList al = gs.graph.findAdjacent(urn)
			assert al.size() == 0
	}  

	void testVersionRange_fubaredUrn2() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2.1-1.1")
			ArrayList al = gs.graph.findAdjacent(urn)
			assert al.size() == 0
	}  

	void testWorkRange_fubaredUrn1() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2-1")
			ArrayList al = gs.graph.findAdjacent(urn)
			assert al.size() == 0
	}  

	void testWorkRange_fubaredUrn2() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:2.2-1.1")
			ArrayList al = gs.graph.findAdjacent(urn)
			assert al.size() == 0
	}  

	void testExemplarRange_fubaredUrn1() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2.1.1-1.1.1")
			ArrayList al = gs.graph.findAdjacent(urn)
			assert al.size() == 0
	}  

	void testExemplarRange_fubaredUrn2() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2.1-1.1")
			ArrayList al = gs.graph.findAdjacent(urn)
			assert al.size() == 0
	}  

	void testExemplarRange_fubaredUrn3() {
		CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2-1")
			ArrayList al = gs.graph.findAdjacent(urn)
			assert al.size() == 0
	}  


  /* -------------------------------------
     FAILING
	 ------------------------------------- */






}
