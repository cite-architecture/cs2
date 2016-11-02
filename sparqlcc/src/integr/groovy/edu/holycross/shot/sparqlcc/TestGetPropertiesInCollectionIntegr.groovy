package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetPropertiesInCollectionIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String objUrn = "urn:cite:hmt:venAsign.1.v1"
  String collUrn = "urn:cite:hmt:pageroi"

  @Test
  void testTest(){
    assert true
  }

  // With Object Urn
  @Test
  void testWithObject() {
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)
    CiteUrn urn = new CiteUrn(objUrn)
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
    CiteUrn urn = new CiteUrn(collUrn)
    ArrayList collProps = cc.getPropertiesInCollection(urn)
    assert collProps.size() == 5
    assert collProps[0].propertyName
    assert collProps[0].label
    assert collProps[0].propertyType
  }

}
