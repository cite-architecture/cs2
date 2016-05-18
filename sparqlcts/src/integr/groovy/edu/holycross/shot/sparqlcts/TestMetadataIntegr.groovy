package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestMetadataIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)


  

  @Test
  void testType_edition() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    String expectedVersion = "Edition"
    String actualVersion = graph.getVersionType(urn)
    assert actualVersion == expectedVersion
    
  }

  @Test
  void testType_edition_exemplar() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.1")
    String expectedVersion = "Edition"
    String actualVersion = graph.getVersionType(urn)
    assert actualVersion == expectedVersion
    
  }

  @Test
  void testType_translation() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest:1.1")
    String expectedVersion = "Translation"
    String actualVersion = graph.getVersionType(urn)
    assert actualVersion == expectedVersion
    
  }

  @Test
  void testType_translation_exemplar() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest.wt:1.1")
    String expectedVersion = "Translation"
    String actualVersion = graph.getVersionType(urn)
    assert actualVersion == expectedVersion
    
  }

  @Test
  void testType_work() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001:1.1")
    String expectedVersion = ""
    String actualVersion = graph.getVersionType(urn)
    assert actualVersion == expectedVersion
    
  }

  @Test
  void testLang_edition(){
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    String expectedLang = "grc"
    String actualLang = graph.getLanguage(urn)
    assert actualLang == expectedLang
  }

  @Test
  void testLang_translation(){
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest:1.1")
    String expectedLang = "eng"
    String actualLang = graph.getLanguage(urn)
    assert actualLang == expectedLang
  }

  @Test
  void testLang_edition_exemp(){
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:1.1")
    String expectedLang = "grc"
    String actualLang = graph.getLanguage(urn)
    assert actualLang == expectedLang
  }

  @Test
  void testLang_translation_exemp(){
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001.engTest.wt:1.1")
    String expectedLang = "eng"
    String actualLang = graph.getLanguage(urn)
    assert actualLang == expectedLang
  }
}
