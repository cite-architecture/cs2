package edu.holycross.shot.sparqlcts

import static org.junit.Assert.*
import org.junit.Test


import edu.harvard.chs.cite.CtsUrn

class TestFilterAttrs extends GroovyTestCase {

  @Test
  void testFiltersToAttrs() {
    String filterExpr = "div[@n = '1']"
    String expectedAttrs = "div n = '1'"
    assert   XmlFormatter.filtersToAttrs(filterExpr) == expectedAttrs
  }


  @Test
  void testCompound() {
    String filterExpr = "div[@n = '1' and @type = 'book']"
    String expectedAttrs = "div n = '1' type = 'book'"
    assert   XmlFormatter.filtersToAttrs(filterExpr) == expectedAttrs
  }

  @Test void testStripFilters() {
    String filterExpr = "div[@n = '1' and @type = 'book']"
    assert XmlFormatter.stripFilters(filterExpr) == "div"
  }

}