package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.*
import edu.holycross.shot.prestochango.*

class TestProp extends GroovyTestCase {


  @Test
  void testImports(){

	  CtsUrn testcts = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
	  Cite2Urn testcite = new Cite2Urn("urn:cite2:hmt:venAsign.v1:1")
	  CiteProperty testProp = new CiteProperty("urn",CitePropertyType.CITE2_URN,"canonical id")
  }

  @Test
  void testConstructor1() {

		CiteProperty testProp = new CiteProperty("urn",CitePropertyType.CITE2_URN,"canonical id")
		assert testProp
		assert testProp.propertyName == "urn"
		assert testProp.propertyType.getLabel() == "CITE2 Object URN"
		assert testProp.label == "canonical id"

  }

  @Test
  void testConstructor2() {

		CiteProperty testProp = new CiteProperty("urn",CitePropertyType.CTS_URN,"canonical id")
		assert testProp
		assert testProp.propertyName == "urn"
		assert testProp.propertyType.getLabel() == "CTS URN"
		assert testProp.label == "canonical id"

  }

}
