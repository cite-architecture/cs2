package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.harvard.chs.cite.*
import edu.holycross.shot.prestochango.*

class TestProp extends GroovyTestCase {


  @Test
  void testImports(){
	  
	  CtsUrn testcts = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
	  CiteUrn testcite = new CiteUrn("urn:cite:hmt:venAsign.1")
	  CiteProperty testProp = new CiteProperty("urn","citeurn","canonical id")
  }
	
  @Test
  void testConstructor() {
	  	
		CiteProperty testProp = new CiteProperty("urn","citeurn","canonical id")
		assert testProp
		assert testProp.propertyName == "urn"
		assert testProp.propertyType == "citeurn"
		assert testProp.label == "canonical id"

  }
  
}
