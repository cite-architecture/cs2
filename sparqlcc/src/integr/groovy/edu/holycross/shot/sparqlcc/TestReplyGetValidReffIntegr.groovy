package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestReplyGetValidReffIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testGetValidReff1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetValidReff"
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:pageroi")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetValidReff xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite:hmt:pageroi</requestUrn>
    <request>GetValidReff</request>
    <resolvedUrn>urn:cite:hmt:pageroi</resolvedUrn>
</cite:request>
<cite:reply>
  <urn>urn:cite:hmt:pageroi.1.v1</urn>
  <urn>urn:cite:hmt:pageroi.1.v2</urn>
  <urn>urn:cite:hmt:pageroi.10.v1</urn>
  <urn>urn:cite:hmt:pageroi.10.v2</urn>
  <urn>urn:cite:hmt:pageroi.12.v1</urn>
  <urn>urn:cite:hmt:pageroi.12.v2</urn>
  <urn>urn:cite:hmt:pageroi.19.v1</urn>
  <urn>urn:cite:hmt:pageroi.19.v2</urn>
  <urn>urn:cite:hmt:pageroi.2.v1</urn>
  <urn>urn:cite:hmt:pageroi.2.v2</urn>
  <urn>urn:cite:hmt:pageroi.23.v1</urn>
  <urn>urn:cite:hmt:pageroi.23.v2</urn>
  <urn>urn:cite:hmt:pageroi.24.v1</urn>
  <urn>urn:cite:hmt:pageroi.24.v2</urn>
  <urn>urn:cite:hmt:pageroi.3.v1</urn>
  <urn>urn:cite:hmt:pageroi.3.v2</urn>
  <urn>urn:cite:hmt:pageroi.30.v1</urn>
  <urn>urn:cite:hmt:pageroi.30.v2</urn>
  <urn>urn:cite:hmt:pageroi.31.v1</urn>
  <urn>urn:cite:hmt:pageroi.31.v2</urn>
  <urn>urn:cite:hmt:pageroi.36.v1</urn>
  <urn>urn:cite:hmt:pageroi.36.v2</urn>
  <urn>urn:cite:hmt:pageroi.39.v1</urn>
  <urn>urn:cite:hmt:pageroi.39.v2</urn>
  <urn>urn:cite:hmt:pageroi.4.v1</urn>
  <urn>urn:cite:hmt:pageroi.4.v2</urn>
  <urn>urn:cite:hmt:pageroi.41.v1</urn>
  <urn>urn:cite:hmt:pageroi.41.v2</urn>
  <urn>urn:cite:hmt:pageroi.49.v1</urn>
  <urn>urn:cite:hmt:pageroi.49.v2</urn>
  <urn>urn:cite:hmt:pageroi.5.v1</urn>
  <urn>urn:cite:hmt:pageroi.5.v2</urn>
  <urn>urn:cite:hmt:pageroi.50.v1</urn>
  <urn>urn:cite:hmt:pageroi.50.v2</urn>
  <urn>urn:cite:hmt:pageroi.6.v1</urn>
  <urn>urn:cite:hmt:pageroi.6.v2</urn>
  <urn>urn:cite:hmt:pageroi.7.v1</urn>
  <urn>urn:cite:hmt:pageroi.7.v2</urn>
  <urn>urn:cite:hmt:pageroi.8.v1</urn>
  <urn>urn:cite:hmt:pageroi.8.v2</urn>
</cite:reply>
</GetValidReff>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

  @Test
  void testGetValidReff2(){
    // set up XMLUnit
    XMLUnit.setNormalizeWhitespace(true)
    //XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetValidReff"
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:pageroi")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString
    reqParams['version'] = "v1"

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetValidReff xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite:hmt:pageroi</requestUrn>
    <request>GetValidReff</request>
    <version>v1</version>
    <resolvedUrn>urn:cite:hmt:pageroi</resolvedUrn>
</cite:request>
<cite:reply>
  <urn>urn:cite:hmt:pageroi.1.v1</urn>
  <urn>urn:cite:hmt:pageroi.10.v1</urn>
  <urn>urn:cite:hmt:pageroi.12.v1</urn>
  <urn>urn:cite:hmt:pageroi.19.v1</urn>
  <urn>urn:cite:hmt:pageroi.2.v1</urn>
  <urn>urn:cite:hmt:pageroi.23.v1</urn>
  <urn>urn:cite:hmt:pageroi.24.v1</urn>
  <urn>urn:cite:hmt:pageroi.3.v1</urn>
  <urn>urn:cite:hmt:pageroi.30.v1</urn>
  <urn>urn:cite:hmt:pageroi.31.v1</urn>
  <urn>urn:cite:hmt:pageroi.36.v1</urn>
  <urn>urn:cite:hmt:pageroi.39.v1</urn>
  <urn>urn:cite:hmt:pageroi.4.v1</urn>
  <urn>urn:cite:hmt:pageroi.41.v1</urn>
  <urn>urn:cite:hmt:pageroi.49.v1</urn>
  <urn>urn:cite:hmt:pageroi.5.v1</urn>
  <urn>urn:cite:hmt:pageroi.50.v1</urn>
  <urn>urn:cite:hmt:pageroi.6.v1</urn>
  <urn>urn:cite:hmt:pageroi.7.v1</urn>
  <urn>urn:cite:hmt:pageroi.8.v1</urn>
</cite:reply>
</GetValidReff>
"""

      Diff xmlDiff = new Diff(expectedXml, replyString)
      assert xmlDiff.identical()
  }


  @Test
  void testGetValidReff3(){
    // set up XMLUnit
    XMLUnit.setNormalizeWhitespace(true)
    //XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetValidReff"
    CiteUrn reqUrn = new CiteUrn("urn:cite:hmt:venAsign.5-7")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetValidReff xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite:hmt:venAsign.5-7</requestUrn>
    <request>GetValidReff</request>
    <resolvedUrn>urn:cite:hmt:venAsign.5.v1-7.v1</resolvedUrn>
</cite:request>
<cite:reply>
  <urn>urn:cite:hmt:venAsign.5.v1</urn>
  <urn>urn:cite:hmt:venAsign.6.v1</urn>
  <urn>urn:cite:hmt:venAsign.7.v1</urn>
</cite:reply>
</GetValidReff>
"""

      Diff xmlDiff = new Diff(expectedXml, replyString)
      assert xmlDiff.identical()
  }


}
