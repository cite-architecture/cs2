package edu.holycross.shot.sparqlcc

//import edu.holycross.shot.citeservlet.Sparql

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*
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


  /** Returns the CiteUrn for the previous item in an ordered collection
  * @param CiteUrn
  * @returns CiteUrn
  */
  CiteUrn getPrevUrn(CiteUrn urn){
    CiteUrn rurn
    String tempUrnString

    // Only if ordered
    if ( !(isOrdered(urn))){
      throw new Exception( "CcGraph.getPrevUrn: ${urn.toString()} must be from an ordered collection.")
    }

    // If a range…
    if ( urn.isRange() ){
      rurn = resolveVersion(new CiteUrn(urn.getRangeBegin()))
    } else {
      rurn = resolveVersion(urn)
    }
    System.err.println("Working with: ${rurn}.")

      String qs = QueryBuilder.getPreviousQuery(rurn)
      String reply = sparql.getSparqlReply("application/json", qs)
      JsonSlurper slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(reply)
        parsedReply.results.bindings.each { bndng ->
          if (bndng.u) {
            tempUrnString = bndng.u?.value
          }
        }

      if (tempUrnString.contains("urn:cite:")){
        return new CiteUrn(tempUrnString)
      } else {
        return null
      }
  }

  /** Returns the CiteUrn for the Next item in an ordered collection
  * @param CiteUrn
  * @returns CiteUrn
  */
  CiteUrn getNextUrn(CiteUrn urn){
    CiteUrn rurn
    String tempUrnString

    // Only if ordered
    if ( !(isOrdered(urn))){
      throw new Exception( "CcGraph.getNextUrn: ${urn.toString()} must be from an ordered collection.")
    }

    // If a range…
    if ( urn.isRange() ){
      rurn = resolveVersion(new CiteUrn(urn.getRangeEnd()))
    } else {
      rurn = resolveVersion(urn)
    }
    System.err.println("Working with: ${rurn}.")

      String qs = QueryBuilder.getNextQuery(rurn)
      String reply = sparql.getSparqlReply("application/json", qs)
      JsonSlurper slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(reply)
        parsedReply.results.bindings.each { bndng ->
          if (bndng.u) {
            tempUrnString = bndng.u?.value
          }
        }

      if (tempUrnString.contains("urn:cite:")){
        return new CiteUrn(tempUrnString)
      } else {
        return null
      }
  }

  /** Returns all versions present for a given object
  * @param CiteUrn
  * @returns ArrayList of CiteUrns
  */
  ArrayList getVersionsOfObject(CiteUrn urn)
  throws Exception {
    ArrayList replyArray = []
    String tempUrnString
    CiteUrn queryUrn
    if ( urn.isRange() ){
      String rangeBeginString = urn.getRangeBegin()
      CiteUrn rangeBeginUrn = new CiteUrn(rangeBeginString)
      if (rangeBeginUrn.hasObjectId()){
        tempUrnString = rangeBeginUrn.reduceToObject()
        } else { // Is not an object-level URN
          throw new Exception( "CcGraph.getVersionsOfObject: ${rangeBeginUrn} is not an object-level URN.")
        }
        } else {
          if (urn.hasObjectId()){
            tempUrnString = urn.reduceToObject()
            } else { // Is not an object-level URN
              throw new Exception( "CcGraph.getVersionsOfObject: ${urn} is not an object-level URN.")
            }
          }
          queryUrn = new CiteUrn(tempUrnString)
          String qs = QueryBuilder.getVersionsOfObjectQuery(queryUrn)
          String reply = sparql.getSparqlReply("application/json", qs)
          JsonSlurper slurper = new groovy.json.JsonSlurper()
          def parsedReply = slurper.parseText(reply)
          parsedReply.results.bindings.each { bndng ->
            if (bndng.v) {
              try{
                replyArray << new CiteUrn( bndng.v?.value )
                } catch (Exception e ) {
                  throw new Exception ( "CcGraph.getVersionsOfObject: Bad data ( ${bndng.v?.value} )")
                }
              }
            }
            return replyArray
          }


  /** Returns the CiteUrn for the first object in an ordered collection.
  * @param CiteUrn
  * @returns CiteUrn
  */
  CiteUrn getFirstUrn(CiteUrn urn)
  throws Exception {
    // Only if ordered
    if ( !(isOrdered(urn))){
      throw new Exception( "CcGraph.getFirstUrn: ${urn.toString()} must be from an ordered collection.")
      } else {
        CiteUrn collUrn = new CiteUrn(urn.reduceToCollection())
        String qs = QueryBuilder.getFirstQuery(collUrn)
        System.err.println(qs)
        String reply = sparql.getSparqlReply("application/json", qs)
        String tempUrnString = ""
        JsonSlurper slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(reply)
        System.err.println(parsedReply)
        parsedReply.results.bindings.each { bndng ->
          if (bndng.urn) {
            tempUrnString = bndng.urn?.value
          }
        }
        if ((tempUrnString == "") || (tempUrnString == null)){
          throw new Exception( "CcGraph.getFirstUrn: ${urn.toString()}. No valid firstUrn for collection ${collUrn.toString()}")
        } else {
          System.err.println(tempUrnString)
          return new CiteUrn(tempUrnString)
        }
      }

  }


  /** Returns the CiteUrn for the last object in an ordered collection.
  * @param CiteUrn
  * @returns CiteUrn
  */
  CiteUrn getLastUrn(CiteUrn urn){
    // Only if ordered
    if ( !(isOrdered(urn))){
      throw new Exception( "CcGraph.getLastUrn: ${urn.toString()} must be from an ordered collection.")
      } else {
        CiteUrn collUrn = new CiteUrn(urn.reduceToCollection())
        String qs = QueryBuilder.getLastQuery(collUrn)
        System.err.println(qs)
        String reply = sparql.getSparqlReply("application/json", qs)
        String tempUrnString = ""
        JsonSlurper slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(reply)
        System.err.println(parsedReply)
        parsedReply.results.bindings.each { bndng ->
          if (bndng.urn) {
            tempUrnString = bndng.urn?.value
          }
        }
        if ((tempUrnString == "") || (tempUrnString == null)){
          throw new Exception( "CcGraph.getFirstUrn: ${urn.toString()}. No valid firstUrn for collection ${collUrn.toString()}")
        } else {
          System.err.println(tempUrnString)
          return new CiteUrn(tempUrnString)
        }
      }
  }

  /**
  * Returns a count of objects in a collection, based on a CiteUrn.
  * With a collection-level URN, returns a count of all versions of all
  * objects. With a version-level URN, counts only objects with the same
  * version-string.
  * @param CiteUrn
  * @returns BigInteger
  */
    BigInteger getCollectionSize(CiteUrn urn){
    String versionString = null
    CiteUrn qUrn
    if(urn.hasVersion()){
       versionString = urn.objectVersion
    }
    qUrn = new CiteUrn(urn.reduceToCollection())

    return getCollectionSize(qUrn,versionString)
  }

  BigInteger getCollectionSize(CiteUrn urn, String versionString)
  throws Exception {
    CiteUrn qUrn
    String qVersion
    String qs
    if(urn.hasVersion()){
      if( (versionString == "") || (versionString == null)){
        qVersion = urn.objectVersion
        qUrn = new CiteUrn(urn.reduceToCollection())
      }
      } else {
        qVersion = versionString
        qUrn = new CiteUrn(urn.reduceToCollection())
      }

      if ((versionString != "") && (versionString != null)){
        qs = QueryBuilder.getVersionedCollectionSizeQuery(qUrn,qVersion)
        } else {
          qs = QueryBuilder.getCollectionSizeQuery(qUrn)
        }

        String reply = sparql.getSparqlReply("application/json", qs)
        String tempUrnString = ""
        JsonSlurper slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(reply)
        if (parsedReply.results.bindings.size) {
          System.err.println(parsedReply.results.bindings)
          System.err.println(parsedReply.results.bindings.getClass())
          return new BigInteger(parsedReply.results.bindings[0].size.value)
          } else {
            throw new Exception( "CcGraph.getCollectionSize: ${urn.toString()}. Failed to get count.")
          }
        }


  /** Returns all valid CiteUrns in a collection.
  * If the input parameter CiteUrn has a version-id, returns
  * only URNs with the same version-id. If the param is a range,
  * returns all URNs contained in that range (for ordered collections),
  * or the first- and last- elements of the range-urn for unordered collections.
  * For queries on notional
  * @param CiteUrn
  * @returns ArrayList of URN-Strings
  */
  ArrayList getValidReff(CiteUrn urn){
    // Is it an object or a range?
    if ( urn.isRange() || urn.hasObjectId() ){
      return gvrForRange(urn)
    } else {
      return gvrForCollection(urn)
    }
  }

  ArrayList getValidReff(CiteUrn urn, String versionString)
  throws Exception {
    CiteUrn qUrn = new CiteUrn(urn.reduceToCollection())
    return gvrForCollection(qUrn,versionString)
  }

  ArrayList gvrForCollection(CiteUrn urn, String versionString)
  throws Exception {
    ArrayList replyArray = []
    String qs
    String qUrnString = urn.reduceToCollection()
    CiteUrn qUrn = new CiteUrn(qUrnString)
    if ( (versionString =="") || ( versionString == null)){
      if (isOrdered(qUrn)){
        qs = QueryBuilder.getGVROrderedCollectionQuery(qUrn)
      } else {
          qs = QueryBuilder.getGVRCollectionQuery(qUrn)
      }
    } else {
        if (isOrdered(qUrn)){
          qs = QueryBuilder.getGVROrderedCollectionVersionedQuery(qUrn, versionString)
        } else {
          qs = QueryBuilder.getGVRCollectionVersionedQuery(qUrn, versionString)
        }
    }

    String reply = sparql.getSparqlReply("application/json", qs)
    String tempUrnString = ""
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
    parsedReply.results.bindings.each { bndng ->
      if (bndng.urn) {
          replyArray << bndng.urn.value
      }
    }
      return replyArray
    }

  ArrayList gvrForCollection(CiteUrn urn)
  throws Exception {
    return gvrForCollection(urn, null)
  }

  ArrayList gvrForRange(CiteUrn urn)
  throws Exception {
    ArrayList replyArray = []
    // is not range
    if (!urn.isRange()){
        if(urn.hasVersion()){
          replyArray << urn.toString()
        } else {
          getVersionsOfObject(urn).each{ v ->
            replyArray << v.toString()
          }
        }
    } else { // is a range
      if (isOrdered(urn)){
        String rStart = urn.getRangeBegin()
        String rEnd = urn.getRangeEnd()
        CiteUrn rStartUrn = resolveVersion(new CiteUrn(rStart))
        CiteUrn rEndUrn = resolveVersion(new CiteUrn(rEnd))
        String vs = rStartUrn.objectVersion
        // replyArray = ["for range; ordered collection"]
        ArrayList tempArray = gvrForCollection(rStartUrn, vs)
        Boolean collecting = false
        tempArray.each { cco ->
            if( cco == rStartUrn.toString()){
              collecting = true
            }
            if( collecting ){
              replyArray << cco
              if (cco == rEndUrn.toString()){
                collecting = false
              }
            }
        }
      } else {
        System.err.println("${urn} is not ordered.")
         CiteUrn rangeStart = new CiteUrn(urn.getRangeBegin())
         CiteUrn rangeEnd = new CiteUrn(urn.getRangeEnd())
        if (rangeStart.hasVersion()){
            System.err.println("${rangeStart} hasVersion.")
            replyArray << urn.getRangeBegin()
            replyArray << urn.getRangeEnd()
        } else {
            System.err.println("${rangeStart} does not have Version.")
           getVersionsOfObject(rangeStart).each{ v ->
             replyArray << v.toString()
           }
           getVersionsOfObject(rangeEnd).each{ v ->
             replyArray << v.toString()
           }
        }
      }
    }

    return replyArray
  }

  CiteCollectionObject getObject(CiteUrn urn){
    // Make a CiteCollection object

    // Make a CiteCollectionObject object
    return null
  }

  /** Returns a Map of full: and abbr: namespaces
  * for a Cite Collection
  * @param CiteUrn
  * @returns Map "full:" and "abbr:"
  */
  Map getCollectionNamespace(CiteUrn urn)
  throws Exception {
    def nsMap = [:]
    nsMap['abbr'] = urn.ns
    nsMap['full'] = "not implemented yet"


    String qs = QueryBuilder.getNamespaceQuery(urn.ns)
    String reply = sparql.getSparqlReply("application/json", qs)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
    if (parsedReply.results.bindings.full) {
        nsMap['full'] = parsedReply.results.bindings[0].full.value
      } else {
         throw new Exception( "CcGraph.getCollectionNamespace: ${urn.toString()}. Failed to get namespace.")
      }

    return nsMap
  }

  /** Return an ArrayList of Map objects
  * @param CiteUrn
  * @returns ArrayList of Map
  */
  ArrayList getPropertiesInCollection(CiteUrn urn){
      CiteUrn collUrn = new CiteUrn(urn.reduceToCollection())
      def collProps = []
      def tempMap = [:]

    String qs = QueryBuilder.getPropertiesForCollectionQuery(collUrn)
    String reply = sparql.getSparqlReply("application/json", qs)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
    parsedReply.results.bindings.each{ pp ->
      tempMap = [property:pp.property.value, label:pp.label.value, type:pp.type.value]
      collProps << tempMap
    }

    return collProps
  }

  CCOSet getRange(CiteUrn urn){
    return null
  }

  CCOSet getPaged(CiteUrn urn, Integer offset, Integer limit){
    return null
  }


  ArrayList getPropertiesForObject(CiteUrn urn){

  }

  String formatReply(String request, Map, params) {
    return ""
  }


}
