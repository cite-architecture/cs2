package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestReplyGetVersionsOfObjectIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testGetVersionsOfObject1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetVersionsOfObject"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:2")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetVersionsOfObject xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite2:hmt:pageroi.v1:2</requestUrn>
    <request>GetVersionsOfObject</request>
    <resolvedUrn>urn:cite2:hmt:pageroi.v1:2</resolvedUrn>
</cite:request>
<cite:reply>
    <versions>
      <version>urn:cite2:hmt:pageroi.v1:2</version>
      <version>urn:cite2:hmt:pageroi.v2:2</version>
    </versions>
</cite:reply>
</GetVersionsOfObject>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

  @Test
  void testGetVersionsOfObject2(){
    // set up XMLUnit
    XMLUnit.setNormalizeWhitespace(true)
    //XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetVersionsOfObject"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:2")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetVersionsOfObject xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite2:hmt:pageroi.v1:2</requestUrn>
    <request>GetVersionsOfObject</request>
    <resolvedUrn>urn:cite2:hmt:pageroi.v1:2</resolvedUrn>
</cite:request>
<cite:reply>
    <versions>
      <version>urn:cite2:hmt:pageroi.v1:2</version>
      <version>urn:cite2:hmt:pageroi.v2:2</version>
    </versions>
</cite:reply>
</GetVersionsOfObject>
"""

      Diff xmlDiff = new Diff(expectedXml, replyString)
      assert xmlDiff.identical()
  }



}
