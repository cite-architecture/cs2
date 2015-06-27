package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn

class TestRangeNode extends GroovyTestCase {


      
  CtsUrn fullUrn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
  
  @Test
  void testConstructor() {
    RangeNode n1 = new RangeNode(fullUrn, "μῆνιν κτλ")
    assert n1
  }
  
}