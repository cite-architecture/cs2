package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestResolveVersionIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  CiteUrn versionedUrn =  new CiteUrn("urn:cite:hmt:vaimg.VA085RN_0086.v1")
  CiteUrn versionedRangeUrn =      new CiteUrn("urn:cite:hmt:vaimg.VA085RN_0086.v1-VA085VN_0087.v1")
  CiteUrn extendedUrn =   new CiteUrn("urn:cite:hmt:vaimg.VA085RN_0086.v1@12,12,12,12")
  CiteUrn unversionedExtendedUrn =   new CiteUrn("urn:cite:hmt:vaimg.VA085RN_0086@12,12,12,12")
  CiteUrn objectUrn =     new CiteUrn("urn:cite:hmt:vaimg.VA085RN_0086")
  CiteUrn collectionUrn = new CiteUrn("urn:cite:hmt:vaimg")
  CiteUrn rangeUrn =      new CiteUrn("urn:cite:hmt:vaimg.VA085RN_0086-VA085VN_0087")
  CiteUrn notInDataUrn =      new CiteUrn("urn:cite:hmt:nonColl.noObject1")

  @Test
  void testTest() {
    assert true
  }


  // Simple object example, should work
  @Test
  void testResolveVersion1() {
    Sparql sparql = new Sparql(baseUrl)
	CcGraph cc = new CcGraph(sparql)

    assert cc.resolveVersion(objectUrn).toString() == versionedUrn.toString()
  }
  // Has a version. Should work
  @Test
  void testResolveVersion2() {
    Sparql sparql = new Sparql(baseUrl)
	CcGraph cc = new CcGraph(sparql)

    assert cc.resolveVersion(versionedUrn).toString() == versionedUrn.toString()
  }
  // Range URN. Should work
  @Test
  void testResolveVersion3() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    assert cc.resolveVersion(rangeUrn).toString() == versionedRangeUrn.toString()
  }



  // Should work
  @Test
  void testVersionForObject1() {
    Sparql sparql = new Sparql(baseUrl)
	CcGraph cc = new CcGraph(sparql)

    assert cc.versionForObject(objectUrn).toString() == versionedUrn.toString()
  }

  // Range URN throws an exception
  @Test
  void testVersionForObject2() {
    Sparql sparql = new Sparql(baseUrl)
	CcGraph cc = new CcGraph(sparql)

	shouldFail {
		assert cc.versionForObject(rangeUrn).toString() == rangeUrn.toString()
	}
  }

  // Collection-level URN throws an exception
  @Test
  void testVersionForObject3() {
    Sparql sparql = new Sparql(baseUrl)
	CcGraph cc = new CcGraph(sparql)

	shouldFail {
		assert cc.versionForObject(collectionUrn).toString() == collectionUrn.toString()
	}
  }

  // Urn with version, returns itself
  @Test
  void testVersionForObject4() {
    Sparql sparql = new Sparql(baseUrl)
	CcGraph cc = new CcGraph(sparql)

    assert cc.versionForObject(versionedUrn).toString() == versionedUrn.toString()
  }


  // Urn with extended ref; invalid without version, so should fail
  @Test
  void testVersionForObject5() {
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)

    shouldFail {
      assert cc.versionForObject(unversionedExtendedUrn).toString() == extendedUrn.toString()
    }
  }

  	// Good urn, but not in data, returns null
  @Test
  void testVersionForObject6() {
    Sparql sparql = new Sparql(baseUrl)
	CcGraph cc = new CcGraph(sparql)

	assert cc.versionForObject(notInDataUrn) == null
  }

}
