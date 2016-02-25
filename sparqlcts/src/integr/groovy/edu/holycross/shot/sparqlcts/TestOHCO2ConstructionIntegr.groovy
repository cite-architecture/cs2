package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


/** Tests GPP request on a single leaf-node URN
 */
class TestOHCO2ConstructionIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)

  //CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1-1.5")

  // test 1.6-1.10
  // test 1.600-2.5

  
  @Test
  void testOhco2Node_version_leaf_Integr() {
		XMLUnit.setNormalizeWhitespace(true)
		// Doesn't work because of some fucked up business with versions of Saxon
		//XMLUnit.setIgnoreWhitespace(true)

		  String xmlString = """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="4">αἵ τ᾽ ἐπεὶ οὖν χειμῶνα φύγον καὶ ἀθέσφατον ὄμβρον </l>"""
		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:3.4")

		  Ohco2Node response = graph.getOhco2Node(urn)
		  assert response.leafNodes.size() == 1
		  println response

		  Diff xmlDiff = new Diff(xmlString, response.leafNodes[0]['rangeNode'].textContent)
		  assert xmlDiff.identical()
		  assert false
  }


  
}
