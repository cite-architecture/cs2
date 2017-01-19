package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestGetPagedIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"


  @Test
  void testTest(){
    assert true
  }


  @Test
  void testPaged1(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Paged for a whole collection
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:")
    Map ccos = cc.getPaged(urn,1,10)
    assert ccos['resolvedUrn'].toString() == urn.toString()
    assert ccos['offset'] == 1
    assert ccos['limit'] == 10
    assert ccos['size'] == 2903
    assert ccos['objects'].size() == 10
    ccos['object'].each { o ->
        System.err.println(o.urn.toString())
    }
  }

  @Test
  void testPaged2(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Paged for a whole collection
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
    Map ccos = cc.getPaged(urn,1,10)
    assert ccos['resolvedUrn'].toString() == urn.toString()
    assert ccos['offset'] == 1
    assert ccos['limit'] == 10
    assert ccos['size'] == 40
    assert ccos['objects'].size() == 10
    ccos['object'].each { o ->
        System.err.println(o.urn.toString())
    }
  }

  @Test
  void testPaged3(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Paged for a whole collection
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
    Map ccos = cc.getPaged(urn,11,10)
    assert ccos['resolvedUrn'].toString() == urn.toString()
    assert ccos['offset'] == 11
    assert ccos['limit'] == 10
    assert ccos['size'] == 40
    assert ccos['objects'].size() == 10
    ccos['object'].each { o ->
        System.err.println(o.urn.toString())
    }
  }

  @Test
  void testPaged4(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Paged for a whole collection
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
    Map ccos = cc.getPaged(urn,31,10)
    assert ccos['resolvedUrn'].toString() == urn.toString()
    assert ccos['offset'] == 31
    assert ccos['limit'] == 10
    assert ccos['size'] == 40
    assert ccos['objects'].size() == 10
    ccos['object'].each { o ->
        System.err.println(o.urn.toString())
    }
  }

    // limit beyond end of collection
  @Test
  void testPaged5(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Paged for a whole collection
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
    Map ccos = cc.getPaged(urn,31,20)
    assert ccos['resolvedUrn'].toString() == urn.toString()
    assert ccos['offset'] == 31
    assert ccos['limit'] == 10
    assert ccos['size'] == 40
    assert ccos['objects'].size() == 10
    ccos['object'].each { o ->
        System.err.println(o.urn.toString())
    }
  }

    // limit beyond end of collection
  @Test
  void testPaged6(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Paged for a whole collection
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
    Map ccos = cc.getPaged(urn,35,20)
    assert ccos['resolvedUrn'].toString() == urn.toString()
    assert ccos['offset'] == 35
    assert ccos['limit'] == 6
    assert ccos['size'] == 40
    assert ccos['objects'].size() == 6
    ccos['object'].each { o ->
        System.err.println(o.urn.toString())
    }
  }

    // limit beyond end of an ordered collection
  @Test
  void testPaged7(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Paged for a whole collection, ordered
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:")
    Map ccos = cc.getPaged(urn,2900,20)
    assert ccos['resolvedUrn'].toString() == urn.toString()
    assert ccos['offset'] == 2900
    assert ccos['limit'] == 4
    assert ccos['size'] == 2903
    assert ccos['objects'].size() == 4
    ccos['object'].each { o ->
        System.err.println(o.urn.toString())
    }
  }

    // Stupid params
  @Test
  void testPaged8(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Paged for a whole collection
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
    shouldFail{
      Map ccos = cc.getPaged(urn,-1,20)
    }
  }
    // Stupid params
  @Test
  void testPaged9(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Paged for a whole collection
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
    shouldFail{
      Map ccos = cc.getPaged(urn,1,-1)
    }
  }
    // Stupid params
  @Test
  void testPaged10(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Paged for a whole collection
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
    shouldFail{
      Map ccos = cc.getPaged(urn,0,10)
    }
  }

    // Stupid params
  @Test
  void testPaged11(){
    Sparql sparql = new Sparql(baseUrl)
	  CcGraph cc = new CcGraph(sparql)

    // Paged for a whole collection
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
    shouldFail{
      Map ccos = cc.getPaged(urn,10,0)
    }
  }

  // One, at the end
@Test
void testPaged12(){
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)

  // Paged for a whole collection
  Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
  Map ccos = cc.getPaged(urn,40,1)
  assert ccos['resolvedUrn'].toString() == urn.toString()
  assert ccos['offset'] == 40
  assert ccos['limit'] == 1
  assert ccos['size'] == 40
  assert ccos['objects'].size() == 1
  ccos['object'].each { o ->
      System.err.println(o.urn.toString())
  }
}

// One, at the beginning
@Test
void testPaged13(){
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)

  // Paged for a whole collection
  Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
  Map ccos = cc.getPaged(urn,1,1)
  assert ccos['resolvedUrn'].toString() == urn.toString()
  assert ccos['offset'] == 1
  assert ccos['limit'] == 1
  assert ccos['size'] == 40
  assert ccos['objects'].size() == 1
  ccos['object'].each { o ->
    System.err.println(o.urn.toString())
  }
}

// Asked for more than one, one from the end
@Test
void testPaged14(){
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)

  // Paged for a whole collection
  Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:pageroi.v1:")
  Map ccos = cc.getPaged(urn,40,10)
  assert ccos['resolvedUrn'].toString() == urn.toString()
  assert ccos['offset'] == 40
  assert ccos['limit'] == 1
  assert ccos['size'] == 40
  assert ccos['objects'].size() == 1
  ccos['object'].each { o ->
    System.err.println(o.urn.toString())
  }
}

}
