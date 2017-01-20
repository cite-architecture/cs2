package edu.holycross.shot.graph

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcts.Sparql
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

class TestIndexGraph extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/graph/query"
  String citeSubj = "urn:cite:hmt:VenAIliad_classifiedTokens.6"


  @Test
  void testConstructor() {
      Sparql sparql = new Sparql(baseUrl)
	    IndexGraph ig = new IndexGraph(sparql)
  }


}
