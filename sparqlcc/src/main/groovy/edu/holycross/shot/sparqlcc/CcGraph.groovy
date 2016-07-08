package edu.holycross.shot.sparqlcc

import edu.holycross.shot.citeservlet.Sparql

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import groovy.json.JsonSlurper

/** A class interacting with a SPARQL endpoint to
 * to resolve SPARQL replies into objects in the abstract data
 * model of CITE Collection objects.
 */
class CcGraph {

  /** SPARQL endpoint object from citeservlet lib. */
  Sparql sparql


  /** Constructor with required SPARQL endpoint object */
  CcGraph(Sparql endPoint) {
    sparql = endPoint
  }

  /** Returns a valid version for an Object-level CITE Urn.
  * @param CiteUrn Can be range. Throws exception if the param is a collection-level URN
  * @returns CiteUrn (version-level)
  */
  CiteUrn resolveVersion(CiteUrn urn)
  throws Exception {
  	  Object returnObject

 	  String tempUrnString = ""
	  // Handle ranges: Be sure both sides have a version, and the same version
	  if ( urn.isRange() ){
		  CiteUrn rangeBegin = new CiteUrn(urn.getRangeBegin())
		  CiteUrn rangeEnd = new CiteUrn(urn.getRangeEnd())
		  System.err.println "${urn.toString()} --> ${rangeBegin.toString()} + ${rangeEnd.toString()}"
      CiteUrn resolvedBegin = resolveVersion(rangeBegin)
      tempUrnString = resolvedBegin.toString()
      tempUrnString += "-${rangeEnd.objectId}.${resolvedBegin.objectVersion}"
      if (rangeEnd.hasExtendedRef()){
        tempUrnString += "@${rangeEnd.getExtendedRef()}"
      }
      returnObject = new CiteUrn(tempUrnString)

	  } else {
	  // Handle single objects
		  tempUrnString = versionForObject(urn)
	  }

	  if ( tempUrnString == ""){
			returnObject = null
	  } else {
			try {
				returnObject = new CiteUrn(tempUrnString)
			} catch (Exception e ) {
				returnObject = null
			}
	  }
	  return returnObject

  }

  /** Performs a query to find a valid version for an object-level URN
  * @param CiteUrn Throws exceptions if param is collection-level or a range.
  * @returns CiteUrn (version-level) May return null if there is not a version present in the data
  */
  CiteUrn versionForObject(CiteUrn urn)
  throws Exception {
    String tempUrnString = ""
    Object returnObject = null

    if ( urn.isRange() ) {
      throw new Exception( "CcGraph.versionForObject: ${urn.toString()} cannot be a range.")
    }
    if ( !(urn.hasObjectId()) ) {
      throw new Exception( "CcGraph.versionForObject: ${urn.toString()} cannot be a collection-level urn.")
    }
    if ( !(urn.hasVersion()) && urn.hasExtendedRef() ) {
      throw new Exception( "CcGraph.versionForObject: ${urn.toString()} cannot have an extendedRef without a version identifier.")
    }
    if ( urn.hasVersion() ){
      tempUrnString = urn.toString()
      } else {
        String queryString = QueryBuilder.resolveVersionQuery(urn)
        String reply = sparql.getSparqlReply("application/json", queryString)

        JsonSlurper slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(reply)
        parsedReply.results.bindings.each { bndng ->
          if (bndng.v) {
            tempUrnString = bndng.v?.value
          }
        }

      }

      if ( tempUrnString == ""){
        returnObject = null
        } else {
          try {
            returnObject = new CiteUrn(tempUrnString)
            } catch (Exception e ) {
              returnObject = null
            }
          }

          return returnObject
        }




}
