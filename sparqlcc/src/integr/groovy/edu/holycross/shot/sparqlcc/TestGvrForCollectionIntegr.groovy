package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGvrForCollectionIntegr  extends GroovyTestCase {

// urn:cite:hmt:vaimg = 966
// urn:cite:hmt:pageroi == 20 for each version
// urn:cite:hmt:venAsign == 2906 for all.
// urn:cite:hmt:venAsign.11.v1-20.v1 == 10, no surprises
// urn:cite:hmt:msA == ordered, 10, no surprises

  String baseUrl = "http://localhost:8080/fuseki/cc/query"

  @Test
  void testTest(){
    assert false
  }
}
