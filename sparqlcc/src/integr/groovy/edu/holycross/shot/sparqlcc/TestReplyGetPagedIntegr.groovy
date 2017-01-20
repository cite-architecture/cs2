package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestReplyGetPagedIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testGetPaged1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetPaged"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString
    reqParams['offset'] = "5"
    reqParams['limit'] = "3"

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetPaged xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite2:hmt:venAsign.v1:</requestUrn>
    <request>GetPaged</request>
    <offset>5</offset>
    <limit>3</limit>
    <resolvedUrn>urn:cite2:hmt:venAsign.v1:</resolvedUrn>
    <count>2903</count>
    <prevOffset>2</prevOffset>
    <prevLimit>3</prevLimit>
    <nextOffset>8</nextOffset>
    <nextLimit>3</nextLimit>
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
</GetPaged>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

  @Test
  void testGetPaged2(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetPaged"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:5-8")
		System.err.println("reqUrn for testGetPaged2: ${reqUrn}")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString
    reqParams['offset'] = "2"
    reqParams['limit'] = "2"

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetPaged xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite2:hmt:venAsign.v1:5-8</requestUrn>
    <request>GetPaged</request>
    <offset>2</offset>
    <limit>2</limit>
    <resolvedUrn>urn:cite2:hmt:venAsign.v1:5-8</resolvedUrn>
    <count>4</count>
    <prevOffset>0</prevOffset>
    <prevLimit>2</prevLimit>
    <nextOffset>4</nextOffset>
    <nextLimit>1</nextLimit>
</cite:request>
<cite:reply>
  <citeObjects>
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
</GetPaged>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

}
