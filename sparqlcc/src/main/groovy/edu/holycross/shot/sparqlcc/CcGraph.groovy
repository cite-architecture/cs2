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
        String reply = sparql.getSparqlReply("application/json", qs)
        String tempUrnString = ""
        JsonSlurper slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(reply)
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
         CiteUrn rangeStart = new CiteUrn(urn.getRangeBegin())
         CiteUrn rangeEnd = new CiteUrn(urn.getRangeEnd())
        if (rangeStart.hasVersion()){
            replyArray << urn.getRangeBegin()
            replyArray << urn.getRangeEnd()
        } else {
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


  /* Returns a CiteCollection
  * @param CiteUrn
  * @returns CiteCollection object
  */
  CiteCollection getCollection(CiteUrn urn){
    String tempUrn = urn.reduceToCollection()


  	CiteUrn collUrn = new CiteUrn(tempUrn)

  	CiteProperty idProp = getCollectionIdProp(collUrn)
    String collectionProp = labelProp.label
    String collectionLabel = getCollectionLabel(collUrn)
  	CiteProperty labelProp = new CiteProperty("CollectionLabel",CitePropertyType.STRING,"Collection Label")
    CiteProperty orderedByProp = null
    if (isOrdered(collUrn)){
    	orderedByProp = getCollectionOrderedByProp(collUrn)
    }
    ArrayList collProps = getPropertiesInCollection(collUrn)
    collProps << labelProp // this isn't in RDF like a proper property

  	ArrayList extensions = getCollectionExtensions(collUrn)

  	Map nss = getCollectionNamespace(collUrn)
  	String nsAbbr = nss['abbr']
  	String nsFull = nss['full']

    try {
    CiteCollection cc = new CiteCollection(collUrn, collectionLabel, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

    return cc
    } catch (Exception e) {
      System.err.println(e)
      throw new Exception( "CcGraph.getCollection: ${urn.toString()}. Could not create collection.")
    }
  }

  /** Returns a CiteProperty with the Canonical Id property
  * of a Cite collection
  * @param CiteUrn
  * @returns CiteProperty
  */
  CiteProperty getCollectionIdProp(CiteUrn urn)
  throws Exception {

        CiteUrn collUrn = new CiteUrn(urn.reduceToCollection())

        String qs = QueryBuilder.getCanonicalIdPropQuery(collUrn)
        String reply = sparql.getSparqlReply("application/json", qs)
        String tempUrnString = ""
        JsonSlurper slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(reply)

        String pName
        String pLabel
        CitePropertyType pType
        System.err.println(parsedReply)

        if (parsedReply.results.bindings[0].name){
          String ts1
          String ts2
          ts1 = parsedReply.results.bindings[0].name.value.toString()
          ts2 = ts1.substring(ts1.lastIndexOf("/") + 1)
          pName = ts2.tokenize("_")[1]
        } else {
          throw new Exception( "CcGraph.getCollectionIdProp: ${urn.toString()}. Failed to get property name.")
        }
        if (parsedReply.results.bindings[0].type){
          pType = CitePropertyType.CITE_URN // it has to be
        } else {
          throw new Exception( "CcGraph.getCollectionIdProp: ${urn.toString()}. Failed to get property type.")
        }
        if (parsedReply.results.bindings[0].label){
          pLabel = parsedReply.results.bindings[0].label.value
        } else {
          throw new Exception( "CcGraph.getCollectionIdProp: ${urn.toString()}. Failed to get property label.")
        }
        try {
           System.err.println("${pName} ${pType} ${pLabel}")
            CiteProperty canonicalId = new CiteProperty(pName,pType,pLabel)
            System.err.println(canonicalId)
            return canonicalId
        } catch (Exception e) {
          throw new Exception( "CcGraph.getCollectionIdProp: ${urn.toString()}. Could not make property out of name = ${pName}, type = ${pType}, label = ${pLabel}.")
        }

  }

  /** Returns a CiteProperty with the Label property
  * of a Cite collection
  * @param CiteUrn
  * @returns CiteProperty
  */
  String getCollectionLabel(CiteUrn urn)
  throws Exception {
    String labelString = ""
    CiteUrn collUrn = new CiteUrn(urn.reduceToCollection())
    String qs = QueryBuilder.getLabelForCollectionQuery(collUrn)
    String reply = sparql.getSparqlReply("application/json", qs)
    String tempUrnString = ""
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
    parsedReply.results.bindings.each{ b ->
        if (b.label){
          labelString = b.label.value
        } else {
          throw new Exception( "CcGraph.getCollectionLabelProp: ${urn.toString()}. Could not get label.")
        }
    }

    //CiteProperty labelProp = new CiteProperty("CollectionLabel",CitePropertyType.STRING,labelString)

    return labelString
  }

  /** Returns a CiteProperty with the OrderedBy property
  * of an ordered Cite collection. Exception if the collections
  * is not ordered
  * @param CiteUrn
  * @returns CiteProperty
  */
  CiteProperty getCollectionOrderedByProp(CiteUrn urn)
  throws Exception {
    CiteUrn collUrn = new CiteUrn(urn.reduceToCollection())

    String nameString
    String labelString

    if (isOrdered(collUrn)){
      String qs = QueryBuilder.getOrderedByPropQuery(collUrn)
      String reply = sparql.getSparqlReply("application/json", qs)
      String tempUrnString = ""
      JsonSlurper slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(reply)
      parsedReply.results.bindings.each{ b ->
        if (b.name){
          String ts1
          String ts2
          ts1 = parsedReply.results.bindings[0].name.value.toString()
          ts2 = ts1.substring(ts1.lastIndexOf("/") + 1)
          nameString = ts2.tokenize("_")[1]
        } else {
          throw new Exception( "CcGraph.getCollectionOrderedByProp: ${urn.toString()}. Could not get name.")
        }
        if (b.label){
          labelString = b.label.value
        } else {
          throw new Exception( "CcGraph.getOrderedByPropQuery: ${urn.toString()}. Could not get label.")
        }
      }
      CiteProperty orderedByProp = new CiteProperty(nameString,CitePropertyType.NUM,labelString)
      return orderedByProp
    } else {
          throw new Exception( "CcGraph.getCollectionOrderedByProp: ${urn.toString()} is not an ordered collection.")
    }
  }

  /** Returns an ArrayList of extensions to a Cite Collection.
  * May return an empty array
  * @param CiteUrn
  * @returns CiteProperty
  */
  ArrayList getCollectionExtensions(CiteUrn urn)
  throws Exception {
    def exts = []
    CiteUrn collUrn = new CiteUrn(urn.reduceToCollection())
    String qs = QueryBuilder.getExtensionsQuery(collUrn)
    String reply = sparql.getSparqlReply("application/json", qs)
    String tempUrnString = ""
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
    parsedReply.results.bindings.each{ b ->
      if(b.ext){
        exts << b.ext.value
      }
    }
    return exts
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

  /** Return an ArrayList of CiteProperty objects
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
    String ts1
    String ts2
    parsedReply.results.bindings.each{ pp ->
      // get just the property name
      ts1 = pp.property.value
      ts2 = ts1.substring(ts1.lastIndexOf("/") + 1)
      tempMap['property'] = ts2.tokenize("_")[1]
      tempMap['type'] = pp.type.value
      tempMap['label'] = pp.label.value
      System.err.println("${tempMap['property']} type ${tempMap['type']}")
      switch (tempMap['type']){
          case "http://www.homermultitext.org/cite/rdf/CiteUrn":
            tempMap['type'] = CitePropertyType.CITE_URN
            break
          case "http://www.homermultitext.org/cite/rdf/CtsUrn":
            tempMap['type'] = CitePropertyType.CTS_URN
            break
          case "string":
            tempMap['type'] = CitePropertyType.STRING
            break
          case "number":
            tempMap['type'] = CitePropertyType.NUM
            break
          case "boolean":
            tempMap['type'] = CitePropertyType.BOOLEAN
            break
          case "markdown":
            tempMap['type'] = CitePropertyType.MARKDOWN
            break
          default:
            break
          }

      collProps << new CiteProperty(tempMap['property'],tempMap['type'],tempMap['label'])
    }

    return collProps
  }

  /* Returns a CmeiteCollectionObject
  * @param CiteUrn
  * @returns CiteCollectionObject object
  */
  CiteCollectionObject getObject(CiteUrn urn){
    CiteUrn objUrn = resolveVersion(urn)
    CiteCollectionObject collectionObject

    // Make a CiteCollection object
    CiteCollection thisCollection = getCollection(objUrn)

    // Make a CiteCollectionObject object
    Map thisProperties = getPropertiesForObject(objUrn)
    // Add canonicalID property if it is (still) missing (see PrestoChango Issue #68)
    if( thisProperties["${thisCollection.canonicalIdProp.propertyName}"] == null){
      thisProperties["${thisCollection.canonicalIdProp.propertyName}"] = objUrn.toString()
    }
    //if ordered, we need prev and next URNs
    if (isOrdered(objUrn)){
      collectionObject = new CiteCollectionObject(objUrn,thisCollection,thisProperties,getPrevUrn(objUrn),getNextUrn(objUrn))
    } else {
      collectionObject = new CiteCollectionObject(objUrn,thisCollection,thisProperties)
    }

    return collectionObject
  }

  CCOSet getRange(CiteUrn urn){
    return null
  }

  CCOSet getPaged(CiteUrn urn, Integer offset, Integer limit){
    return null
  }

  /** Returns a Map of properties and their values for an object
  * in a CITE Collection
  * @param CiteUrn
  * @returns Map
  */
  Map getPropertiesForObject(CiteUrn urn){
    def propMap = [:]
    CiteUrn objUrn = resolveVersion(urn)
    ArrayList collProps = getPropertiesInCollection(objUrn)
    def verbsList = []
    def propNames = []
    String collectionName = objUrn.collection
    collProps.each { cp ->
      verbsList << "http://www.homermultitext.org/citedata/${collectionName}_${cp.propertyName}"
      propNames << cp.propertyName
    }
    String qs = QueryBuilder.getPropertiesForObjectQuery(objUrn,verbsList,propNames )
    //System.err.println(qs)
    String reply = sparql.getSparqlReply("application/json", qs)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
    //System.err.println(parsedReply)
    parsedReply.results.bindings[0].each{ bb ->
        //System.err.println("Key=${bb.key}")
        propMap["${bb.key}"] = bb.value.value
        //System.err.println("Value=${bb.value.value} is class ${bb.value.value.getClass()}")
    }
    return propMap

  }

  String formatReply(String request, Map, params) {
    return ""
  }


}
