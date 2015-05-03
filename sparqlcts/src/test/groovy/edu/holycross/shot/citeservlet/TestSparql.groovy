package edu.holycross.shot.citeservlet

import static org.junit.Assert.*
import org.junit.Test
  
class TestSparql extends GroovyTestCase {

  // remember how to pass in property values to
  // test case...?
  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  String verbs = "SELECT DISTINCT ?v WHERE {?s ?v ?o . } "
  Integer expectedNumberVerbs = 15
  
  @Test
  void testSetup() {
    Sparql sparql = new Sparql(baseUrl)
    def slurper = new groovy.json.JsonSlurper()
    def parsedColls = slurper.parseText(sparql.getSparqlReply("application/json", verbs))
    println "PARSED: " + parsedColls
    def verbList = []
    parsedColls.results.bindings.each { b ->
      verbList.add(b.v.value)
    }
    assert verbList.size() == expectedNumberVerbs
  }
  
}