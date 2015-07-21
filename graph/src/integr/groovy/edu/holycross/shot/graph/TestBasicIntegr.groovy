package edu.holycross.shot.graph

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql


class TestBasicIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/graph/query"
  Sparql sparql = new Sparql(baseUrl)
  GraphService gs = new GraphService(sparql)


  @Test
  void testBasic() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.2")
 	println gs.graph.findAdjacent(urn)   
  }
  
}
