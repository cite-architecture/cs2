package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test
import groovy.json.*
import org.custommonkey.xmlunit.*
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


/** Tests GPP request on a single leaf-node URN
 */
class TestOHCO2ConstructionIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ctsTest/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)


  @Test
  void testOhco2_json_Integr(){

		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.9-2.1")
		  Ohco2Node response = graph.getOhco2Node(urn)
		  println "---------------------------------------------------"
		  println "JSON Output for constructed Ohco2Node for <${urn}>."
		  println "---------------------------------------------------"
		  println response.toJson()
		  println "---------------------------------------------------"
		  assert response
  }
  
  @Test
  void testOhco2Node_version_leaf_Integr() {
		XMLUnit.setNormalizeWhitespace(true)

		  String xmlString = """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="4">αἵ τ᾽ ἐπεὶ οὖν χειμῶνα φύγον καὶ ἀθέσφατον ὄμβρον </l>"""
		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:3.4")

		  Ohco2Node response = graph.getOhco2Node(urn)
		
		  assert response.nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.4"
		  assert response.prevUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.3"
		  assert response.nextUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.5"
		  assert response.nodeLabel == "Homeric Epic, Iliad (Allen's Iliad (test ed.)): 3.4 (urn:cts:greekLit:tlg0012.tlg001.testAllen:3.4)"
		  assert response.nodeLang == "grc"
		  
		  assert response.leafNodes.size() == 1

		  Diff xmlDiff = new Diff(xmlString, response.leafNodes[0]['rangeNode'].textContent)
		  assert xmlDiff.identical()
  }

  @Test
  void testOhco2Node_version_range_Integr() {
		XMLUnit.setNormalizeWhitespace(true)

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

  @Test
  void testOhco2Node_work_leaf_Integr() {
		XMLUnit.setNormalizeWhitespace(true)

		  String xmlString = """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="4">αἵ τ᾽ ἐπεὶ οὖν χειμῶνα φύγον καὶ ἀθέσφατον ὄμβρον </l>"""
		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:3.4")

		  Ohco2Node response = graph.getOhco2Node(urn)
		
		  assert response.nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.4"
		  assert response.leafNodes[0]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.4"
		  assert response.prevUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.3"
		  assert response.nextUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.5"
		  assert response.nodeLabel == "Homeric Epic, Iliad (Allen's Iliad (test ed.)): 3.4 (urn:cts:greekLit:tlg0012.tlg001.testAllen:3.4)"
		  
		  assert response.leafNodes.size() == 1

		  Diff xmlDiff = new Diff(xmlString, response.leafNodes[0]['rangeNode'].textContent)
		  assert xmlDiff.identical()
  }
  
  @Test
  void testOhco2Node_work_range_Integr() {
		XMLUnit.setNormalizeWhitespace(true)

		  String xmlString1 = """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="1">Αὐτὰρ ἐπεὶ κόσμηθεν ἅμ᾽ ἡγεμόνεσσιν ἕκαστοι, </l>"""		  
		  String xmlString2 = """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="2">Τρῶες μὲν κλαγγῇ τ᾽ ἐνοπῇ τ᾽ ἴσαν ὄρνιθες ὣς </l>"""		  
		  String xmlString3 = """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="3">ἠΰτε περ κλαγγὴ γεράνων πέλει οὐρανόθι πρό· </l>"""		  

		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:3.1-3.3")

		  Ohco2Node response = graph.getOhco2Node(urn)
		
		  assert response.nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.1-3.3"
		  assert response.prevUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:2.10"
		  assert response.nextUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.4"
		  assert response.nodeLabel == "Range request: 3.1-3.3, from Allen's Iliad (test ed.). (urn:cts:greekLit:tlg0012.tlg001.testAllen:3.1-3.3)."
		  
		  assert response.leafNodes.size() == 3

		  assert response.leafNodes[0]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.1"
		  assert response.leafNodes[1]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.2"
		  assert response.leafNodes[2]['rangeNode'].nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen:3.3"
		  
		  Diff xmlDiff1 = new Diff(xmlString1, response.leafNodes[0]['rangeNode'].textContent)
		  assert xmlDiff1.identical()
		  Diff xmlDiff2 = new Diff(xmlString2, response.leafNodes[1]['rangeNode'].textContent)
		  assert xmlDiff2.identical()
		  Diff xmlDiff3 = new Diff(xmlString3, response.leafNodes[2]['rangeNode'].textContent)
		  assert xmlDiff3.identical()
  }

  @Test
  void testOhco2Node_translation_exemp_leaf_Integr() {
		XMLUnit.setNormalizeWhitespace(true)

		  String xmlString = """<w xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="1">αἵ</w>"""
		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:3.4.1")

		  Ohco2Node response = graph.getOhco2Node(urn)
		
		  assert response.nodeUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:3.4.1"
		  assert response.prevUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:3.3.7"
		  assert response.nextUrn.toString() == "urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:3.4.2"
		  assert response.nodeLabel == "Homeric Epic, Iliad (Allen. Iliad. Greek. 3 books of 10 lines. Word-tokens wrapped and citable.): 3.4.1 (urn:cts:greekLit:tlg0012.tlg001.testAllen.wt:3.4.1)"
		  assert response.nodeLang == "grc"
		  
		  assert response.leafNodes.size() == 1

		  Diff xmlDiff = new Diff(xmlString, response.leafNodes[0]['rangeNode'].textContent)
		  assert xmlDiff.identical()
  }

  @Test
  void testOhco2Node_nodeLang1() {

		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:3.1-3.3")

		  Ohco2Node response = graph.getOhco2Node(urn)
	      assert response.nodeLang == "grc"

  }

  @Test
  void testOhco2Node_nodeLang2() {

		  CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0016.tlg001:3.3")

		  Ohco2Node response = graph.getOhco2Node(urn)
	      assert response.nodeLang == "eng"

  }

  @Test
  void testOhco2Node_1Level_leaf_Integr() {
		XMLUnit.setNormalizeWhitespace(true)

		  String xmlString = """<l xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0" n="3">Er hat den Knaben wohl in dem Arm,</l>"""
		  CtsUrn urn = new CtsUrn("urn:cts:hmtDemo:goethe.erlkoenig.deu:3")

		  Ohco2Node response = graph.getOhco2Node(urn)
		
		  assert response.nodeUrn.toString() == "urn:cts:hmtDemo:goethe.erlkoenig.deu:3"
		  assert response.prevUrn.toString() == "urn:cts:hmtDemo:goethe.erlkoenig.deu:2" 
		  assert response.nextUrn.toString() == "urn:cts:hmtDemo:goethe.erlkoenig.deu:4"
		  assert response.nodeLabel == """Johann Wolfgang von Goethe, Der Erlkönig (German (1782)): 3 (urn:cts:hmtDemo:goethe.erlkoenig.deu:3)"""
		  assert response.nodeLang == "deu"
		  
		  assert response.leafNodes.size() == 1

		  Diff xmlDiff = new Diff(xmlString, response.leafNodes[0]['rangeNode'].textContent)
		  assert xmlDiff.identical()

	      println new JsonBuilder(response).toPrettyString()

  }

}


