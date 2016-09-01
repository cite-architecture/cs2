package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestReplyGetFirstLastUrnIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testGetFirstUrn1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetFirstUrn"
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:venAsign.5.v1")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetFirstUrn xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite:hmt:venAsign.5.v1</requestUrn>
    <request>GetFirstUrn</request>
    <resolvedUrn>urn:cite:hmt:venAsign.5.v1</resolvedUrn>
</cite:request>
<cite:reply>
    <firstUrn>urn:cite:hmt:venAsign.1.v1</firstUrn>
</cite:reply>
</GetFirstUrn>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }


   @Test
  void testGetLastUrn1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetLastUrn"
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:venAsign.5.v1")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetLastUrn xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite:hmt:venAsign.5.v1</requestUrn>
    <request>GetLastUrn</request>
    <resolvedUrn>urn:cite:hmt:venAsign.5.v1</resolvedUrn>
</cite:request>
<cite:reply>
    <lastUrn>urn:cite:hmt:venAsign.2906.v1</lastUrn>
</cite:reply>
</GetLastUrn>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

}
