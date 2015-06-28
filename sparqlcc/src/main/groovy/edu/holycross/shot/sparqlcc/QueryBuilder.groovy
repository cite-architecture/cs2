package edu.holycross.shot.sparqlcc

import edu.harvard.chs.cite.CiteUrn

abstract class QueryBuilder {

  static String getExampleQuery(CiteUrn urn) {
    return "Query about ${urn}"
  }

}
