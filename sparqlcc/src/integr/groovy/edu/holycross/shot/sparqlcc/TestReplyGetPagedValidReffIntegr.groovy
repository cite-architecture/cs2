package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestReplyGetPagedValidReffIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testGetPagedValidReff1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetPagedValidReff"
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:pageroi")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString
    reqParams['offset'] = "10"
    reqParams['limit'] = "5"

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetPagedValidReff xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite:hmt:pageroi</requestUrn>
    <request>GetPagedValidReff</request>
    <offset>10</offset>
    <limit>5</limit>
    <resolvedUrn>urn:cite:hmt:pageroi</resolvedUrn>
    <count>40</count>
</cite:request>
<cite:reply>
  <urn>urn:cite:hmt:pageroi.2.v2</urn>
  <urn>urn:cite:hmt:pageroi.23.v1</urn>
  <urn>urn:cite:hmt:pageroi.23.v2</urn>
  <urn>urn:cite:hmt:pageroi.24.v1</urn>
  <urn>urn:cite:hmt:pageroi.24.v2</urn>
</cite:reply>
</GetPagedValidReff>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

  @Test
  void testGetPagedValidReff2(){
    // set up XMLUnit
    XMLUnit.setNormalizeWhitespace(true)
    //XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetPagedValidReff"
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:pageroi")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString
    reqParams['version'] = "v1"
    reqParams['offset'] = "3"
    reqParams['limit'] = "5"

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetPagedValidReff xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite:hmt:pageroi</requestUrn>
    <request>GetPagedValidReff</request>
    <version>v1</version>
    <offset>3</offset>
    <limit>5</limit>
    <resolvedUrn>urn:cite:hmt:pageroi</resolvedUrn>
    <count>20</count>
</cite:request>
<cite:reply>
  <urn>urn:cite:hmt:pageroi.12.v1</urn>
  <urn>urn:cite:hmt:pageroi.19.v1</urn>
  <urn>urn:cite:hmt:pageroi.2.v1</urn>
  <urn>urn:cite:hmt:pageroi.23.v1</urn>
  <urn>urn:cite:hmt:pageroi.24.v1</urn>
</cite:reply>
</GetPagedValidReff>
"""

      Diff xmlDiff = new Diff(expectedXml, replyString)
      assert xmlDiff.identical()
  }


  @Test
  void testGetPagedValidReff3(){
    // set up XMLUnit
    XMLUnit.setNormalizeWhitespace(true)
    //XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetPagedValidReff"
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:venAsign.1-10")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString
    reqParams['offset'] = "5"
    reqParams['limit'] = "2"

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetPagedValidReff xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite:hmt:venAsign.1-10</requestUrn>
    <request>GetPagedValidReff</request>
    <offset>5</offset>
    <limit>2</limit>
    <resolvedUrn>urn:cite:hmt:venAsign.1-10</resolvedUrn>
    <count>10</count>
</cite:request>
<cite:reply>
  <urn>urn:cite:hmt:venAsign.5.v1</urn>
  <urn>urn:cite:hmt:venAsign.6.v1</urn>
</cite:reply>
</GetPagedValidReff>
"""

      Diff xmlDiff = new Diff(expectedXml, replyString)
      assert xmlDiff.identical()
  }

}
