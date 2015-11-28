package edu.holycross.shot.graph

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.citeservlet.Sparql


class TestCtsIntegr extends GroovyTestCase {

  String baseUrl = "http://localhost:8080/fuseki/graph/query"
  Sparql sparql = new Sparql(baseUrl)
  GraphService gs = new GraphService(sparql)

  /* URNs for Testing (based on test dataset */
  def ctsGroupUrn = "uren:ctrs:greekLirt:tlg0012:"
  def ctsWorkUrn = "urn:cts:greekLit:tlg0012.tlg001:1.1"
  def ctsVersionUrn = "urn:cts:greekLit:tlg0012.tlg001.msA:1.1"
  def ctsExemplarUrn = "urn:cts:greekLit:tlg0012.tlg001.msA.wt:1.1"

   @Test
  void testWorkContainer() {
    CtsUrn urn = new CtsUrn(ctsGroupUrn)
	ArrayList al = gs.graph.findAdjacent(urn)
	println "${urn} testing for container "
	println "${al}"
	println al
	assert al.size() == 694
  }  

  void testWorkContainer() {
    CtsUrn urn = new CtsUrn(ctsWorkUrn)
	ArrayList al = gs.graph.findAdjacent(urn)
	println "${urn} testing for container "
	println "${al}"
	println al
	assert al.size() == 694
  }  

   @Test
  void testVersionContainer() {
    CtsUrn urn = new CtsUrn(ctsVersionUrn)
	ArrayList al = gs.graph.findAdjacent(urn)
	println "${urn} testing for container "
	println "${al}"
	println al
	assert al.size() == 694
  }  

  void testExemplarContainer() {
    CtsUrn urn = new CtsUrn(ctsExemplarUrn)
	ArrayList al = gs.graph.findAdjacent(urn)
	println "${urn} testing for container "
	println "${al}"
	println al
	assert al.size() == 694
  }  
/* @Test
  void testGetVersions() {
    CtsUrn urn = new CtsUrn(ctsSubjNotionalLeaf)
	ArrayList al = gs.graph.getVersionsForNotionalUrn(urn)
	//println "al.size() == ${al.size()}"
	assert al.size() == 3
  } */

/* @Test
  void testNotionalLeaf() {
    CtsUrn urn = new CtsUrn(ctsSubjNotionalLeaf)
	ArrayList al = gs.graph.findAdjacent(urn)
	//println "${al}"
	assert al.size() == 78
  } */

/* 
  @Test
  void testVersionLeaf() {
    CtsUrn urn = new CtsUrn(ctsSubjLeaf)
	ArrayList al = gs.graph.findAdjacent(urn)
 	println "${urn}: ${gs.graph.findAdjacent(urn)}"
	assert al.size() == 56 
  } */

  /* @Test
  void testVersionLeafWithSubstring() {
    CtsUrn urn = new CtsUrn(ctsSubjLeafWithSubstr)
	ArrayList al = gs.graph.findAdjacent(urn)
	println "${urn} testing for version leaf with substring"
 	//println "${urn}: ${gs.graph.findAdjacent(urn)}"
	assert al.size() == 56 
  } */


  /* @Test
  void testVersionContainer() {
    CtsUrn urn = new CtsUrn(ctsSubjContaining)
	ArrayList al = gs.graph.findAdjacent(urn)
	println "${urn} testing for container "
	assert al.size() == 95
  } */


  /* @Test
  void testVersionLeafRange() {
    CtsUrn urn = new CtsUrn(ctsSubjLeafRange)
 	println gs.graph.findAdjacent(urn)   
  } */
  
  /* @Test
  void testGroup() {
    CtsUrn urn = new CtsUrn(ctsSubjGroup)
 	println gs.graph.findAdjacent(urn)   
  } */ 


}
