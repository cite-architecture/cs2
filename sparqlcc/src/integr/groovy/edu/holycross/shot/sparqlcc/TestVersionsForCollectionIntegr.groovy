package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestVersionsForCollectionIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"

  @Test
  void testTest() {
    assert true
	 }


  // Simple object example, should work
  @Test
  void testVersionsForCollection1() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn collectionUrn =  new Cite2Urn("urn:cite2:hmt:msA:")
		String test1 = "urn:cite2:hmt:msA.v1:"
    CcGraph cc = new CcGraph(sparql)

		ArrayList vcc = cc.versionsForCollection(collectionUrn)

		assert vcc.size() == 2
		def result = vcc.find{ it.toString() == test1}
		assert result

  }

  // Simple object example, urn has version-string should work
  @Test
  void testVersionsForCollection2() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn collectionUrn =  new Cite2Urn("urn:cite2:hmt:msA.v1:")
		String test1 = "urn:cite2:hmt:msA.v2:"
    CcGraph cc = new CcGraph(sparql)

		ArrayList vcc = cc.versionsForCollection(collectionUrn)

		assert vcc.size() == 2
		def result = vcc.find{ it.toString() == test1}
		assert result

  }

  // Simple object example, with version, should work
  @Test
  void testVersionsForCollection3() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn collectionUrn =  new Cite2Urn("urn:cite2:hmt:venAsign.v1:")
		String test1 = "urn:cite2:hmt:venAsign.v1:"
    CcGraph cc = new CcGraph(sparql)

		ArrayList vcc = cc.versionsForCollection(collectionUrn)

		assert vcc.size() == 1
		def result = vcc.find{ it.toString() == test1}
		assert result

  }

  // Simple object example, without version, should work
  @Test
  void testVersionsForCollection4() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn collectionUrn =  new Cite2Urn("urn:cite2:hmt:venAsign:")
		String test1 = "urn:cite2:hmt:venAsign.v1:"
    CcGraph cc = new CcGraph(sparql)

		ArrayList vcc = cc.versionsForCollection(collectionUrn)

		assert vcc.size() == 1
		def result = vcc.find{ it.toString() == test1}
		assert result

  }

  // Not in data
  @Test
  void testVersionsForCollection5() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn collectionUrn =  new Cite2Urn("urn:cite2:hmt:notACollection:")
    CcGraph cc = new CcGraph(sparql)

		ArrayList vcc = cc.versionsForCollection(collectionUrn)

		assert vcc.size() == 0

  }

}
