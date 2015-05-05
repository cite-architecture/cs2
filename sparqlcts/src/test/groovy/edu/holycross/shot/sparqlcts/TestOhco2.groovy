package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn

class TestOhco2 extends GroovyTestCase {


  CtsUrn firstNode = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
  CtsUrn secondNode = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.2")
  CtsUrn thirdNode = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.3")
  
  @Test
  void testConstructor() {
    Ohco2Node o2node = new Ohco2Node(secondNode, firstNode, thirdNode, "οὐλομένην κτλ")
    assert o2node

    assert shouldFail {
      Ohco2Node nullText = new Ohco2Node(secondNode, firstNode, thirdNode, "")
    }


    Ohco2Node emptyPrev = new Ohco2Node(firstNode, null, secondNode,"μῆνιν κτλ")
    assert emptyPrev
  }
  
}