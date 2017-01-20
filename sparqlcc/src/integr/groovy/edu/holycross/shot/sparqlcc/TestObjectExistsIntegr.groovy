package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestObjectExistsIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"

  @Test
  void testTest() {
    assert true
	 }


  // Simple object example, should work
  @Test
  void testObjectExists1() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:msA.v1:1r")
    CcGraph cc = new CcGraph(sparql)

		assert cc.objectExists(urn)

  }

  // Simple object example, should work
  @Test
  void testObjectExists2() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:msA.v2:1r")
    CcGraph cc = new CcGraph(sparql)

		assert cc.objectExists(urn)

  }

  // Simple object example, should work
  @Test
  void testObjectExists3() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:vaimg.v1:VA088RN_0089")
    CcGraph cc = new CcGraph(sparql)

		assert cc.objectExists(urn)

  }

  // Object with subref. Should work.
  @Test
  void testObjectExists4() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:vaimg.v1:VA088RN_0089@0.1,0.1,0.1,0.1")
    CcGraph cc = new CcGraph(sparql)

		assert cc.objectExists(urn)

  }

  // Range. Should work
  @Test
  void testObjectExists5() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:venAsign.v1:2-3")
    CcGraph cc = new CcGraph(sparql)

		assert cc.objectExists(urn)

  }

  // Range with subref. Should work, even if subrefs are not implemented for this collection by any extension.
  @Test
  void testObjectExists6() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:venAsign.v1:2@abc-3@abc")
    CcGraph cc = new CcGraph(sparql)

		assert cc.objectExists(urn)

  }

  // Collection-level urn. Should throw an exception
  @Test
  void testObjectExists7() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:venAsign.v1:")
    CcGraph cc = new CcGraph(sparql)
		assert shouldFail{
			assert cc.objectExists(urn)
		}

  }

  // Non-exists
  @Test
  void testObjectExists8() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:venAsign.v1:1@abc-notAnObject@abc")
    CcGraph cc = new CcGraph(sparql)

		assert ( cc.objectExists(urn) == false )

  }

  // Non-exists
  @Test
  void testObjectExists9() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:venAsign.v1:1-notAnObject")
    CcGraph cc = new CcGraph(sparql)

		assert ( cc.objectExists(urn) == false )

  }

  // Non-exists
  @Test
  void testObjectExists10() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:venAsign.v1:notAnObject")
    CcGraph cc = new CcGraph(sparql)

		assert ( cc.objectExists(urn) == false )

  }

  // Notional object, should return false
  @Test
  void testObjectExists11() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:venAsign:1")
    CcGraph cc = new CcGraph(sparql)

		assert ( cc.objectExists(urn) == false )

  }

  // Non-exists
  @Test
  void testObjectExists12() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:venAsign.v1:notAnObject@abc-3@abc")
    CcGraph cc = new CcGraph(sparql)

		assert ( cc.objectExists(urn) == false )

  }

  // Why isn't this working?
  @Test
  void testObjectExists13() {
    Sparql sparql = new Sparql(baseUrl)
    Cite2Urn urn =  new Cite2Urn("urn:cite2:hmt:vaimg.v1:VA085RN_0086-VA087RN_0088")
    CcGraph cc = new CcGraph(sparql)

		assert cc.objectExists(urn)

  }





}
