package edu.holycross.shot.sparqlcts

import edu.holycross.shot.citeservlet.Sparql

import edu.harvard.chs.cite.CtsUrn
import groovy.json.JsonSlurper

/** A class interacting with a SPARQL endpoint to
 * to resolve SPARQL replies into objects in the abstract data
 * model of OHCO2.
 */
class CtsGraph {

  Sparql sparql

  CtsGraph(Sparql endPoint) {
    sparql = endPoint
  }

  
  /** Determines a valid version value for a CTS URN.
   * If the URN is at the version level, this value is simply the
   * version element of the URN.  If the URN is at the work level,
   * the SPARQL endpoint is consulted and the identifier of 
   * the first version mapped to this work is returned.
   * @param urn CTS URN to find version for.
   * @returns A string with the version identifier component of a
   * version-level URN, without namespace qualifier.
   */
  String findVersion(CtsUrn urn) 
  throws Exception {
    String workLevel = urn.labelForWorkLevel()
    String vers = null
    if ((workLevel == 'version') || (workLevel == 'exemplar')) {
      vers = urn.getVersion(false)
    } else {
      String vQuery = QueryBuilder.getVersionQuery(urn)
      String reply = sparql.getSparqlReply("application/json", vQuery)

      JsonSlurper slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(reply)
      parsedReply.results.bindings.each { bndng ->
	if (bndng.vers) {
	  CtsUrn versionUrn = new CtsUrn(bndng.vers?.value)
	  vers = versionUrn.getVersion(false)
	}
      }
    }
    if (vers == null) {
      throw new Exception("CtsGraph:findVersion: could not determine version for ${urn}")
    } else {
	return vers
    }
  }

}