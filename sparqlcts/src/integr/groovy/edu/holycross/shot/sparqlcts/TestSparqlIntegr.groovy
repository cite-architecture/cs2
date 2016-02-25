package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.citeservlet.Sparql


class TestSparqlIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  String verbQuery = "SELECT DISTINCT ?v WHERE {?s ?v ?o . } "
  Integer expectedNumberVerbs = 20 

  
  @Test
  void testSetup() {
    Sparql sparql = new Sparql(baseUrl)
    String uri = System.getProperty('gretty.baseURI')
    def slurper = new groovy.json.JsonSlurper()
    println "RUN verbQuery " + verbQuery

    def parsedColls = slurper.parseText(sparql.getSparqlReply("application/json", verbQuery))
    def verbList = []
    parsedColls.results.bindings.each { b ->
      verbList.add(b.v.value)
    }
    assert verbList.size() == expectedNumberVerbs
  }
  
}
