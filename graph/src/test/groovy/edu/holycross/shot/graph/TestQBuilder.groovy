package edu.holycross.shot.graph

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

class TestQBuilder extends GroovyTestCase {


  URI citeSubj = new URI("urn:cite:hmt:VenAIliad_classifiedTokens.6")
  URI ctsSubj = new URI("urn:cts:greekLit:tlg0012.tlg001.msA:1.2")
  
  @Test
  void testExampleQuery() {
    CiteUrn urn = new CiteUrn(citeSubj.toString())
    println "For ${urn}, " + QueryBuilder.getExampleQuery(urn)
  }

  @Test
  void testLabelQueryCite() {
    String urnStr = citeSubj.toString()
    println QueryBuilder.labelQuery(urnStr)
  }

  @Test
  void testLabelQueryCts() {
    String urnStr = ctsSubj.toString()
    println QueryBuilder.labelQuery(urnStr)
  }

  
}
