package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetCollectionIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"


  @Test
  void testTest(){
    assert false
  }

  @Test
  void testGetCollectionIdProp(){
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign")
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteProperty prop = cc.getCollectionIdProp(urn)
    assert prop.propertyName == "OccurrenceUrn"
    assert prop.propertyType == "string"
    assert prop.label.size() > 0
  }

  @Test
  void testGetCollectionLabelProp(){
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign")
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteProperty prop = cc.getCollectionLabelProp(urn)
    assert prop.propertyName == "CollectionLabel"
    assert prop.propertyType == "string"
    assert prop.label.size() > 0
  }

  @Test
  void testGetCollectionOrderedByProp(){
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign")
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    CiteProperty prop = cc.getCollectionOrderedByProp(urn)
    assert prop.propertyName == "Sequence"
    assert prop.propertyType == "number"
    assert prop.label.size() > 0
  }

  @Test
  void testGetCollectionExtensions(){
    CiteUrn urn = new CiteUrn("urn:cite:hmt:vaimg")
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    ArrayList exts = cc.getCollectionExtensions(urn)
    assert exts.size() == 1
    assert exts[0] == "cite:CiteImage"
  }

  @Test
  void testGetCollectionExtensions2(){
    // no extensions
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign")
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cc = new CcGraph(sparql)
    ArrayList exts = cc.getCollectionExtensions(urn)
    assert exts.size() == 0
  }

  @Test
  void testOrdered(){
      CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign.1")
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
      CiteCollection coll = cc.getCollection(urn)
      assert cc.isValid()
  }

  @Test
  void testUnordered(){
      CiteUrn urn = new CiteUrn("urn:cite:hmt:pageroi.1")
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
      CiteCollection coll = cc.getCollection(urn)
      assert cc.isValid()
  }


}
