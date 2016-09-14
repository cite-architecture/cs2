package edu.holycross.shot.sparqlimg

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlimg.CiteImage
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestResolveVersionsIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/img/query"
  String iipserv = "http://beta.hpcc.uh.edu/fcgi-bin/iipsrv.fcgi"



	@Test
	void testResolveVersion1(){

		CiteUrn urn = new CiteUrn("urn:cite:hmt:vaimg.VA327RN_0497")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv)
		CiteUrn resolvedUrn = cimg.resolveVersion(urn)
			assert resolvedUrn.toString() == "urn:cite:hmt:vaimg.VA327RN_0497.v1"
	}

	@Test
	void testResolveVersion2(){
		CiteUrn urn = new CiteUrn("urn:cite:hmt:vaimg.VA327RN_0497.v1")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv)
		CiteUrn resolvedUrn = cimg.resolveVersion(urn)
		assert resolvedUrn.toString() == "urn:cite:hmt:vaimg.VA327RN_0497.v1"
	}

	@Test
	void testResolveVersion3(){
		CiteUrn urn = new CiteUrn("urn:cite:hmt:vaimg.VA327RN_0497-VA327VN_0498")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv)
		shouldFail{
			CiteUrn resolvedUrn = cimg.resolveVersion(urn)
		}
	}



}
