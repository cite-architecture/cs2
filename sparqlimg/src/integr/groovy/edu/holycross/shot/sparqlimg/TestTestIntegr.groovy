package edu.holycross.shot.sparqlimg

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlimg.CiteImage
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestTestIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/img/query"
  String iipserv = "http://beta.hpcc.uh.edu/fcgi-bin/iipsrv.fcgi"


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testGetCollectionIdProp(){
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign")
    Sparql sparql = new Sparql(baseUrl)
    CiteImage cimg = new CiteImage(sparql,iipserv)
		assert cimg.getCaptionProp(urn.toString()) == "xvaimg_Label"
  }


}
