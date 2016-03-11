package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql
import edu.holycross.shot.sparqlcts.CtsGraph


class TestURNCheckIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/ds/query"
  Sparql sparql = new Sparql(baseUrl)
  CtsGraph graph = new CtsGraph(sparql)


  @Test
  void test1() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001.testAllen:1.1")
    String expectedResult = "okay"
    String actualResult = graph.checkUrn(urn)
    assert actualResult.replaceAll("\\s","") == expectedResult.replaceAll("\\s","")
  }

  @Test
  void test2() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:1.1")
    String expectedResult = "okay"
    String actualResult = graph.checkUrn(urn)
    assert actualResult.replaceAll("\\s","") == expectedResult.replaceAll("\\s","")
  }

  @Test
  void test3() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    String expectedResult = "okay"
    String actualResult = graph.checkUrn(urn)
    assert actualResult.replaceAll("\\s","") == expectedResult.replaceAll("\\s","")
  }

  @Test
  void test4() {
    CtsUrn urn = new CtsUrn("urn:cts:greekLit:tlg0012.NOWORK:1.1")
    String expectedResult = "Invalid URN reference: urn:cts:greekLit:tlg0012.NOWORK:1.1"
    String actualResult = graph.checkUrn(urn)
    assert actualResult.replaceAll("\\s","") == expectedResult.replaceAll("\\s","")
  }


}
