package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.*
import edu.holycross.shot.prestochango.*

class TestObj extends GroovyTestCase {

	 /* Make a collection */

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
	
	CiteUrn prevUrn = new CiteUrn("urn:cite:testNs:testColl.one.v1")
	CiteUrn nextUrn = new CiteUrn("urn:cite:testNs:testColl.three.v1")

	CiteProperty idProp = new CiteProperty("urn","citeurn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")
	CiteProperty orderedByProp = new CiteProperty("seq","number","sequence")
	CiteProperty booleanProp = new CiteProperty("trueOrFalse","boolean","a boolean property")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]
	
	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"
	 
    CiteCollection ccOrdered = new CiteCollection(collUrn, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)
    CiteCollection ccUnordered = new CiteCollection(collUrn, idProp, labelProp, null, nsAbbr, nsFull, collProps, extensions)

  @Test
  void testEmptyTest(){
	  assert true
  }

 @Test void testConstructor1() {

	/* Make some property values */

	def properties = ["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"]

    CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.one.v1")	

	CiteCollectionObject cco = new CiteCollectionObject(urn,ccUnordered,properties)

	assert cco

 }

 @Test void testConstructor2() {

	def properties = ["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"]

    CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.two.v1")	

	CiteCollectionObject cco = new CiteCollectionObject(urn,ccOrdered,properties,prevUrn,nextUrn)

	assert cco

 }
  
}
