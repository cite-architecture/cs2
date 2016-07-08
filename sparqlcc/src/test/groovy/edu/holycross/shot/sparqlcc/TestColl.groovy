package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.*
import edu.holycross.shot.prestochango.*

class TestColl extends GroovyTestCase {

 // Ordered colections MUST have a sequence value
 @Test void testConstructor1() {

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
	
	CiteProperty idProp = new CiteProperty("urn","citeurn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")
	CiteProperty orderedByProp = new CiteProperty("seq","number","sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]
	
	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"
	 
    CiteCollection cc = new CiteCollection(collUrn, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	assert cc
	assert cc.isValid()
 }
  
}
