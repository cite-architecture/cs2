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
		
		  assert response.nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.4"
		  assert response.prevUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.3"
		  assert response.nextUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.5"
		  assert response.nodeLabel == "Homeric Epic, Iliad (Allen's Iliad (test ed.)): 3.4 (urn:cts:greekLit:tlg0012.tlg001.testAllen:3.4)"
		  
		  assert response.leafNodes.size() == 1

		  Diff xmlDiff = new Diff(xmlString, response.leafNodes[0]['rangeNode'].textContent)
		  assert xmlDiff.identical()
  }

  @Test
  void testOhco2Node_version_range_Integr() {
		XMLUnit.setNormalizeWhitespace(true)
		// Doesn't work because of some fucked up business with versions of Saxon
		//XMLUnit.setIgnoreWhitespace(true)

		  String xmlString1 = """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="1">Αὐτὰρ ἐπεὶ κόσμηθεν ἅμ᾽ ἡγεμόνεσσιν ἕκαστοι, </l>"""		  
		  String xmlString2 = """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="2">Τρῶες μὲν κλαγγῇ τ᾽ ἐνοπῇ τ᾽ ἴσαν ὄρνιθες ὣς </l>"""		  
		  String xmlString3 = """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="3">ἠΰτε περ κλαγγὴ γεράνων πέλει οὐρανόθι πρό· </l>"""		  

		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:3.1-3.3")

		  Ohco2Node response = graph.getOhco2Node(urn)
		
		  assert response.nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.1-3.3"
		  assert response.prevUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:2.10"
		  assert response.nextUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.4"
		  assert response.nodeLabel == "Range request: 3.1-3.3, from Allen's Iliad (test ed.). (urn:cts:greekLit:tlg0012.tlg001.testAllen:3.1-3.3)."
		  
		  assert response.leafNodes.size() == 3

		  Diff xmlDiff1 = new Diff(xmlString1, response.leafNodes[0]['rangeNode'].textContent)
		  assert xmlDiff1.identical()
		  Diff xmlDiff2 = new Diff(xmlString2, response.leafNodes[1]['rangeNode'].textContent)
		  assert xmlDiff2.identical()
		  Diff xmlDiff3 = new Diff(xmlString3, response.leafNodes[2]['rangeNode'].textContent)
		  assert xmlDiff3.identical()
  }

  
}
