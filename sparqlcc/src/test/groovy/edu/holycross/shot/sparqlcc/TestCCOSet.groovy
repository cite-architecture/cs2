package edu.holycross.shot.sparqlcc

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*

import static org.junit.Assert.*
import org.junit.Test



/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestCCOSet2 extends GroovyTestCase {

	/* Make a collection */

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
	String descr = "Test collection"
	CiteUrn prevUrn = new CiteUrn("urn:cite:testNs:testColl.one.v1")
	CiteUrn nextUrn = new CiteUrn("urn:cite:testNs:testColl.three.v1")

	CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE_URN,"canonical id")
	CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")
	CiteProperty orderedByProp = new CiteProperty("seq",CitePropertyType.NUM,"sequence")
	CiteProperty booleanProp = new CiteProperty("trueOrFalse",CitePropertyType.BOOLEAN,"a boolean property")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]

	String orderedProp = "seq"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

	CiteCollection ccOrdered = new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	CiteCollection ccUnordered = new CiteCollection(collUrn,descr, idProp, labelProp, null, nsAbbr, nsFull, collProps, extensions)

	@Test void testConstructor1() {

		// Make some property values

		def properties = ["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.one.v1")

		CiteCollectionObject cco = new CiteCollectionObject(urn,ccUnordered,properties)

		assert cco
	}



	@Test void testObject1(){
		// Make some property values

		def properties = ["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.one.v1")

		CiteCollectionObject cco = new CiteCollectionObject(urn,ccOrdered,properties)

		assert cco
		assert cco.getSequence() == 1
	}

	@Test void testObject2(){
		// Make some property values

		def properties = ["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"2"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.two.v1")

		CiteCollectionObject cco = new CiteCollectionObject(urn,ccOrdered,properties)

		assert cco
		assert cco.getSequence() == 2
	}

	@Test void testObjects(){
		// Make some property values

		def properties1 = ["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"]
		def properties2 = ["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"2"]
		def properties3 = ["urn":"urn:cite:testNs:testColl.three.v1","label":"object 3","seq":"3"]

		CiteUrn urn1 = new CiteUrn("urn:cite:testNs:testColl.one.v1")
		CiteUrn urn2 = new CiteUrn("urn:cite:testNs:testColl.two.v1")
		CiteUrn urn3 = new CiteUrn("urn:cite:testNs:testColl.three.v1")

		CiteCollectionObject cco1 = new CiteCollectionObject(urn1,ccOrdered,properties1)
		CiteCollectionObject cco2 = new CiteCollectionObject(urn2,ccOrdered,properties2)
		CiteCollectionObject cco3 = new CiteCollectionObject(urn3,ccOrdered,properties3)

		assert cco1
		assert cco1.getSequence() == 1
		assert cco2
		assert cco2.getSequence() == 2
		assert cco3
		assert cco3.getSequence() == 3
	}


	@Test void testCCOSet(){
		// Make some property values

		def properties1 = ["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"]
		def properties2 = ["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"2"]
		def properties3 = ["urn":"urn:cite:testNs:testColl.three.v1","label":"object 3","seq":"3"]

		CiteUrn urn1 = new CiteUrn("urn:cite:testNs:testColl.one.v1")
		CiteUrn urn2 = new CiteUrn("urn:cite:testNs:testColl.two.v1")
		CiteUrn urn3 = new CiteUrn("urn:cite:testNs:testColl.three.v1")

		CiteCollectionObject cco1 = new CiteCollectionObject(urn1,ccOrdered,properties1)
		CiteCollectionObject cco2 = new CiteCollectionObject(urn2,ccOrdered,properties2)
		CiteCollectionObject cco3 = new CiteCollectionObject(urn3,ccOrdered,properties3)

		assert cco1.getSequence() == 1
		assert cco2.getSequence() == 2
		assert cco3.getSequence() == 3

		def objectArray = [cco1,cco2,cco3]

		assert objectArray.size() == 3

		/* HERE WE MAKE A CCOSET!!! */
		CCOSet ccos = new CCOSet(ccOrdered,objectArray)

		assert ccos.urn.toString() == "urn:cite:testNs:testColl.one.v1-three.v1"
		assert ccos.startUrn.toString() == "urn:cite:testNs:testColl.one.v1"
		assert ccos.endUrn.toString() == "urn:cite:testNs:testColl.three.v1"

	}

	@Test void testCCOSet2(){
		// Make some property values

		def properties1 = ["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"]
		def properties2 = ["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"2"]
		def properties3 = ["urn":"urn:cite:testNs:testColl.three.v1","label":"object 3","seq":"3"]

		CiteUrn urn1 = new CiteUrn("urn:cite:testNs:testColl.one.v1")
		CiteUrn urn2 = new CiteUrn("urn:cite:testNs:testColl.two.v1")
		CiteUrn urn3 = new CiteUrn("urn:cite:testNs:testColl.three.v1")

		CiteCollectionObject cco1 = new CiteCollectionObject(urn1,ccUnordered,properties1)
		CiteCollectionObject cco2 = new CiteCollectionObject(urn2,ccUnordered,properties2)
		CiteCollectionObject cco3 = new CiteCollectionObject(urn3,ccUnordered,properties3)

		assert cco1.getPropertyValue("seq") == "1"
		assert cco3.getPropertyValue("seq") == "3"

		def objectArray = [cco1,cco3]

		assert objectArray.size() == 2

		/* HERE WE MAKE A CCOSET!!! */
		CCOSet ccos = new CCOSet(ccUnordered,objectArray)

		assert ccos.urn.toString() == "urn:cite:testNs:testColl.one.v1-three.v1"
		assert ccos.startUrn.toString() == "urn:cite:testNs:testColl.one.v1"
		assert ccos.endUrn.toString() == "urn:cite:testNs:testColl.three.v1"

	}

	@Test void testCCOSetTest(){ assert true }

}
