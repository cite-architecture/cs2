package edu.holycross.shot.sparqlimg

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlimg.CiteImage
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetPropertiesIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/img/query"
  String iipserv = "http://beta.hpcc.uh.edu/fcgi-bin/iipsrv.fcgi"
	String serviceUrl = "http://localhost:8080/sparqlimg/api?"


	@Test
	void testGetCaptionProp(){
		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg.v1:VA327RN_0497")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		String expectedPropName = "citedata:vaimg_Label"
		assert cimg.getCaptionProp(urn.toString()) == expectedPropName
	}

	@Test
	void testGetCaptionPropUnversioned(){
		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg:VA327RN_0497")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		String expectedPropName = "citedata:vaimg_Label"
		assert cimg.getCaptionProp(urn.toString()) == expectedPropName
	}


	@Test
	void testGetRightsProp(){
		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg.v1:VA327RN_0497")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		String expectedPropName = "citedata:vaimg_Rights"
		assert cimg.getRightsProp(urn.toString()) == expectedPropName
	}

	@Test
	void testGetRightsPropUnversioned(){
		Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg:VA327RN_0497")
		Sparql sparql = new Sparql(baseUrl)
		CiteImage cimg = new CiteImage(sparql,iipserv,serviceUrl)
		String expectedPropName = "citedata:vaimg_Rights"
		assert cimg.getRightsProp(urn.toString()) == expectedPropName
	}



}
