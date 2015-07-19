package edu.holycross.shot.graph


import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CiteUrn

class TestTriple extends GroovyTestCase {

  URI testSubj = new URI("urn:cite:hmt:VenAIliad_classifiedTokens.6")
  URI testVerbWithUriObj = new URI("cite:analyzes")
  URI testVerbWithStringObj = new URI("cite:transformsToString")
  Object testObjString = "·"
  Number testObjInteger = 2
  Number testObjFloat = 2.5
  Object testObjUri = new URI("urn:cts:greekLit:tlg0012.tlg001.msA:1.2@·[1]")

  String expectedStringWithStringObject = """<urn:cite:hmt:VenAIliad_classifiedTokens.6> <cite:transformsToString> "·" ."""
  String expectedStringWithUriObject = "<urn:cite:hmt:VenAIliad_classifiedTokens.6> <cite:analyzes> <urn:cts:greekLit:tlg0012.tlg001.msA:1.2@·[1]> ."

  @Test
  void testObjectTypes() {
	  assert testSubj.getClass() == URI
	  assert testVerbWithUriObj.getClass() == URI
	  assert testVerbWithStringObj.getClass() == URI
	  assert testObjString.getClass() == String
	  assert testObjUri.getClass() == URI
	  assert testObjInteger.toString().isNumber()
	  assert testObjFloat.toString().isNumber()
  }

  @Test
  void testConstructorWithIngeterObject() {
    Triple  t = new Triple(testSubj, testVerbWithStringObj, testObjInteger)
  }

  @Test
  void testConstructorWithFloatObject() {
    Triple  t = new Triple(testSubj, testVerbWithStringObj, testObjFloat)
  }

  @Test
  void testConstructorWithStringObject() {
    Triple  t = new Triple(testSubj, testVerbWithStringObj, testObjString)
  }

  @Test
  void testConstructorWithUriObject() {
    Triple  t = new Triple(testSubj, testVerbWithUriObj, testObjUri)
  }

  @Test
  void testStringOutputWithUriObject() {
    Triple  t = new Triple(testSubj, testVerbWithUriObj, testObjUri)
	assert t.toString() == expectedStringWithUriObject
  }

  @Test
  void testStringOutputWithStringObject() {
    Triple  t = new Triple(testSubj, testVerbWithStringObj, testObjString)
	assert t.toString() == expectedStringWithStringObject
  }
  
}
