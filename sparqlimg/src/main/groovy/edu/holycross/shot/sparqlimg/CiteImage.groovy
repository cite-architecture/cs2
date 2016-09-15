package edu.holycross.shot.sparqlimg

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*
import groovy.json.JsonSlurper




/** Implementation of all requests of the CHS Image Extension to the
* CITE Collection service.
*/
class CiteImage {


  Integer debug = 0

  Sparql sparql


  /** String value of URL for IIPSrv fast cgi. */
  String iipsrv

  /** SPARQL query endpoint for HMT graph triples.   */
  String tripletServerUrl

  /** QueryGenerator object formulating SPARQL query strings. */
  QueryBuilder qb


    /** Constructor initializing required values
    * @param serverUrl String value for URL of sparql endpoint to query.
    * @param fcgiUrl String value for URL of IIPSrv fast cgi.
.   */
    CiteImage(Sparql endpoint, String fcgiUrl) {
        this.sparql = endpoint
        this.iipsrv = fcgiUrl
        this.qb = new QueryBuilder()
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
				throw new Exception("SparqlImg: Range URNs are not valid parameters for CITE Image Extension requests.")
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
	      throw new Exception( "CiteImg: versionForObject: ${urn.toString()} cannot be a range.")
	    }
	    if ( !(urn.hasObjectId()) ) {
	      throw new Exception( "CiteImg: versionForObject: ${urn.toString()} cannot be a collection-level urn.")
	    }
	    if ( !(urn.hasVersion()) && urn.hasExtendedRef() ) {
	      throw new Exception( "CiteImg: versionForObject: ${urn.toString()} cannot have an extendedRef without a version identifier.")
	    }
	    if ( urn.hasVersion() ){
	      tempUrnString = urn.toString()
	      } else {
	        String queryString = qb.resolveVersionQuery(urn)
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

	  /** Returns 'true' if an object identified by a Cite URN is a member of an ordered collection.
	  * @param CiteUrn
	  * @returns Boolean
	  */
	  Boolean isOrdered(CiteUrn urn)
	  throws Exception {
	      String collectionUrnStr = urn.reduceToCollection()
	      CiteUrn collectionUrn = new CiteUrn(collectionUrnStr)
	      String qs = QueryBuilder.isOrderedQuery(collectionUrn)
	      String reply = sparql.getSparqlReply("application/json", qs)
	      JsonSlurper slurper = new groovy.json.JsonSlurper()
	      def parsedReply = slurper.parseText(reply)
	      //System.err.println qs
	      //System.err.println parsedReply
	      //System.err.println "${urn} >> ${collectionUrn}"
	      return parsedReply.boolean == true
	  }

    /**
    * Composes a String validating against the .rng schema for the GetCaption reply.
    * @param urnStr URN, as a String, of image.
    * @returns A valid reply to the CiteImage GetCaption request.
    */
    String getCaptionReply(String urnStr)
    throws Exception {
        CiteUrn urn = new CiteUrn(urnStr)
        CiteUrn baseUrn = new CiteUrn("urn:cite:${urn.getNs()}:${urn.getCollection()}.${urn.getObjectId()}")
        return getCaptionReply(baseUrn)
    }


    /**
    * Composes a String validating against the .rng schema for the GetCaption reply.
    * @param urn URN of the image.
    * @returns A valid reply to the CiteImage GetCaption request.
    */
    String getCaptionReply(CiteUrn urn) {
			CiteUrn resolvedUrn = resolveVersion(urn)
        String rightsVerb = getRightsProp(urn.toString())
        String captionVerb = getCaptionProp(urn.toString())
			CiteUrn baseUrn = new CiteUrn("urn:cite:${resolvedUrn.getNs()}:${resolvedUrn.getCollection()}.${resolvedUrn.getObjectId()}.${resolvedUrn.getObjectVersion()}")
        StringBuffer reply = new StringBuffer("<GetCaption xmlns='http://chs.harvard.edu/xmlns/citeimg'>\n")
        reply.append("<request>\n<urn>${urn}</urn>\n<resolvedUrn>${resolvedUrn}</resolvedUrn>\n</request>\n<reply>\n")
        String q = qb.getImageInfo(baseUrn, captionVerb, rightsVerb)
	if (debug > 0) {
	  System.err.println "CiteImage: query = ${q}"
	}
        String imgReply =  sparql.getSparqlReply("application/json", q)
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(imgReply)
        parsedReply.results.bindings.each { b ->
            reply.append("<caption>${b.caption?.value}</caption>\n")
        }
        reply.append("</reply>\n</GetCaption>\n")
        return reply.toString()
    }


    /**
    * Composes a string that represents the TTL verb
    * for grabbing the Rights property of an image-collection-object
    * @param urnStr URN, as a String, of the image.
    * @returns a namespaced RDF verb.
    */
    String getRightsProp(String urnStr)
    throws Exception {
        CiteUrn urn = new CiteUrn(urnStr)
		CiteUrn baseUrn = new CiteUrn("urn:cite:${urn.getNs()}:${urn.getCollection()}.${urn.getObjectId()}")
        String rightsPropReply =  sparql.getSparqlReply("application/json", qb.getRightsProp(baseUrn))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(rightsPropReply)
        String prop = ""
        parsedReply.results.bindings.each { b ->
            prop = b?.prop.value
        }
        String verb = "citedata:"
        verb += prop
        return verb
    }

    /**
    * Composes a string that represents the TTL verb
    * for grabbing the Caption property of an image-collection-object
    * @param urnStr URN, as a String, of the image.
    * @returns a namespaced RDF verb.
    */
    String getCaptionProp(String urnStr)
    throws Exception {
        CiteUrn urn = new CiteUrn(urnStr)
		CiteUrn baseUrn = new CiteUrn("urn:cite:${urn.getNs()}:${urn.getCollection()}.${urn.getObjectId()}")
        String captionPropReply =  sparql.getSparqlReply("application/json", qb.getCaptionProp(baseUrn))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(captionPropReply)
        String prop = ""
        parsedReply.results.bindings.each { b ->
            prop = b?.prop.value
        }
        String verb = "citedata:"
        verb += prop
        return verb
    }

    /**
    * Composes a String validating against the .rng schema for the GetRights reply.
    * @param urnStr URN, as a String, of the image.
    * @returns A valid reply to the CiteImage GetRights request.
    */
    String getRightsReply(String urnStr)
    throws Exception {
        CiteUrn urn = new CiteUrn(urnStr)
		CiteUrn baseUrn = new CiteUrn("urn:cite:${urn.getNs()}:${urn.getCollection()}.${urn.getObjectId()}")
        return getRightsReply(baseUrn)
    }

		/**
		* Composes a String validating against the .rng schema for the GetRights reply.
		* @param urn URN of the image.
		* @returns A valid reply to the CiteImage GetRights request.
		*/
		String getRightsReply(CiteUrn urn){
			CiteUrn resolvedUrn = resolveVersion(urn)
			String rightsVerb = getRightsProp(urn.toString())
			String captionVerb = getCaptionProp(urn.toString())
			CiteUrn baseUrn = new CiteUrn("urn:cite:${resolvedUrn.getNs()}:${resolvedUrn.getCollection()}.${resolvedUrn.getObjectId()}.${resolvedUrn.getObjectVersion()}")

			StringBuffer reply = new StringBuffer("<GetRights xmlns='http://chs.harvard.edu/xmlns/citeimg'>\n")

			reply.append("<request>\n<urn>${urn}</urn>\n<resolvedUrn>${resolvedUrn}</resolvedUrn>\n</request>\n<reply>\n")

			System.err.println(qb.getImageInfo(baseUrn, captionVerb, rightsVerb))

			String imgReply =  sparql.getSparqlReply("application/json", qb.getImageInfo(baseUrn, captionVerb, rightsVerb))

			def slurper = new groovy.json.JsonSlurper()

			def parsedReply = slurper.parseText(imgReply)

			parsedReply.results.bindings.each { b ->
				reply.append("<rights>${b.license.value}</rights>\n")
			}

			reply.append("</reply>\n</GetRights>\n")
			return reply.toString()
		}



    /** Determines iip fastcgi URL to binary image.
    * @param urnStr URN, as a String, of the image.
    * @returns A String value with a valid URN to a binary image.
    */
    String getBinaryRedirect(String urnStr)
    throws Exception {
        CiteUrn urn = new CiteUrn(urnStr)
        return getBinaryRedirect(urn)
    }



    /** Determines iip fastcgi URL to binary image.
    * @param urnStr URN, as a String, of the image.
    * @returns A String value with a valid URN to a binary image.
    */
    String getBinaryRedirect(CiteUrn urn) {
        String pathReply =  sparql.getSparqlReply("application/json", qb.binaryPathQuery(urn))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(pathReply)
        String path = ""
        parsedReply.results.bindings.each { b ->
            path = b.path.value
        }
        return "${iipsrv}?OBJ=IIP,1.0&FIF=${path}/${urn.getObjectId()}.tif"
    }



    String getIIPMooViewerReply(CiteUrn urn) {
        String roi = urn.getExtendedRef()
			CiteUrn resolvedUrn = resolveVersion(urn)
			CiteUrn baseUrn = new CiteUrn("urn:cite:${resolvedUrn.getNs()}:${resolvedUrn.getCollection()}.${resolvedUrn.getObjectId()}.${resolvedUrn.getObjectVersion()}")
        String license
        String caption
        String path

        String rightsVerb = getRightsProp(urn.toString())
        String captionVerb = getCaptionProp(urn.toString())
        String imgReply =  sparql.getSparqlReply("application/json", qb.getImageInfo(baseUrn, captionVerb, rightsVerb))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(imgReply)
        parsedReply.results.bindings.each { b ->
            license = b.license.value
            caption = b.caption.value
        }

        String pathReply =  sparql.getSparqlReply("application/json", qb.binaryPathQuery(baseUrn))
        def pathSlurp = new groovy.json.JsonSlurper()
        def pathParse = pathSlurp.parseText(pathReply)
        pathParse.results.bindings.each { b ->
            path = b.path.value
        }

        StringBuffer reply = new StringBuffer("<GetIIPMooViewer  xmlns='http://chs.harvard.edu/xmlns/citeimg'>\n<request>\n")
				reply.append("<urn>${urn}</urn>\n<resolvedUrn>${resolvedUrn}</resolvedUrn>\n")
				reply.append("<baseUrn>${baseUrn}</baseUrn>\n<roi>${roi}</roi>\n</request>\n<reply>")
        reply.append("<serverUrl val='" + iipsrv + "'/>\n")

        reply.append("<imgPath val='" + path + "/${urn.getObjectId()}.tif'/>\n")

        reply.append("<roi val='" + roi + "'/>\n")

        reply.append("<label>${caption} ${license}</label>\n")

        reply.append("</reply>\n</GetIIPMooViewer>\n")
        return reply.toString()
    }



    String getImagePlusReply(CiteUrn urn, String baseUrl) {
			CiteUrn resolvedUrn = resolveVersion(urn)
			CiteUrn baseUrn = new CiteUrn("urn:cite:${resolvedUrn.getNs()}:${resolvedUrn.getCollection()}.${resolvedUrn.getObjectId()}.${resolvedUrn.getObjectVersion()}")
        String binaryUrl = "${baseUrl}request=GetBinaryImage&amp;urn=${resolvedUrn}"
        String zoomableUrl =  "${baseUrl}request=GetIIPMooViewer&amp;urn=${resolvedUrn}"

        String rightsVerb = getRightsProp(urn.toString())
        String captionVerb = getCaptionProp(urn.toString())
        String caption
        String rights
        String q = qb.getImageInfo(baseUrn, captionVerb, rightsVerb)
        System.err.println "QUERY " + q
        String imgReply =  sparql.getSparqlReply("application/json", q)
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(imgReply)
        parsedReply.results.bindings.each { b ->
            System.err.println "BIND " + b
            rights = b.license.value
            caption = b.caption.value
        }

        StringBuffer reply = new StringBuffer("<GetImagePlus  xmlns='http://chs.harvard.edu/xmlns/citeimg'>\n")
			reply.append("<request>\n<urn>${urn}</urn>\n<resolvedUrn>${resolvedUrn}</resolvedUrn>\n</request>\n<reply>\n")
        reply.append("<caption>${caption}</caption>\n")
        reply.append("<rights>${rights}</rights>\n")
        reply.append("<binaryUrl>${binaryUrl}</binaryUrl>\n")
        reply.append("<zoomableUrl>${zoomableUrl}</zoomableUrl>\n")
        reply.append("</reply>\n</GetImagePlus>\n")
        return reply.toString()
    }


    /* returns JSON string */
    String summarizeGroups() {
        StringBuffer reply = new StringBuffer()
        String ctsReply =  getSparqlReply("application/json", qb.summarizeGroupsQuery())
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(ctsReply)
        parsedReply.results.bindings.each { b ->
            reply.append(b)
        }
        return reply.toString()
    }




}
