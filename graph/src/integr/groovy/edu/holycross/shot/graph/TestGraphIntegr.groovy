package edu.holycross.shot.graph

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcts.Sparql
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn


class TestGraphIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/graph/query"
  String verbQuery = "SELECT distinct ?v WHERE { ?s ?v <urn:cts:greekLit:tlg0012.tlg001.msA:1.1> .  }"
  Integer expectedNumberVerbs = 7


  @Test
  void testSetup() {
    Sparql sparql = new Sparql(baseUrl)
    String uri = System.getProperty('gretty.baseURI')
    def slurper = new groovy.json.JsonSlurper()
    println "RUN verbQuery " + verbQuery

    def parsedColls = slurper.parseText(sparql.getSparqlReply("application/json", verbQuery))

	assert parsedColls

	def verbList = []
	parsedColls.results.bindings.each { b ->
		verbList.add(b.v.value)
	}
	assert verbList.size() == expectedNumberVerbs
  }


}
