package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetCollectionIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testGetCollectionIdProp(){
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:")
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteProperty prop = cc.getCollectionIdProp(urn)
    assert prop.propertyName == "OccurrenceUrn"
	  assert prop.propertyType == CitePropertyType.CITE2_URN
    assert prop.label.size() > 0
  }

  @Test
  void testGetCollectionLabelProp(){
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:")
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteProperty prop = cc.getCollectionLabelProp(urn)
    assert prop.propertyName == "Label"
    assert prop.propertyType == CitePropertyType.STRING
    assert prop.label.size() > 0
  }

  @Test
  void testGetCollectionOrderedByProp(){
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:")
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteProperty prop = cc.getCollectionOrderedByProp(urn)
    assert prop.propertyName == "Sequence"
    assert prop.propertyType == CitePropertyType.NUM
    assert prop.label.size() > 0
  }

  @Test
  void testGetCollectionOrderedByProp4unordered(){
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    shouldFail {
      CiteProperty prop = cc.getCollectionOrderedByProp(urn)
    }
  }

  @Test
  void testGetCollectionExtensions(){
    Cite2Urn urn = new Cite2Urn("urn:cite:hmt:vaimg")
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    ArrayList exts = cc.getCollectionExtensions(urn)
    assert exts.size() == 1
    assert exts[0] == "http://www.homermultitext.org/cite/rdf/CiteImage"
  }

  @Test
  void testGetCollectionExtensions2(){
    // no extensions
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:")
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    ArrayList exts = cc.getCollectionExtensions(urn)
    assert exts.size() == 0
  }

  @Test
  void testOrdered(){
      Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:1")
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
      CiteCollection coll = cc.getCollection(urn)
      assert coll.isValid()
  }

  @Test
  void testUnordered(){
      Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:1")
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
      CiteCollection coll = cc.getCollection(urn)
      assert coll.isValid()
  }


}
