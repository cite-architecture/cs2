package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn

class TestOhco2Range extends GroovyTestCase {

  // test on notional work
  CtsUrn rangeUrn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1-1.2")
  CtsUrn prevUrn = null

  CtsUrn firstUrn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
  CtsUrn secondUrn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.2")
    
  CtsUrn nextUrn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.3")

  @Test
  void testConstructor() {
    RangeNode n1 = new RangeNode(firstUrn, "μῆνιν κτλ")
    RangeNode n2 = new RangeNode(secondUrn, "οὐλομένην κτλ")
    assert n1
    assert n2
    ArrayList rangeNodes = [n1, n2]

    
    Ohco2Range o2range = new Ohco2Range(rangeUrn, "Iliad 1.1-1.2, notionally",prevUrn,nextUrn,rangeNodes)

    assert o2range
    
  }
  
}