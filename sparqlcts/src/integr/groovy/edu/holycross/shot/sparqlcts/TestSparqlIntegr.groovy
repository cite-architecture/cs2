package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.citeservlet.Sparql


class TestSparqlIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  String verbQuery = "SELECT DISTINCT ?v WHERE {?s ?v ?o . } "
  Integer expectedNumberVerbs = 15

  
  @Test
  void testSetup() {
    //    URL tst1 = new URL("http://localhost:8080/fuseki/documentation.html")

    URL tst1 = new URL("http://localhost:8080/fuseki/")
    //    URL tst1 = new URL("http://localhost:8080/")
    
    println "Try retrieving docs from ${tst1} "
    println tst1.getText("UTF-8")
    /*    
    Sparql sparql = new Sparql(baseUrl)
    String uri = System.getProperty('gretty.baseURI')
    System.err.println "GRETTY URI " + uri
    System.err.println "GRETTY context path " + System.getProperty('gretty.contextPath')

    
    def slurper = new groovy.json.JsonSlurper()
    println "RUN verbQuery " + verbQuery

    def parsedColls = slurper.parseText(sparql.getSparqlReply("application/json", verbQuery))
    def verbList = []
    parsedColls.results.bindings.each { b ->
      verbList.add(b.v.value)
    }
    assert verbList.size() == expectedNumberVerbs
    */
  }
  
}