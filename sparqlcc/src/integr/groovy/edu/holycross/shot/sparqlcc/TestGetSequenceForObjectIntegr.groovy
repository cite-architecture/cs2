package edu.holycross.shot.sparqlcc

import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestGetSequenceForObjectIntegr extends GroovyTestCase {

	/* Make a collection */
	String baseUrl = "http://localhost:8080/fuseki/cc/query"
	Sparql sparql = new Sparql(baseUrl)
	CcGraph cc = new CcGraph(sparql)


	@Test void testSequence1() {

		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:1")
		assert cc.getSequenceForObject(urn) == 1

	}

	@Test void testSequence2() {

		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:2")
		assert cc.getSequenceForObject(urn) == 2

	}

	@Test void testSequence3() {

		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:460")
		assert cc.getSequenceForObject(urn) == 460

	}

	@Test void testSequence4() {

		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:2")
		shouldFail{
			assert cc.getSequenceForObject(urn) == 2
		}

	}

	@Test void testSequence5() {

		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:2-3")
		shouldFail{
			assert cc.getSequenceForObject(urn) == 2
		}

	}

	@Test void testSequence6() {

		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:")
		shouldFail{
			assert cc.getSequenceForObject(urn) == 2
		}

	}


}
