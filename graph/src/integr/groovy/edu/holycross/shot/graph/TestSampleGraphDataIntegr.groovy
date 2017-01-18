package edu.holycross.shot.graph

import static org.junit.Assert.*
import org.junit.Test


import edu.holycross.shot.sparqlcts.Sparql
import edu.harvard.chs.cite.CtsUrn


class TestSampleGraphDataIntegr extends GroovyTestCase {


	ArrayList getMasterVerbList(){
		String baseUrl = "http://localhost:8080/fuseki/verbs/query"
		String verbQuery = "SELECT distinct ?s WHERE { ?s ?v ?o .  }"

		Sparql sparql = new Sparql(baseUrl)
		String uri = System.getProperty('gretty.baseURI')
		def slurper = new groovy.json.JsonSlurper()

		def parsedColls = slurper.parseText(sparql.getSparqlReply("application/json", verbQuery))


		ArrayList verbList = []
		parsedColls.results.bindings.each { b ->
			verbList.add(b.s.value)
		}
		return verbList

	}


  @Test
  void testMasterVerbs() {
  String baseUrl = "http://localhost:8080/fuseki/verbs/query"
  String verbQuery = "SELECT distinct ?s WHERE { ?s ?v ?o .  }"
  Integer expectedNumberVerbs = 60

  Sparql sparql = new Sparql(baseUrl)
  String uri = System.getProperty('gretty.baseURI')
  def slurper = new groovy.json.JsonSlurper()

  def parsedColls = slurper.parseText(sparql.getSparqlReply("application/json", verbQuery))

   assert parsedColls

	def verbList = []
	parsedColls.results.bindings.each { b ->
		verbList.add(b.s.value)
	}
	assert verbList.size() == expectedNumberVerbs
  }


	@Test
	void testGetVerbList(){
		ArrayList verbList = getMasterVerbList()
		assert verbList.size() == 60
	}

	@Test
	void testSampleGraphData(){
		ArrayList masterVerbList = getMasterVerbList()
		// Get verbs from graph data
		String baseUrl = "http://localhost:8080/fuseki/graph/query"
		String verbQuery = "SELECT distinct ?v WHERE { ?s ?v ?o .  }"
		Sparql sparql = new Sparql(baseUrl)
		String uri = System.getProperty('gretty.baseURI')
		def slurper = new groovy.json.JsonSlurper()

		def parsedColls = slurper.parseText(sparql.getSparqlReply("application/json", verbQuery))

		assert parsedColls

		def verbList = []
		parsedColls.results.bindings.each { b ->
			if( !(b.v.value.contains("/citedata/")) ){
				verbList.add(b.v.value)
			}
		}

		assert verbList.size() == masterVerbList.size()
		assert verbList as Set == masterVerbList as Set

	}




}
