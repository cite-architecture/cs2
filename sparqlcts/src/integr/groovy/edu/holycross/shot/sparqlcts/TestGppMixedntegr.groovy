package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph

/** Tests GPP request on a range URN where one is a 
 * leaf, and one is a container
 */
class TestGppMixedIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)

  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.10-2")

  
  @Test
  void testContainer() {
    //Ohco2Node onode = graph.getLeafNodeObject(urn)
    //println "XML IS: " + onode.toXml()
  }


  
}
