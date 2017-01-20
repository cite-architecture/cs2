package edu.holycross.shot.graph


import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CiteUrn

class TestTriple extends GroovyTestCase {

  URI testSubj = new URI("urn:cite:hmt:VenAIliad_classifiedTokens.6")
  URI testVerbWithUriObj = new URI("cite:analyzes")
  URI testVerbWithIntegerObj = new URI("olo:item")
  URI testVerbWithFloatObj = new URI("hmt:value")
  URI testVerbWithStringObj = new URI("cite:transformsToString")
  Object testObjString = "·"
  Number testObjInteger = 2
  Number testObjFloat = 2.5
  Object testObjUri = new URI("urn:cts:greekLit:tlg0012.tlg001.msA:1.2@·[1]")

  String expectedStringWithStringObject = """<urn:cite:hmt:VenAIliad_classifiedTokens.6> <cite:transformsToString> "·" ."""
  String expectedStringWithUriObject = "<urn:cite:hmt:VenAIliad_classifiedTokens.6> <cite:analyzes> <urn:cts:greekLit:tlg0012.tlg001.msA:1.2@·[1]> ."
  String expectedStringWithIntegerObject = """<urn:cite:hmt:VenAIliad_classifiedTokens.6> <olo:item> 2 ."""
  String expectedStringWithFloatObject = """<urn:cite:hmt:VenAIliad_classifiedTokens.6> <hmt:value> 2.5 ."""

  String encodedUrn = "<urn:cts%3AgreekLit%3Atlg0012.tlg001.msA%3A2.1%40%E1%BC%B1%CF%80%CF%80%CE%BF%CE%BA%CE%BF%CF%81%CF%85%CF%83%CF%84%CE%B1%E1%BD%B6%5B1%5D>"

  @Test
  void testEncodeDecodeURI(){

	println "Encoded Urn"
	println encodedUrn
	println "Decoded"
	println URLDecoder.decode(encodedUrn,"UTF-8")

    Triple  t = new Triple(testSubj, testVerbWithUriObj, testObjUri)
	println "Original URI"
	println testObjUri
	println "Encoded URI"
	String enco = URLEncoder.encode(testObjUri.toString(), "UTF-8")
	println enco
	String deco = URLDecoder.decode(enco,"UTF-8")
	assert deco == testObjUri.toString()
	println "Triple Object"
	println t.obj
	println "Triple"
	println t
	assert t.toString() == expectedStringWithUriObject
  }

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
  void testConstructorWithIntegerObject() {
    Triple  t = new Triple(testSubj, testVerbWithIntegerObj, testObjInteger)
	assert t.toString() == expectedStringWithIntegerObject
  }

  @Test
  void testConstructorWithFloatObject() {
    Triple  t = new Triple(testSubj, testVerbWithFloatObj, testObjFloat)
	assert t.toString() == expectedStringWithFloatObject
  }

  @Test
  void testConstructorWithStringObject() {
    Triple  t = new Triple(testSubj, testVerbWithStringObj, testObjString)
	assert t.toString() == expectedStringWithStringObject
  }

  @Test
  void testConstructorWithUriObject() {
    Triple  t = new Triple(testSubj, testVerbWithUriObj, testObjUri)
	assert t.toString() == expectedStringWithUriObject
  }


}
