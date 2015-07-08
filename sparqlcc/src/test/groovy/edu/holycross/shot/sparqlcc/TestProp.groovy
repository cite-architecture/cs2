package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CiteUrn

class TestProp extends GroovyTestCase {

  @Test
  void testConstructor() {
    CiteProperty stringProp = new CiteProperty("PropName", CiteProperty.PropertyType.STRING,"Label for property","Value of this property")
    assert stringProp

    assert shouldFail {
      CiteProperty badProp = new CiteProperty("PropName",CiteProperty.PropertyType.NUMBER,"Label for property","Value of this property")
    }
  }
  
}