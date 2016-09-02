package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
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
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:venAsign.5-7")


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
    <requestUrn>urn:cite:hmt:venAsign.5-7</requestUrn>
    <request>GetRange</request>
    <resolvedUrn>urn:cite:hmt:venAsign.5.v1-7.v1</resolvedUrn>
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
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:venAsign.5.v1-7.v1")


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
    <requestUrn>urn:cite:hmt:venAsign.5.v1-7.v1</requestUrn>
    <request>GetRange</request>
    <resolvedUrn>urn:cite:hmt:venAsign.5.v1-7.v1</resolvedUrn>
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
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:vaimg.VA082RN_0083-VA094RN_0095")


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
    <requestUrn>urn:cite:hmt:vaimg.VA082RN_0083-VA094RN_0095</requestUrn>
    <request>GetRange</request>
    <resolvedUrn>urn:cite:hmt:vaimg.VA082RN_0083.v1-VA094RN_0095.v1</resolvedUrn>
</cite:request>
<cite:reply>
  <citeObjects>
      <citeObject urn="urn:cite:hmt:vaimg.VA082RN_0083.v1">
          <citeProperty name="Image" label="Image URN" type="citeurn">urn:cite:hmt:vaimg.VA082RN_0083.v1</citeProperty>
          <citeProperty name="Label" label="Caption" type="string">Venetus A: Marcianus Graecus Z. 454 (= 822).  Photograph in natural light, folio 82, recto.</citeProperty>
          <citeProperty name="Rights" label="Rights" type="string">This image was derived from an original ©2007, Biblioteca Nazionale Marciana, Venezie, Italia. The derivative image is ÔøΩ2010, Center for Hellenic Studies. Original and derivative are licensed under the Creative Commons Attribution-Noncommercial-Share Alike 3.0 License. The CHS/Marciana Imaging Project was directed by David Jacobs of the British Library.</citeProperty>
          <extension>http://www.homermultitext.org/cite/rdf/CiteImage</extension>
      </citeObject>
      <citeObject urn="urn:cite:hmt:vaimg.VA094RN_0095.v1">
          <citeProperty name="Image" label="Image URN" type="citeurn">urn:cite:hmt:vaimg.VA094RN_0095.v1</citeProperty>
          <citeProperty name="Label" label="Caption" type="string">Venetus A: Marcianus Graecus Z. 454 (= 822).  Photograph in natural light, folio 94, recto.</citeProperty>
          <citeProperty name="Rights" label="Rights" type="string">This image was derived from an original ©2007, Biblioteca Nazionale Marciana, Venezie, Italia. The derivative image is ÔøΩ2010, Center for Hellenic Studies. Original and derivative are licensed under the Creative Commons Attribution-Noncommercial-Share Alike 3.0 License. The CHS/Marciana Imaging Project was directed by David Jacobs of the British Library.</citeProperty>
          <extension>http://www.homermultitext.org/cite/rdf/CiteImage</extension>
      </citeObject>
    </citeObjects>
</cite:reply>
</GetRange>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }



}
