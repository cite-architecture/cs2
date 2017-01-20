package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestReplyGetPrevNextUrnIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testGetPrevUrn1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetPrevUrn"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:5")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetPrevUrn xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite2:hmt:venAsign.v1:5</requestUrn>
    <request>GetPrevUrn</request>
    <resolvedUrn>urn:cite2:hmt:venAsign.v1:5</resolvedUrn>
</cite:request>
<cite:reply>
    <prevUrn>urn:cite2:hmt:venAsign.v1:4</prevUrn>
</cite:reply>
</GetPrevUrn>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }


   @Test
  void testGetNextUrn1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetNextUrn"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:5")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetNextUrn xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite2:hmt:venAsign.v1:5</requestUrn>
    <request>GetNextUrn</request>
    <resolvedUrn>urn:cite2:hmt:venAsign.v1:5</resolvedUrn>
</cite:request>
<cite:reply>
    <nextUrn>urn:cite2:hmt:venAsign.v1:6</nextUrn>
</cite:reply>
</GetNextUrn>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

  @Test
 void testGetPrevNextUrn1(){
   // set up XMLUnit
   XMLUnit.setNormalizeWhitespace(true)
   //XMLUnit.setIgnoreWhitespace(true)

   //Set up params
   String reqString = "GetPrevNextUrn"
   Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:5")


   def reqParams = [:]
   reqParams['urn'] = reqUrn.toString()
   reqParams['request'] = reqString

   String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
   System.err.println("----")
   System.err.println(replyString)
   System.err.println("----")

   String expectedXml = """
<GetPrevNextUrn xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
   <requestUrn>urn:cite2:hmt:venAsign.v1:5</requestUrn>
   <request>GetPrevNextUrn</request>
   <resolvedUrn>urn:cite2:hmt:venAsign.v1:5</resolvedUrn>
</cite:request>
<cite:reply>
  <prevUrn>urn:cite2:hmt:venAsign.v1:4</prevUrn>
  <nextUrn>urn:cite2:hmt:venAsign.v1:6</nextUrn>
</cite:reply>
</GetPrevNextUrn>
"""

     Diff xmlDiff = new Diff(expectedXml, replyString)
     assert xmlDiff.identical()
 }



}
