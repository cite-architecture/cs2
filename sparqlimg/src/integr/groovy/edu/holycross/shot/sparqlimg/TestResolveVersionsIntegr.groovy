package edu.holycross.shot.sparqlimg

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlimg.CiteImage
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestResolveVersionsIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/img/query"
  String iipserv = "http://beta.hpcc.uh.edu/fcgi-bin/iipsrv.fcgi"
	String serviceUrl = "http://localhost:8080/sparqlimg/api?"


	@Test
	void testResolveVersion1(){

		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg:VA327RN_0497")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		Cite2Urn resolvedUrn = cimg.resolveVersion(urn)
			assert resolvedUrn.toString() == "urn:cite2:hmt:vaimg.v1:VA327RN_0497"
	}

	@Test
	void testResolveVersion2(){
		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg.v1:VA327RN_0497")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		Cite2Urn resolvedUrn = cimg.resolveVersion(urn)
		assert resolvedUrn.toString() == "urn:cite2:hmt:vaimg.v1:VA327RN_0497"
	}

	@Test
	void testResolveVersion3(){
		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg:VA327RN_0497-VA327VN_0498")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
			shouldFail{
			Cite2Urn resolvedUrn = cimg.resolveVersion(urn)
			// second part of range not in data
				assert resolvedUrn.toString() == "urn:cite2:hmt:vaimg.v1:VA327RN_0497-VA327VN_0498"
			}
	}

	@Test
	void testResolveVersion4(){
		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg:VA327VN_0498")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		shouldFail{
		Cite2Urn resolvedUrn = cimg.resolveVersion(urn)
		// not in data
			assert resolvedUrn.toString() == "urn:cite2:hmt:vaimg.v1:VA327VN_0498"
		}
	}



}
