package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetPropertiesInCollectionIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String objUrn = "urn:cite2:hmt:venAsign.v1:1"
  String collUrn = "urn:cite2:hmt:pageroi.v1:"

  @Test
  void testTest(){
    assert true
  }

  // With Object Urn
  @Test
  void testWithObject() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(objUrn)
    ArrayList collProps = cc.getPropertiesInCollection(urn)
    assert collProps.size() == 5
    assert collProps[0].propertyName
    assert collProps[0].label
    assert collProps[0].propertyType
  }

  // With Collection URN
  @Test
  void testWithCollection(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    Cite2Urn urn = new Cite2Urn(collUrn)
    ArrayList collProps = cc.getPropertiesInCollection(urn)
    assert collProps.size() == 5
    assert collProps[0].propertyName
    assert collProps[0].label
    assert collProps[0].propertyType
  }

}
