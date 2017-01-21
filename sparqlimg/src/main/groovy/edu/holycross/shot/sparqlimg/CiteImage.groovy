package edu.holycross.shot.sparqlimg

import edu.harvard.chs.cite.Cite2Urn
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

	/** Base URL for this service */
	String baseUrl

  /** QueryGenerator object formulating SPARQL query strings. */
  QueryBuilder qb


    /** Constructor initializing required values
    * @param serverUrl String value for URL of sparql endpoint to query.
    * @param fcgiUrl String value for URL of IIPSrv fast cgi.
.   */
    CiteImage(Sparql endpoint, String fcgiUrl, String baseUrlString) {
        this.sparql = endpoint
        this.iipsrv = fcgiUrl
        this.baseUrl = baseUrlString
        this.qb = new QueryBuilder()
    }

  /** Returns a valid version for an Object-level CITE Urn.
	* Throws exception if there is not versioned collection that contains the
	* identified object.
  * @param Cite2Urn Can be range.
  * @returns Cite2Urn
  */
	Cite2Urn resolveVersion(Cite2Urn urn)
	throws Exception {
		Cite2Urn returnUrn

		if (urn.hasCollectionVersion()){
			returnUrn = urn
		} else{
			if (urn.isRange()){
				Cite2Urn collUrn = urn.reduceToCollection()
				ArrayList vfc = versionsForCollection(collUrn)
				if (vfc.size() > 0){
					Cite2Urn testUrn
					vfc.each{ curn ->
							testUrn = new Cite2Urn("${curn}${urn.objectId_1}-${urn.objectId_2}")
							if (objectExists(testUrn)){
									returnUrn = testUrn
							} else {
								throw new Exception("CiteImage, resolveVersion: No versions of  ${urn} exist in data.")
							}
					}

				} else {
					throw new Exception("CiteImage, resolveVersion: No versions of collection ${urn.reduceToCollection()} exist in data.")
				}

			} else {

				if (urn.hasObjectId()){
					ArrayList vfo = versionsForObject(urn)
					if (vfo.size() > 0){
						returnUrn = vfo[0]
					} else {
						throw new Exception("CiteImage,, resolveVersion: No versions of collection ${urn.reduceToCollection()} contain an object identified as ${urn}")
					}
				} else { // it is a collection-level URN
					returnUrn = versionsForCollection(urn)[0]
				}
			}
		}

		return returnUrn
	}

	/** Performs a query to find all valid versioned collections given a Cite2Urn (at any level)
	* @param Cite2Urn
	* @returns ArrayList of Cite2Urns at the collection-version level
	*/
	ArrayList versionsForCollection(Cite2Urn urn)
	throws Exception {
		String tempUrnString = ""
		def versionArray = []
		Cite2Urn collUrn = urn.reduceToCollection()
		Cite2Urn tempUrn

    String qs = QueryBuilder.getVersionsForCollectionQuery(collUrn)
    String reply = sparql.getSparqlReply("application/json", qs)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
      parsedReply.results.bindings.each { bndng ->
        if (bndng.cv) {
					try {
	          tempUrn = new Cite2Urn(bndng.cv?.value)
					} catch (Exception e){
						throw new Exception("CCGraph, versionsForCollection. Could not turn ${bndng.cv?.value} into a Cite2 URN: ${e}")
					}
					versionArray << tempUrn
        }
      }

		return versionArray
	}

	/** Performs a query to find a all valid versioned collection that contain an object-ID
	* @param Cite2Urn
	* @returns ArrayList of Cite2Urns
	*/
	ArrayList versionsForObject(Cite2Urn urn)
	throws Exception {
		ArrayList vfo = []
		Cite2Urn collUrn
		Cite2Urn testUrn
		if ( !(urn.hasObjectId())){
			throw new Exception("CcGraph, versionsForObject: ${urn} does not have an object id.")
		} else {
			String objStr = urn.getObjectId()
			collUrn = urn.reduceToCollection()
			ArrayList collVersions = versionsForCollection(collUrn)
			if (collVersions.size() < 1){
				throw new Exception("CcGraph, versionsForObject: ${collUrn} is not represented in this data by any version.")
			} else {
				collVersions.each{ cv ->
						testUrn = new Cite2Urn("${cv}${objStr}")
						if (objectExists(testUrn)){
							vfo << testUrn
						}
				}

			}

		}

		return vfo
	}

  /** Returns 'true' if an object identified by a Cite URN is a member of an ordered collection.
  * @param CiteUrn
  * @returns Boolean
  */
  Boolean isOrdered(Cite2Urn urn)
  throws Exception {
      Cite2Urn collectionUrn = resolveVersion(urn).reduceToCollectionVersion()
      String qs = QueryBuilder.isOrderedQuery(collectionUrn)
      String reply = sparql.getSparqlReply("application/json", qs)
      JsonSlurper slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(reply)
      return parsedReply.boolean == true
  }

    /**
    * Composes a String validating against the .rng schema for the GetCaption reply.
    * @param urnStr URN, as a String, of image.
    * @returns A valid reply to the CiteImage GetCaption request.
    */
    String getCaptionReply(String urnStr)
    throws Exception {
        Cite2Urn urn = new Cite2Urn(urnStr)
        Cite2Urn baseUrn = urn.reduceToObject()
        return getCaptionReply(baseUrn)
    }


    /**
    * Composes a String validating against the .rng schema for the GetCaption reply.
    * @param urn URN of the image.
    * @returns A valid reply to the CiteImage GetCaption request.
    */
    String getCaptionReply(Cite2Urn urn) {
			Cite2Urn resolvedUrn = resolveVersion(urn).reduceToObject()
        String rightsVerb = getRightsProp(resolvedUrn.toString())
        String captionVerb = getCaptionProp(resolvedUrn.toString())
			Cite2Urn baseUrn = urn.reduceToObject()
        StringBuffer reply = new StringBuffer("<GetCaption xmlns='http://chs.harvard.edu/xmlns/citeimg'>\n")
        reply.append("<request>\n<urn>${urn}</urn>\n<resolvedUrn>${resolvedUrn}</resolvedUrn>\n</request>\n<reply>\n")
        String q = qb.getImageInfo(resolvedUrn, captionVerb, rightsVerb)
				System.err.println("------ qs ----\n${q}\n-------")
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
        Cite2Urn urn = new Cite2Urn(urnStr)
				Cite2Urn baseUrn = resolveVersion(urn).reduceToObject()
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
        Cite2Urn urn = new Cite2Urn(urnStr)
				Cite2Urn baseUrn = resolveVersion(urn).reduceToObject()
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
        Cite2Urn urn = new Cite2Urn(urnStr)
				Cite2Urn baseUrn = urn.reduceToObject()
        return getRightsReply(baseUrn)
    }

		/**
		* Composes a String validating against the .rng schema for the GetRights reply.
		* @param urn URN of the image.
		* @returns A valid reply to the CiteImage GetRights request.
		*/
		String getRightsReply(Cite2Urn urn){
			Cite2Urn resolvedUrn = resolveVersion(urn)
			String rightsVerb = getRightsProp(urn.toString())
			String captionVerb = getCaptionProp(urn.toString())
			Cite2Urn baseUrn = resolvedUrn.reduceToObject()

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
        Cite2Urn urn = new Cite2Urn(urnStr)
        return getBinaryRedirect(urn)
    }



    /** Determines iip fastcgi URL to binary image.
    * @param urnStr URN, as a String, of the image.
    * @returns A String value with a valid URN to a binary image.
    */
    String getBinaryRedirect(Cite2Urn urn) {
				Cite2Urn resolvedUrn = resolveVersion(urn).reduceToObject()
        String pathReply =  sparql.getSparqlReply("application/json", qb.binaryPathQuery(resolvedUrn))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(pathReply)
        String path = ""
        parsedReply.results.bindings.each { b ->
            path = b.path.value
        }
        return "${iipsrv}?OBJ=IIP,1.0&FIF=${path}/${resolvedUrn.getObjectId()}.tif"
    }

		String getExtendedCollectionsReply(String extensionString){

        String extendedCollectionsReply =  sparql.getSparqlReply("application/json", qb.getExtendedCollectionQuery(extensionString))
        def pathSlurp = new groovy.json.JsonSlurper()
        def pathParse = pathSlurp.parseText(extendedCollectionsReply)
				ArrayList<String> colls = []
        pathParse.results.bindings.each { b ->
            colls << b.coll.value
        }

        StringBuffer reply = new StringBuffer("<GetExtendedCollections  xmlns='http://chs.harvard.edu/xmlns/citeimg'>\n<request>\n")
				reply.append("</request>\n<reply>\n")
					colls.each { cc ->
		        reply.append("""<collection urn="${cc}"/>\n""")
					}

        reply.append("</reply>\n</GetExtendedCollections>\n")
        return reply.toString()

		}

    String getIIPMooViewerReply(Cite2Urn urn) {
        String roi = urn.getExtendedRef()
			Cite2Urn resolvedUrn = resolveVersion(urn)
			Cite2Urn baseUrn = resolvedUrn.reduceToObject()
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



    String getImagePlusReply(Cite2Urn urn) {
			Cite2Urn resolvedUrn = resolveVersion(urn)
			Cite2Urn baseUrn = resolvedUrn.reduceToObject()
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


  /** returns 'true' if an object identified by a cite urn is present in the dataset;
	* for a range, returns 'true' if the beginning- and ending-objects are present.
  * @param cite2urn
  * @returns boolean
  */
  boolean objectExists(Cite2Urn urn)
		throws Exception{
			Boolean returnValue
			String qs
			String reply
			JsonSlurper slurper
			def parsedReply

			if ( !(urn.hasObjectId())){
				throw new Exception("CiteImage, objectExists: ${urn} does not identify an object.")
			}
			Cite2Urn testUrn = urn.reduceToObject()
			if (testUrn.isRange()){
				Cite2Urn beginUrn = testUrn.getRangeBegin()
	      qs = QueryBuilder.getObjectExistsQuery(beginUrn)
	      reply = sparql.getSparqlReply("application/json", qs)
	      slurper = new groovy.json.JsonSlurper()
	      parsedReply = slurper.parseText(reply)
	      returnValue =  parsedReply.boolean == true

				Cite2Urn endUrn = testUrn.getRangeEnd()
	      qs = QueryBuilder.getObjectExistsQuery(endUrn)
	      reply = sparql.getSparqlReply("application/json", qs)
	      slurper = new groovy.json.JsonSlurper()
	      parsedReply = slurper.parseText(reply)
	      returnValue =  ( returnValue && (parsedReply.boolean == true))

			} else {

	      qs = QueryBuilder.getObjectExistsQuery(testUrn)
	      reply = sparql.getSparqlReply("application/json", qs)
	      slurper = new groovy.json.JsonSlurper()
	      parsedReply = slurper.parseText(reply)
	      returnValue =  parsedReply.boolean == true

			}

			return returnValue
	}


}
