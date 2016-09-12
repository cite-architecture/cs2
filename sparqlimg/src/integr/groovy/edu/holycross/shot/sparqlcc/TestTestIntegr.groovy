package edu.holycross.shot.sparqlimg

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlimg.CiteImg
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestTestIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/img/query"
  String iipserv = "http://beta.hpcc.uh.edu/fcgi-bin/iipsrv.fcgi"


  @Test
  void testTest(){
    assert false
  }

  @Test
  void testGetCollectionIdProp(){
    CiteUrn urn = new CiteUrn("urn:cite:hmt:venAsign")
    Sparql sparql = new Sparql(baseUrl)
    CcGraph cimg = new CiteImg(sparql,iipserv)
    CiteProperty prop = cimg.getCollectionIdProp(urn)
    assert prop.propertyName == "OccurrenceUrn"
    assert prop.propertyType == CitePropertyType.CITE_URN
    assert prop.label.size() > 0
  }


}
