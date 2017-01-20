package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGvrForCollectionIntegr  extends GroovyTestCase {

	// urn:cite:hmt:vaimg = 966
	// urn:cite2:hmt:pageroi.v1: == 20 for each version
	// urn:cite2:hmt:venAsign.v1: == 2906 for all.
	// urn:cite2:hmt:venAsign.v1:11.v1-20.v1 == 10, no surprises
	// urn:cite2:hmt:msA.v1: == ordered, 10, no surprises

	String baseUrl = "http://localhost:8080/fuseki/cc/query"

	@Test
	void testTest(){
		assert true
	}

	@Test
	void testBigCollection(){
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:")
		assert cc.getValidReff(urn)['urns'].size() == 2903
	}

	@Test
	void testCollections(){
		ArrayList replyArray = []
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
		replyArray = cc.getValidReff(urn)['urns']
		assert replyArray.size() == 20
	}

	@Test
	void testCollectionsWithVersionString(){
		ArrayList replyArray = []
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
		replyArray = cc.getValidReff(urn)['urns']
		assert replyArray.size() == 20
		def urnMatch = replyArray.find { it.toString() == "urn:cite2:hmt:pageroi.v1:2"}
		assert urnMatch != null
	}

	@Test
	void testOrderedCollection(){
		ArrayList replyArray = []
		Sparql sparql = new Sparql(baseUrl)
		CcGraph cc = new CcGraph(sparql)
		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:")
		replyArray = cc.getValidReff(urn)['urns']
		assert replyArray.size() == 2903
		assert replyArray[0].toString() == "urn:cite2:hmt:venAsign.v1:1"
		assert replyArray[2902].toString() == "urn:cite2:hmt:venAsign.v1:2906"
	}

}
