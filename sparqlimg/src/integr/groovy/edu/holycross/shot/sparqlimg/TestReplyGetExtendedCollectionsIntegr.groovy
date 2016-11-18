package edu.holycross.shot.sparqlimg

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlimg.CiteImage
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestReplyGetExtendedCollectionsIntegr extends GroovyTestCase {

  String baseUrl1 = "http://localhost:8080/fuseki/img/query"
  String baseUrl2 = "http://localhost:8080/fuseki/cc/query"
  String iipserv = "http://beta.hpcc.uh.edu/fcgi-bin/iipsrv.fcgi"
	String serviceUrl = "http://localhost:8080/sparqlimg/api"


	@Test
	void testGetExtendeCollection1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

		Sparql sparql = new Sparql(baseUrl1)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		String replyString = cimg.getExtendedCollectionsReply("cite:CiteImage")

    String expectedXml = """
<GetExtendedCollections xmlns='http://chs.harvard.edu/xmlns/citeimg'>
<request>
</request>
<reply>
<collection urn="urn:cite:hmt:vaimg"/>
</reply>
</GetExtendedCollections>
"""

		System.err.println(replyString)
		System.err.println("<----replyString | expectedXML ----->")
		System.err.println(expectedXml)

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()

	}

	@Test
	void testGetExtendeCollection2(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

		Sparql sparql = new Sparql(baseUrl2)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		String replyString = cimg.getExtendedCollectionsReply("cite:CiteImage")

    String expectedXml = """
<GetExtendedCollections xmlns='http://chs.harvard.edu/xmlns/citeimg'>
<request>
</request>
<reply>
<collection urn="urn:cite:hmt:vaimg"/>
</reply>
</GetExtendedCollections>
"""

		System.err.println(replyString)
		System.err.println("<----replyString | expectedXML ----->")
		System.err.println(expectedXml)

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()

	}

}
