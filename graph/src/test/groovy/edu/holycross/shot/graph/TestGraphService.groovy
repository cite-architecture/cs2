package edu.holycross.shot.graph

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.graph.GraphService

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

class TestGraphService extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/graph/query"
  String citeSubj = "urn:cite:hmt:VenAIliad_classifiedTokens.6"


  @Test
  void testConstructor() {
      Sparql sparql = new Sparql(baseUrl)
	  GraphService gs = new GraphService(sparql)
  }

  
}
