package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn

class TestQBuilder extends GroovyTestCase {


  
  @Test
  void testVersionDesc() {
    CtsUrn firstNode = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    println "For ${firstNode}, " + QueryBuilder.getVersionDescrQuery(firstNode)
  }

  
  @Test
  void testLeafNodeTextQ() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:2.1")

    String query =   QueryBuilder.getLeafNodeTextQuery(urn,0)
    println "For ${urn}, " + query
  }


  
}