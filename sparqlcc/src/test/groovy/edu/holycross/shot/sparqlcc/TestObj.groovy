package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CiteUrn

class TestObj extends GroovyTestCase {

  CiteUrn urn = new CiteUrn("urn:cite:hmt:msA.12r")
  CiteProperty rvProp = new CiteProperty("rv", CiteProperty.PropertyType.STRING,"Recto or verso","recto")
  CiteProperty labelProp = new CiteProperty("label", CiteProperty.PropertyType.STRING,"Description","Venetus A (Marciana 454 = 822), folio 12, recto")

  
  @Test
  void testConstructor() {
    ArrayList propList = [rvProp, labelProp]
    CiteObject cobj = new CiteObject(urn, propList)
    assert cobj
  }


  @Test
  void testBad() {
    ArrayList bogusList = ["nonsense", -1]
    assert shouldFail {
      CiteObject cobj = new CiteObject(urn, bogusList)
    }
  }
  
}