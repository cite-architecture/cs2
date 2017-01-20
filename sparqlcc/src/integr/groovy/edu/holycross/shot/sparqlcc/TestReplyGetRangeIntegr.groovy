package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestReplyGetRangeIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testGetRangeOrdered1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetRange"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:5-7")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetRange xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite2:hmt:venAsign.v1:5-7</requestUrn>
    <request>GetRange</request>
    <resolvedUrn>urn:cite2:hmt:venAsign.v1:5-7</resolvedUrn>
</cite:request>
<cite:reply>
<citeObjects>
<citeObject urn="urn:cite2:hmt:venAsign.v1:5">
<citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="Cite2Urn">urn:cite2:hmt:venAsign.v1:5</citeProperty>
<citeProperty name="Label" label="Label" type="string">Sign 5.v1</citeProperty>
<citeProperty name="Sequence" label="Sequence" type="number">5</citeProperty>
<citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="Cite2Urn">urn:cite2:hmt:critsigns:asterisk</citeProperty>
<citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.12</citeProperty>
<prevUrn>urn:cite2:hmt:venAsign.v1:4</prevUrn>
<nextUrn>urn:cite2:hmt:venAsign.v1:6</nextUrn>
</citeObject>
<citeObject urn="urn:cite2:hmt:venAsign.v1:6">
<citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="Cite2Urn">urn:cite2:hmt:venAsign.v1:6</citeProperty>
<citeProperty name="Label" label="Label" type="string">Sign 6.v1</citeProperty>
<citeProperty name="Sequence" label="Sequence" type="number">6</citeProperty>
<citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="Cite2Urn">urn:cite2:hmt:critsigns:asterisk</citeProperty>
<citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.13</citeProperty>
<prevUrn>urn:cite2:hmt:venAsign.v1:5</prevUrn>
<nextUrn>urn:cite2:hmt:venAsign.v1:7</nextUrn>
</citeObject>
<citeObject urn="urn:cite2:hmt:venAsign.v1:7">
<citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="Cite2Urn">urn:cite2:hmt:venAsign.v1:7</citeProperty>
<citeProperty name="Label" label="Label" type="string">Sign 7.v1</citeProperty>
<citeProperty name="Sequence" label="Sequence" type="number">7</citeProperty>
<citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="Cite2Urn">urn:cite2:hmt:critsigns:asterisk</citeProperty>
<citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.14</citeProperty>
<prevUrn>urn:cite2:hmt:venAsign.v1:6</prevUrn>
<nextUrn>urn:cite2:hmt:venAsign.v1:8</nextUrn>
</citeObject>
</citeObjects>
</cite:reply>
</GetRange>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

  @Test
  void testGetRangeOrdered2(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetRange"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:venAsign:5-7")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetRange xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite2:hmt:venAsign:5-7</requestUrn>
    <request>GetRange</request>
    <resolvedUrn>urn:cite2:hmt:venAsign.v1:5-7</resolvedUrn>
</cite:request>
<cite:reply>
<citeObjects>
<citeObject urn="urn:cite2:hmt:venAsign.v1:5">
<citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="Cite2Urn">urn:cite2:hmt:venAsign.v1:5</citeProperty>
<citeProperty name="Label" label="Label" type="string">Sign 5.v1</citeProperty>
<citeProperty name="Sequence" label="Sequence" type="number">5</citeProperty>
<citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="Cite2Urn">urn:cite2:hmt:critsigns:asterisk</citeProperty>
<citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.12</citeProperty>
<prevUrn>urn:cite2:hmt:venAsign.v1:4</prevUrn>
<nextUrn>urn:cite2:hmt:venAsign.v1:6</nextUrn>
</citeObject>
<citeObject urn="urn:cite2:hmt:venAsign.v1:6">
<citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="Cite2Urn">urn:cite2:hmt:venAsign.v1:6</citeProperty>
<citeProperty name="Label" label="Label" type="string">Sign 6.v1</citeProperty>
<citeProperty name="Sequence" label="Sequence" type="number">6</citeProperty>
<citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="Cite2Urn">urn:cite2:hmt:critsigns:asterisk</citeProperty>
<citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.13</citeProperty>
<prevUrn>urn:cite2:hmt:venAsign.v1:5</prevUrn>
<nextUrn>urn:cite2:hmt:venAsign.v1:7</nextUrn>
</citeObject>
<citeObject urn="urn:cite2:hmt:venAsign.v1:7">
<citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="Cite2Urn">urn:cite2:hmt:venAsign.v1:7</citeProperty>
<citeProperty name="Label" label="Label" type="string">Sign 7.v1</citeProperty>
<citeProperty name="Sequence" label="Sequence" type="number">7</citeProperty>
<citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="Cite2Urn">urn:cite2:hmt:critsigns:asterisk</citeProperty>
<citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.14</citeProperty>
<prevUrn>urn:cite2:hmt:venAsign.v1:6</prevUrn>
<nextUrn>urn:cite2:hmt:venAsign.v1:8</nextUrn>
</citeObject>
</citeObjects>
</cite:reply>
</GetRange>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

  @Test
  void testGetRangeUnordered1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetRange"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:vaimg.v1:VA082RN_0083-VA094RN_0095")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString
    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
		assert replyString.contains("CITEError")
  }



}
