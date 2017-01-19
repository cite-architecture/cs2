package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
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
  void testCalculatePagedNavigation1(){
      def myMap = [:]

      Integer myLimit = 10
      BigInteger myOffset = 11
      BigInteger mySize = 100


      myMap['prevOffset'] = 1
      myMap['prevLimit'] = 10
      myMap['nextOffset'] = 21
      myMap['nextLimit'] = 10

      Map testMap = cc.calculatePagedNavigation( mySize, myOffset, myLimit)

      assert myMap == testMap
  }

  @Test
  void testCalculatePagedNavigation2(){
      def myMap = [:]

      Integer myLimit = 10
      BigInteger myOffset = 1
      BigInteger mySize = 100


      myMap['prevOffset'] = 0
      myMap['prevLimit'] = 0
      myMap['nextOffset'] = 11
      myMap['nextLimit'] = 10

      Map testMap = cc.calculatePagedNavigation( mySize, myOffset, myLimit)

      assert myMap == testMap
  }

  @Test
  void testCalculatePagedNavigation3(){
      def myMap = [:]

      Integer myLimit = 10
      BigInteger myOffset = 91
      BigInteger mySize = 100


      myMap['prevOffset'] = 81
      myMap['prevLimit'] = 10
      myMap['nextOffset'] = 0
      myMap['nextLimit'] = 0

      Map testMap = cc.calculatePagedNavigation( mySize, myOffset, myLimit)

      assert myMap == testMap
  }

  @Test
  void testCalculatePagedNavigation4(){
      def myMap = [:]

      Integer myLimit = 10
      BigInteger myOffset = 86
      BigInteger mySize = 100


      myMap['prevOffset'] = 76
      myMap['prevLimit'] = 10
      myMap['nextOffset'] = 96
      myMap['nextLimit'] = 5

      Map testMap = cc.calculatePagedNavigation( mySize, myOffset, myLimit)

      assert myMap == testMap
  }

  @Test
  void testCalculatePagedNavigation5(){
      def myMap = [:]

      Integer myLimit = 10
      BigInteger myOffset = 6
      BigInteger mySize = 100


      myMap['prevOffset'] = 1
      myMap['prevLimit'] = 5
      myMap['nextOffset'] = 16
      myMap['nextLimit'] = 10

      Map testMap = cc.calculatePagedNavigation( mySize, myOffset, myLimit)

      assert myMap == testMap
  }

  @Test
  void testGetPagedValidReff1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetPagedValidReff"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")


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
    <requestUrn>urn:cite2:hmt:pageroi.v1:</requestUrn>
    <request>GetPagedValidReff</request>
    <offset>10</offset>
    <limit>5</limit>
    <resolvedUrn>urn:cite2:hmt:pageroi.v1:</resolvedUrn>
    <count>40</count>
    <prevOffset>5</prevOffset>
    <prevLimit>5</prevLimit>
    <nextOffset>15</nextOffset>
    <nextLimit>5</nextLimit>
</cite:request>
<cite:reply>
  <urn>urn:cite2:hmt:pageroi.v1:2.v2</urn>
  <urn>urn:cite2:hmt:pageroi.v1:23.v1</urn>
  <urn>urn:cite2:hmt:pageroi.v1:23.v2</urn>
  <urn>urn:cite2:hmt:pageroi.v1:24.v1</urn>
  <urn>urn:cite2:hmt:pageroi.v1:24.v2</urn>
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
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")


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
    <requestUrn>urn:cite2:hmt:pageroi.v1:</requestUrn>
    <request>GetPagedValidReff</request>
    <version>v1</version>
    <offset>3</offset>
    <limit>5</limit>
    <resolvedUrn>urn:cite2:hmt:pageroi.v1:</resolvedUrn>
    <count>20</count>
    <prevOffset>1</prevOffset>
    <prevLimit>2</prevLimit>
    <nextOffset>8</nextOffset>
    <nextLimit>5</nextLimit>
</cite:request>
<cite:reply>
  <urn>urn:cite2:hmt:pageroi.v1:12.v1</urn>
  <urn>urn:cite2:hmt:pageroi.v1:19.v1</urn>
  <urn>urn:cite2:hmt:pageroi.v1:2.v1</urn>
  <urn>urn:cite2:hmt:pageroi.v1:23.v1</urn>
  <urn>urn:cite2:hmt:pageroi.v1:24.v1</urn>
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
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:1-10")


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
    <requestUrn>urn:cite2:hmt:venAsign.v1:1-10</requestUrn>
    <request>GetPagedValidReff</request>
    <offset>5</offset>
    <limit>2</limit>
    <resolvedUrn>urn:cite2:hmt:venAsign.v1:1-10</resolvedUrn>
    <count>10</count>
    <prevOffset>3</prevOffset>
    <prevLimit>2</prevLimit>
    <nextOffset>7</nextOffset>
    <nextLimit>2</nextLimit>
</cite:request>
<cite:reply>
  <urn>urn:cite2:hmt:venAsign.v1:5.v1</urn>
  <urn>urn:cite2:hmt:venAsign.v1:6.v1</urn>
</cite:reply>
</GetPagedValidReff>
"""

      Diff xmlDiff = new Diff(expectedXml, replyString)
      assert xmlDiff.identical()
  }

}
