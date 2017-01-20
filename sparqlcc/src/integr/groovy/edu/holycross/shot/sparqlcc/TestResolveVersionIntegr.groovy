package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestResolveVersionIntegr extends GroovyTestCase {


	String baseUrl = "http://localhost:8080/fuseki/cc/query"

	@Test
	void testTest() {
		assert true
	}


	// Simple object example, should work
	@Test
	void testResolveVersion1() {
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
	  Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:vaimg:VA085RN_0086")

		assert cc.resolveVersion(urn).toString() == "urn:cite2:hmt:vaimg.v1:VA085RN_0086"
	}
	// Has a version. Should work
	@Test
	void testResolveVersion2() {
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
	  Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:vaimg.v1:VA085RN_0086")

		assert cc.resolveVersion(urn).toString() == "urn:cite2:hmt:vaimg.v1:VA085RN_0086"
	}
	// Range URN. Should work
	@Test
	void testResolveVersion3() {
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
	  Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:vaimg:VA085RN_0086-VA089RN_0090")

		assert cc.resolveVersion(urn).toString() == "urn:cite2:hmt:vaimg.v1:VA085RN_0086-VA089RN_0090"
	}



	// Should work
	@Test
	void testResolveVersion4() {
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
	  Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:vaimg.v1:VA085RN_0086-VA089RN_0090")

		assert cc.resolveVersion(urn).toString() == "urn:cite2:hmt:vaimg.v1:VA085RN_0086-VA089RN_0090"
	}

	// Subref
	@Test
	void testResolveVersion5() {
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
	  Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:vaimg.v1:VA085RN_0086@12,12,12,12-VA089RN_0090")

		assert cc.resolveVersion(urn).toString() == "urn:cite2:hmt:vaimg.v1:VA085RN_0086@12,12,12,12-VA089RN_0090"
	}

	// Collection-level URN should work
	@Test
	void testResolveVersion6() {
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
	  Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:vaimg:")

		assert cc.resolveVersion(urn).toString() == "urn:cite2:hmt:vaimg.v1:"
	}

	// Urn with version, returns itself
	@Test
	void testResolveVersion7() {
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
	  Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:vaimg.v1:VA085RN_0086")

		assert cc.resolveVersion(urn).toString() == "urn:cite2:hmt:vaimg.v1:VA085RN_0086"
	}


	// Good urn, but not in data, throws exception
	@Test
	void testResolveVersion8() {
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
	  Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:vaimg:notAnObject")
		assert shouldFail{
			assert cc.resolveVersion(urn)
		}
	}

	// Good but not in data, returns throws exception
	@Test
	void testResolveVersion9() {
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
	  Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:notACollection:12")
		assert shouldFail{
			assert cc.resolveVersion(urn)
		}
	}

}
