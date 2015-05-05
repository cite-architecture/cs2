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



  
  /**  Determines whether or not a CTS URN refers to a leaf
   * citation node.  Consults the SPARQL endpoint
   * @returns Boolean true if urn refers to a leaf citation node.
   */
  boolean isLeafNode(CtsUrn requestUrn) {
    String replyText = ""
    CtsUrn urn = resolveVersion(requestUrn)
    String vQuery = QueryBuilder.getVersionQuery(urn)
    

    //    println 
    /*
    String reply = getSparqlReply("text/xml", qg.getIsLeafQuery(urn))
    def root = new XmlParser().parseText(reply)
    def replyNode = root[sparql.boolean][0]
    replyText = replyNode.text()
    */
    return (replyText == "true")
  }



  /**  Constructs a version-level CTS URN for a
   * given URN.  If the request URN already has a version,
   * it is returned unchanged.  If the request URN is at the
   * work level, a version attested in the triple store is chosen,
   * and URN referring to that version for the same passage is returned.
   * @param urn A CTS URN reference to resolve to a version-level CTS URN.
   * @returns A CTS URN at the version level citing the passage in 
   * the request urn.
   */
  CtsUrn resolveVersion(CtsUrn urn) 
  throws Exception {
    String workLevel = urn.labelForWorkLevel()
    if ((workLevel == 'version')|(workLevel == 'exemplar')) {
      return urn

    } else {
      String version = findVersion(urn)
      if (version) {
	String resolvedStr  = "urn:cts:${urn.getCtsNamespace()}:${urn.getTextGroup()}.${urn.getWork()}.${version}:${urn.getPassageComponent()}"
	
	try {
	  return (new CtsUrn(resolvedStr))
	} catch (Exception e) {
	  System.err.println ("CtsGraph:resolveVersion: error: ${e}")
	  throw e
	}

	
      } else {
	throw new Exception("CtsGraph: resolveVersion: no version found for urn ${urn}")
      }
    }
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