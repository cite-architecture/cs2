package edu.holycross.shot.sparqlimg

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlimg.CiteImage
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestReplyGetCaptionIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/img/query"
  String iipserv = "http://beta.hpcc.uh.edu/fcgi-bin/iipsrv.fcgi"
	String serviceUrl = "http://localhost:8080/sparqlimg/api?"



	@Test
	void testGetCaptionUnversioned(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

		CiteUrn urn = new CiteUrn("urn:cite:hmt:vaimg.VA327RN_0497")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		String replyString = cimg.getCaptionReply(urn)

    String expectedXml = """
<GetCaption xmlns='http://chs.harvard.edu/xmlns/citeimg'>
<request>
<urn>urn:cite:hmt:vaimg.VA327RN_0497</urn>
<resolvedUrn>urn:cite:hmt:vaimg.VA327RN_0497.v1</resolvedUrn>
</request>
<reply>
<caption>Venetus A: Marcianus Graecus Z. 454 (= 822), folio 327, recto.</caption>
</reply>
</GetCaption>
"""

		System.err.println(replyString)
		System.err.println("<----replyString | expectedXML ----->")
		System.err.println(expectedXml)

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()

	}

	@Test
	void testGetCaptionVersioned(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

		CiteUrn urn = new CiteUrn("urn:cite:hmt:vaimg.VA327RN_0497.v1")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		String replyString = cimg.getCaptionReply(urn)

    String expectedXml = """
<GetCaption xmlns='http://chs.harvard.edu/xmlns/citeimg'>
<request>
<urn>urn:cite:hmt:vaimg.VA327RN_0497.v1</urn>
<resolvedUrn>urn:cite:hmt:vaimg.VA327RN_0497.v1</resolvedUrn>
</request>
<reply>
<caption>Venetus A: Marcianus Graecus Z. 454 (= 822), folio 327, recto.</caption>
</reply>
</GetCaption>
"""

		System.err.println(replyString)
		System.err.println("<----replyString | expectedXML ----->")
		System.err.println(expectedXml)

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()

	}


}
