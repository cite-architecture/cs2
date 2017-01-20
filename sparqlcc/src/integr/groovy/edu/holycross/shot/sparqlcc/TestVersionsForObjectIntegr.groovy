package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestVersionsForObjectIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"

  @Test
  void testTest() {
    assert true
	 }


  // Simple object example, should work
  @Test
  void testVersionsForObject1() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:msA:1r")
		String test1 = "urn:cite2:hmt:msA.v1:1r"
		String test2 = "urn:cite2:hmt:msA.v2:1r"
    CcGraph cc = new CcGraph(sparql)

		ArrayList vfo = cc.versionsForObject(urn)

		assert vfo.size() == 2
		def result = vfo.find{ it.toString() == test1}
		assert result
		result = vfo.find{ it.toString() == test2}
		assert result

  }

  // Simple object example, with version-string, should work
  @Test
  void testVersionsForObject2() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:msA.v1:1r")
		String test1 = "urn:cite2:hmt:msA.v1:1r"
		String test2 = "urn:cite2:hmt:msA.v2:1r"
    CcGraph cc = new CcGraph(sparql)

		ArrayList vfo = cc.versionsForObject(urn)

		assert vfo.size() == 2
		def result = vfo.find{ it.toString() == test1}
		assert result
		result = vfo.find{ it.toString() == test2}
		assert result

  }

  // Simple object example, urn with version-string, should work
  @Test
  void testVersionsForObject3() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:venAsign.v1:1")
		String test1 = "urn:cite2:hmt:venAsign.v1:1"
    CcGraph cc = new CcGraph(sparql)

		ArrayList vfo = cc.versionsForObject(urn)

		assert vfo.size() == 1
		def result = vfo.find{ it.toString() == test1}
		assert result

  }

}
