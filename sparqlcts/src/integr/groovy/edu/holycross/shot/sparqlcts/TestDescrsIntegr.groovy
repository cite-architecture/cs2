package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestDescrsIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)


  

  @Test
  void testVersion() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    String expectedLabel = "Homeric epic, Iliad, Venetus A"
    println "Got label of " + graph.getLabel(urn).size() + " chars"
    println "Expected " + expectedLabel.size()
    println "But crappy white space management is killing this."
  }
  
}