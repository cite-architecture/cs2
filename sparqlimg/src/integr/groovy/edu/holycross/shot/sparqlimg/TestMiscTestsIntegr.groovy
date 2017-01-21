package edu.holycross.shot.sparqlimg

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlimg.CiteImage
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestMiscTestsIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/img/query"
  String iipserv = "http://beta.hpcc.uh.edu/fcgi-bin/iipsrv.fcgi"
	String serviceUrl = "http://localhost:8080/sparqlimg/api?"

  @Test
	void testTest(){
		assert true
	}

	@Test
	void testResolveVersion(){
		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg:VA327RN_0497")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		assert cimg.resolveVersion(urn).toString() == "urn:cite2:hmt:vaimg.v1:VA327RN_0497"
	}




}
