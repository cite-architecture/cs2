package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn

class TestOhco2 extends GroovyTestCase {

  CtsUrn firstNode = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
  CtsUrn secondNode = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.2")
  CtsUrn thirdNode = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.3")
  CtsUrn rangeUrn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-1.3")
  
  @Test
  void testConstructor() {

	  Map tempMap1 = ['rangeNode':['urn':firstNode,'textContent':"μῆνιν κτλ"],'typeExtras':null]
	  Map tempMap2 = ['rangeNode':['urn':secondNode,'textContent':"οὐλομένην κτλ"],'typeExtras':null]
	  Map tempMap3 = ['rangeNode':['urn':thirdNode,'textContent':"πολλὰς δ᾽ κτλ"],'typeExtras':null]

	  ArrayList rangeNodes = new ArrayList()
	  rangeNodes.push(tempMap1)
	  Ohco2Node o2nodeLeaf = new Ohco2Node(firstNode, "Iliad 1.1", null, thirdNode, rangeNodes)
	  rangeNodes.push(tempMap2)
	  rangeNodes.push(tempMap3)
	  Ohco2Node o2nodeRange = new Ohco2Node(rangeUrn, "Iliad 1.1-1.3", null, null, rangeNodes)
    assert o2nodeLeaf
	assert o2nodeRange


	assert shouldFail {
      Ohco2Node nullText = new Ohco2Node(secondNode, "Node with no text -- very, very bad!", firstNode, thirdNode, null)
    }
  }
  
}
