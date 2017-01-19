package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestReplyGetCollectionSizeIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testGetCollectionSize1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetCollectionSize"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString
    //reqParams['version'] = ""

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetCollectionSize xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite2:hmt:pageroi.v1:</requestUrn>
    <request>GetCollectionSize</request>
    <resolvedUrn>urn:cite2:hmt:pageroi.v1:</resolvedUrn>
</cite:request>
<cite:reply>
    <count>20</count>
</cite:reply>
</GetCollectionSize>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

  @Test
  void testGetCollectionSize2(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetCollectionSize"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString
    reqParams['version'] = "v2"

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetCollectionSize xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite2:hmt:pageroi.v1:</requestUrn>
    <request>GetCollectionSize</request>
    <version>v2</version>
    <resolvedUrn>urn:cite2:hmt:pageroi.v1:</resolvedUrn>
</cite:request>
<cite:reply>
    <count>20</count>
</cite:reply>
</GetCollectionSize>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }


}
