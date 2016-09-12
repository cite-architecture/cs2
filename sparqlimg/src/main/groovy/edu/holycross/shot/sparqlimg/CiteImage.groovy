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
    CiteImage(String serverUrl, String fcgiUrl) {
        this.tripletServerUrl = serverUrl
        this.iipsrv = fcgiUrl
        this.qb = new QueryBuilder()
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
        String rightsVerb = getRightsProp(urn.toString())
        String captionVerb = getCaptionProp(urn.toString())
        CiteUrn baseUrn = new CiteUrn("urn:cite:${urn.getNs()}:${urn.getCollection()}.${urn.getObjectId()}")
        StringBuffer reply = new StringBuffer("<GetCaption xmlns='http://chs.harvard.edu/xmlns/citeimg'>\n")
        reply.append("<request>\n<urn>${urn}</urn>\n</request>\n<reply>\n")
        String q = qb.getImageInfo(baseUrn, captionVerb, rightsVerb)
	if (debug > 0) {
	  System.err.println "CiteImage: query = ${q}"
	}
        String imgReply =  getSparqlReply("application/json", q)
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
        String rightsPropReply =  getSparqlReply("application/json", qb.getRightsProp(baseUrn))
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
        String captionPropReply =  getSparqlReply("application/json", qb.getCaptionProp(baseUrn))
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
        String rightsVerb = getRightsProp(urn.toString())
        String captionVerb = getCaptionProp(urn.toString())
		CiteUrn baseUrn = new CiteUrn("urn:cite:${urn.getNs()}:${urn.getCollection()}.${urn.getObjectId()}")
        StringBuffer reply = new StringBuffer("<GetRights xmlns='http://chs.harvard.edu/xmlns/citeimg'>\n")
        reply.append("<request>\n<urn>${urn}</urn>\n</request>\n<reply>\n")
        String imgReply =  getSparqlReply("application/json", qb.getImageInfo(baseUrn, captionVerb, rightsVerb))
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
        String pathReply =  getSparqlReply("application/json", qb.binaryPathQuery(urn))
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
        String baseUrnStr = "urn:cite:${urn.getNs()}:${urn.getCollection()}.${urn.getObjectId()}"
		CiteUrn baseUrn = new CiteUrn(baseUrnStr)
        String license
        String caption
        String path

        String rightsVerb = getRightsProp(urn.toString())
        String captionVerb = getCaptionProp(urn.toString())
        String imgReply =  getSparqlReply("application/json", qb.getImageInfo(baseUrn, captionVerb, rightsVerb))
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(imgReply)
        parsedReply.results.bindings.each { b ->
            license = b.license.value
            caption = b.caption.value
        }

        String pathReply =  getSparqlReply("application/json", qb.binaryPathQuery(baseUrn))
        def pathSlurp = new groovy.json.JsonSlurper()
        def pathParse = pathSlurp.parseText(pathReply)
        pathParse.results.bindings.each { b ->
            path = b.path.value
        }

        StringBuffer reply = new StringBuffer("<GetIIPMooViewer  xmlns='http://chs.harvard.edu/xmlns/citeimg'>\n<request>\n<urn>${urn}</urn>\n<baseUrn>${baseUrn}</baseUrn>\n<roi>${roi}</roi>\n</request>\n<reply>")
        reply.append("<serverUrl val='" + iipsrv + "'/>\n")

        reply.append("<imgPath val='" + path + "/${urn.getObjectId()}.tif'/>\n")

        reply.append("<roi val='" + roi + "'/>\n")

        reply.append("<label>${caption} ${license}</label>\n")

        reply.append("</reply>\n</GetIIPMooViewer>\n")
        return reply.toString()
    }



    String getImagePlusReply(CiteUrn urn, String baseUrl) {
        String baseUrnStr = "urn:cite:${urn.getNs()}:${urn.getCollection()}.${urn.getObjectId()}"
		CiteUrn baseUrn = new CiteUrn(baseUrnStr)
        String binaryUrl = "${baseUrl}request=GetBinaryImage&amp;urn=${urn}"
        String zoomableUrl =  "${baseUrl}request=GetIIPMooViewer&amp;urn=${urn}"

        String rightsVerb = getRightsProp(urn.toString())
        String captionVerb = getCaptionProp(urn.toString())
        String caption
        String rights
        String q = qb.getImageInfo(baseUrn, captionVerb, rightsVerb)
        System.err.println "QUERY " + q
        String imgReply =  getSparqlReply("application/json", q)
        def slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(imgReply)
        parsedReply.results.bindings.each { b ->
            System.err.println "BIND " + b
            rights = b.license.value
            caption = b.caption.value
        }

        StringBuffer reply = new StringBuffer("<GetImagePlus  xmlns='http://chs.harvard.edu/xmlns/citeimg'>\n<request>\n<urn>${urn}</urn>\n\n</request>\n<reply>")
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



    /** Submits a SPARQL query to the configured endpoint
    * and returns the text of the reply.
    * @param acceptType  Value to use for headers.Accept in
    * http request.  If the value of acceptType is 'applicatoin/json'
    * fuseki's additional 'output' parameter is added to the
    * http request string so that the string returned for the
    * the request will be in JSON format.  This separates the
    * concerns of forming SPARQL queries from the decision about
    * how to parse the reply in a given format.
    * @param query Text of SPARQL query to submit.
    * @returns Text content of reply.
    */
    String getSparqlReply(String acceptType, String query) {
        String replyString
        def encodedQuery = URLEncoder.encode(query)
        def q = "${tripletServerUrl}query?query=${encodedQuery}"
        if (acceptType == "application/json") {
            q +="&output=json"
        }
		URL queryUrl = new URL(q)
        return queryUrl.getText("UTF-8")

    }


}
