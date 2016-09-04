package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
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
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:venAsign")


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
    <requestUrn>urn:cite:hmt:venAsign</requestUrn>
    <request>GetPaged</request>
    <offset>5</offset>
    <limit>3</limit>
    <resolvedUrn>urn:cite:hmt:venAsign</resolvedUrn>
    <count>2903</count>
    <prevOffset>2</prevOffset>
    <prevLimit>3</prevLimit>
    <nextOffset>8</nextOffset>
    <nextLimit>3</nextLimit>
</cite:request>
<cite:reply>
  <citeObjects>
      <citeObject urn="urn:cite:hmt:venAsign.5.v1">
          <citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="citeurn">urn:cite:hmt:critsigns.asterisk</citeProperty>
          <citeProperty name="Label" label="Label" type="string">Sign 5.v1</citeProperty>
          <citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="citeurn">urn:cite:hmt:venAsign.5.v1</citeProperty>
          <citeProperty name="Sequence" label="Sequence" type="number">5</citeProperty>
          <citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.12</citeProperty>
          <prevUrn>urn:cite:hmt:venAsign.4.v1</prevUrn>
          <nextUrn>urn:cite:hmt:venAsign.6.v1</nextUrn>
      </citeObject>
      <citeObject urn="urn:cite:hmt:venAsign.6.v1">
          <citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="citeurn">urn:cite:hmt:critsigns.asterisk</citeProperty>
          <citeProperty name="Label" label="Label" type="string">Sign 6.v1</citeProperty>
          <citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="citeurn">urn:cite:hmt:venAsign.6.v1</citeProperty>
          <citeProperty name="Sequence" label="Sequence" type="number">6</citeProperty>
          <citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.13</citeProperty>
          <prevUrn>urn:cite:hmt:venAsign.5.v1</prevUrn>
          <nextUrn>urn:cite:hmt:venAsign.7.v1</nextUrn>
      </citeObject>
      <citeObject urn="urn:cite:hmt:venAsign.7.v1">
          <citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="citeurn">urn:cite:hmt:critsigns.asterisk</citeProperty>
          <citeProperty name="Label" label="Label" type="string">Sign 7.v1</citeProperty>
          <citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="citeurn">urn:cite:hmt:venAsign.7.v1</citeProperty>
          <citeProperty name="Sequence" label="Sequence" type="number">7</citeProperty>
          <citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.14</citeProperty>
          <prevUrn>urn:cite:hmt:venAsign.6.v1</prevUrn>
          <nextUrn>urn:cite:hmt:venAsign.8.v1</nextUrn>
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
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:venAsign.5-8")


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
    <requestUrn>urn:cite:hmt:venAsign.5-8</requestUrn>
    <request>GetPaged</request>
    <offset>2</offset>
    <limit>2</limit>
    <resolvedUrn>urn:cite:hmt:venAsign.5-8</resolvedUrn>
    <count>4</count>
    <prevOffset>0</prevOffset>
    <prevLimit>2</prevLimit>
    <nextOffset>4</nextOffset>
    <nextLimit>1</nextLimit>
</cite:request>
<cite:reply>
  <citeObjects>
      <citeObject urn="urn:cite:hmt:venAsign.6.v1">
          <citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="citeurn">urn:cite:hmt:critsigns.asterisk</citeProperty>
          <citeProperty name="Label" label="Label" type="string">Sign 6.v1</citeProperty>
          <citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="citeurn">urn:cite:hmt:venAsign.6.v1</citeProperty>
          <citeProperty name="Sequence" label="Sequence" type="number">6</citeProperty>
          <citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.13</citeProperty>
          <prevUrn>urn:cite:hmt:venAsign.5.v1</prevUrn>
          <nextUrn>urn:cite:hmt:venAsign.7.v1</nextUrn>
      </citeObject>
      <citeObject urn="urn:cite:hmt:venAsign.7.v1">
          <citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="citeurn">urn:cite:hmt:critsigns.asterisk</citeProperty>
          <citeProperty name="Label" label="Label" type="string">Sign 7.v1</citeProperty>
          <citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="citeurn">urn:cite:hmt:venAsign.7.v1</citeProperty>
          <citeProperty name="Sequence" label="Sequence" type="number">7</citeProperty>
          <citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.14</citeProperty>
          <prevUrn>urn:cite:hmt:venAsign.6.v1</prevUrn>
          <nextUrn>urn:cite:hmt:venAsign.8.v1</nextUrn>
      </citeObject>
    </citeObjects>
  </cite:reply>
</GetPaged>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

}
