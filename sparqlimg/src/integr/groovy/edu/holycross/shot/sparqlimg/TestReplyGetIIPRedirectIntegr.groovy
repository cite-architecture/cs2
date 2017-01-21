package edu.holycross.shot.sparqlimg

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlimg.CiteImage
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestReplyGetIIPRedirectIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/img/query"
  String iipserv = "http://beta.hpcc.uh.edu/fcgi-bin/iipsrv.fcgi"
	String serviceUrl = "http://localhost:8080/sparqlimg/api?"


	@Test
	void testGetIIPUnversioned(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg:VA327RN_0497")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		String replyString = cimg.getBinaryRedirect(urn)

		assert replyString == "http://beta.hpcc.uh.edu/fcgi-bin/iipsrv.fcgi?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/VenA/VA327RN_0497.tif"

	}

	@Test
	void testGetIIPVersioned(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg.v1:VA327RN_0497")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		String replyString = cimg.getBinaryRedirect(urn)

			assert replyString == "http://beta.hpcc.uh.edu/fcgi-bin/iipsrv.fcgi?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/VenA/VA327RN_0497.tif"

	}


}
