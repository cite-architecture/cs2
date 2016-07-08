package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.citeservlet.Sparql

import edu.holycross.shot.prestochango.*


class TestSparqlIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  String verbQuery = "SELECT DISTINCT ?v WHERE {?s ?v ?o . } "
  Integer expectedNumberVerbs = 31 // just the number that happen to be present in the test data

	@Test
	void testTest() {
		assert true
	}

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
