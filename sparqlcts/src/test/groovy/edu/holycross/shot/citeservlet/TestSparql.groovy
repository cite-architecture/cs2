package edu.holycross.shot.citeservlet

import static org.junit.Assert.*
import org.junit.Test
  
class TestSparqlInteg extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  String verbQuery = "SELECT DISTINCT ?v WHERE {?s ?v ?o . } "
  Integer expectedNumberVerbs = 15
  
  @Test
  void testSetup() {
    Sparql sparql = new Sparql(baseUrl)
    def slurper = new groovy.json.JsonSlurper()
    def parsedColls = slurper.parseText(sparql.getSparqlReply("application/json", verbQuery))
    def verbList = []
    parsedColls.results.bindings.each { b ->
      verbList.add(b.v.value)
    }
    assert verbList.size() == expectedNumberVerbs
  }
  
}