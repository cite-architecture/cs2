package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph

/** Tests GPP request on a single leaf-node URN
 */
class TestGppRangeIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)

  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.5")

  // test 1.6-1.10
  // test 1.600-2.5

  
  @Test
  void testRanges() {
    //Ohco2Node onode = graph.getLeafNodeObject(urn)
    //println "XML IS: " + onode.toXml()
  }


  
}