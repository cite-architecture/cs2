package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestObjNodeIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)

  CtsUrn notionalUrn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
  

  @Test
  void testLeafObject() {
    Ohco2Node onode = graph.getLeafNodeObject(notionalUrn)
    println "for ${notionalUrn}, got ${onode}"
  }
  
}