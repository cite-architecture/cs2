package edu.holycross.shot.sparqlcc

//import edu.holycross.shot.citeservlet.Sparql

import edu.harvard.chs.cite.Cite2Urn
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
				System.err.println("collUrn: ${collUrn}")
				ArrayList vfc = versionsForCollection(collUrn)
				System.err.println("versions of coll: ${vfc}")
				if (vfc.size() > 0){
					Cite2Urn testUrn
					vfc.each{ curn ->
							testUrn = new Cite2Urn("${curn}${urn.objectId_1}-${urn.objectId_2}")
							System.err.println("Testing: ${testUrn}")
							if (objectExists(testUrn)){
									System.err.println("found it!")
									returnUrn = testUrn
							} else {
								System.err.println("did not find it.")
							}
					}

				} else {
					throw new Exception("CcGraph, resolveVersion: No versions of collection ${urn.reduceToCollection()} exist in data.")
				}

			} else {
				ArrayList vfo = versionsForObject(urn)
				System.err.println(vfo)
				if (vfo.size() > 0){
					System.err.println(vfo[0].toString())
					returnUrn = vfo[0]
				} else {
					System.err.println(e)
					throw new Exception("CcGraph, resolveVersion: No versions of collection ${urn.reduceToCollection()} contain an object identified as ${urn}")
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
					System.err.println(bndng.cv?.value)
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
			System.err.println(collVersions)
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
				throw new Exception("CcGraph, objectExists: ${urn} does not identify an object.")
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


  /** returns 'true' if an object identified by a cite urn is a member of an ordered collection.
  * @param Cite2Urn
  * @returns boolean
  */
  boolean isordered(Cite2Urn urn)
  throws Exception {
      String collectionUrnStr = urn.reduceToCollection()
      Cite2Urn collectionUrn = new Cite2Urn(collectionUrnStr)
      String qs = QueryBuilder.isOrderedQuery(collectionUrn)
      String reply = sparql.getSparqlReply("application/json", qs)
      JsonSlurper slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(reply)
      //System.err.println qs
      //System.err.println parsedReply
      //System.err.println "${urn} >> ${collectionUrn}"
      return parsedReply.boolean == true
  }


  /** Returns the Cite2Urn for the previous item in an ordered collection
  * @param Cite2Urn
  * @returns Map ['resolvedUrn':Cite2Urn, 'prevUrn':Cite2Urn]
  */
  Map getPrevUrn(Cite2Urn urn){
    Cite2Urn rurn
    def replyMap = [:]
    String tempUrnString

    // Only if ordered
    if ( !(isOrdered(urn))){
      throw new Exception( "CcGraph.getPrevUrn: ${urn.toString()} must be from an ordered collection.")
    }

    // If a range…
    if ( urn.isRange() ){
      rurn = resolveVersion(new Cite2Urn(urn.getRangeBegin()))
    } else {
      rurn = resolveVersion(urn)
    }

      String qs = QueryBuilder.getPreviousQuery(rurn)
      String reply = sparql.getSparqlReply("application/json", qs)
      JsonSlurper slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(reply)
        parsedReply.results.bindings.each { bndng ->
          if (bndng.u) {
            tempUrnString = bndng.u?.value
          }

        }

      if (tempUrnString != null){
        if (tempUrnString.contains("urn:cite:")){
          replyMap['resolvedUrn'] = rurn
          replyMap['prevUrn'] = new Cite2Urn(tempUrnString)
          return replyMap
        }
      } else {
          replyMap['resolvedUrn'] = rurn
          replyMap['prevUrn'] = null
          return replyMap
 }
  }

  /** Returns the Cite2Urn for the Next item in an ordered collection
  * @param Cite2Urn
  * @returns Map ['resolvedUrn':Cite2Urn, 'nextUrn':Cite2Urn]
  */
  Map getNextUrn(Cite2Urn urn){
    Cite2Urn rurn
    String tempUrnString
    def replyMap = [:]

    // Only if ordered
    if ( !(isOrdered(urn))){
      throw new Exception( "CcGraph.getNextUrn: ${urn.toString()} must be from an ordered collection.")
    }

    // If a range…
    if ( urn.isRange() ){
      rurn = resolveVersion(new Cite2Urn(urn.getRangeEnd()))
    } else {
      rurn = resolveVersion(urn)
    }

      String qs = QueryBuilder.getNextQuery(rurn)
      String reply = sparql.getSparqlReply("application/json", qs)
      JsonSlurper slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(reply)
        parsedReply.results.bindings.each { bndng ->
          if (bndng.u) {
            tempUrnString = bndng.u?.value
          }
        }

      if (tempUrnString != null){
        if (tempUrnString.contains("urn:cite:")){
          replyMap['resolvedUrn'] = rurn
          replyMap['nextUrn'] = new Cite2Urn(tempUrnString)
          return replyMap
        }
      } else {
          replyMap['resolvedUrn'] = rurn
          replyMap['nextUrn'] = null
          return replyMap
      }
  }

  /** Returns all versions present for a given object. That is to say
	* Looks in every version of a collection to find matching object-level URNs.
  * @param Cite2Urn
  * @returns ArrayList of Cite2Urns
  */
  ArrayList getVersionsOfObject(Cite2Urn urn)
  throws Exception {
    ArrayList replyArray = []
    String tempUrnString
    Cite2Urn queryUrn
    if ( urn.isRange() ){
      String rangeBeginString = urn.getRangeBegin()
      Cite2Urn rangeBeginUrn = new Cite2Urn(rangeBeginString)
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
          queryUrn = new Cite2Urn(tempUrnString)
          String qs = QueryBuilder.getVersionsOfObjectQuery(queryUrn)
          String reply = sparql.getSparqlReply("application/json", qs)
          JsonSlurper slurper = new groovy.json.JsonSlurper()
          def parsedReply = slurper.parseText(reply)
          parsedReply.results.bindings.each { bndng ->
            if (bndng.v) {
              try {
                replyArray << new Cite2Urn( bndng.v?.value )
                } catch (Exception e ) {
                  throw new Exception ( "CcGraph.getVersionsOfObject: Bad data ( ${bndng.v?.value} )")
                }
              }
            }
            return replyArray
          }


  /** Returns the Cite2Urn for the first object in an ordered collection.
  * @param Cite2Urn
  * @returns Map ['resolvedUrn':Cite2Urn, 'firstUrn':Cite2Urn]
  */
  Map getFirstUrn(Cite2Urn urn)
  throws Exception {
    // Only if ordered
    if ( !(isOrdered(urn))){
      throw new Exception( "CcGraph.getFirstUrn: ${urn.toString()} must be from an ordered collection.")
      } else {
        def replyMap = [:]
        Cite2Urn collUrn = new Cite2Urn(urn.reduceToCollection())
        String qs = QueryBuilder.getFirstQuery(collUrn)
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
          replyMap['resolvedUrn'] = urn
          replyMap['firstUrn'] = new Cite2Urn(tempUrnString)
          return replyMap
        }
      }

  }


  /** Returns the Cite2Urn for the last object in an ordered collection.
  * @param Cite2Urn
  * @returns Map ['resolvedUrn':Cite2Urn, 'lastUrn':Cite2Urn]
  */
  Map getLastUrn(Cite2Urn urn){
    // Only if ordered
    if ( !(isOrdered(urn))){
      throw new Exception( "CcGraph.getLastUrn: ${urn.toString()} must be from an ordered collection.")
      } else {
        def replyMap = [:]
        Cite2Urn collUrn = new Cite2Urn(urn.reduceToCollection())
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
          throw new Exception( "CcGraph.getLastUrn: ${urn.toString()}. No valid lastUrn for collection ${collUrn.toString()}")
        } else {
          replyMap['resolvedUrn'] = urn
          replyMap['lastUrn'] = new Cite2Urn(tempUrnString)
          return replyMap
        }
      }
  }

  /**
  * Returns a count of objects in a collection, based on a Cite2Urn.
  * With a collection-level URN, returns a count of all versions of all
  * objects. With a version-level URN, counts only objects with the same
  * version-string.
  * @param Cite2Urn
  * @returns Map ['urn':Cite2Urn, 'version':String, 'size': BigInteger]
  */
    Map getCollectionSize(Cite2Urn urn){
    String versionString = null
    Cite2Urn qUrn
    if(urn.hasCollectionVersion()){
       versionString = urn.objectVersion
    }
    qUrn = new Cite2Urn(urn.reduceToCollection())

    return getCollectionSize(qUrn,versionString)
  }

  Map getCollectionSize(Cite2Urn urn, String versionString)
  throws Exception {
    Cite2Urn qUrn
    String qVersion
    String qs
    if(urn.hasCollectionVersion()){
      if( (versionString == "") || (versionString == null)){
        qVersion = urn.objectVersion
        qUrn = new Cite2Urn(urn.reduceToCollection())
      }
      } else {
        qVersion = versionString
        qUrn = new Cite2Urn(urn.reduceToCollection())
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
          Map returnMap = [:]
          returnMap['resolvedUrn'] = qUrn
          returnMap['version'] = qVersion
          returnMap['size'] = new BigInteger(parsedReply.results.bindings[0].size.value)
          return returnMap
          } else {
            throw new Exception( "CcGraph.getCollectionSize: ${urn.toString()}. Failed to get count.")
          }
        }


  /** Returns all valid Cite2Urns in a collection.
  * If the input parameter Cite2Urn has a version-id, returns
  * only URNs with the same version-id. If the param is a range,
  * returns all URNs contained in that range (for ordered collections),
  * or the first- and last- elements of the range-urn for unordered collections.
  * For queries on notional
  * @param Cite2Urn
  * @returns Map ['resolvedUrn': Cite2Urn, 'versionString': String, 'urns': ArrayList of URN-Strings]
  */
  Map getValidReff(Cite2Urn urn){
    // Is it an object or a range?
    if ( urn.isRange() || urn.hasObjectId() ){
      //System.err.println("${urn} is range; no version")
      return gvrForRange(urn)
    } else {
      //System.err.println("${urn} is NOT range; no version")
      return gvrForCollection(urn)
    }
  }

  Map getValidReff(Cite2Urn urn, String versionString)
  throws Exception {
      //System.err.println("${urn} has version = ${versionString}.")
    if ((versionString == "") || (versionString == null)){
      return getValidReff(urn)
    } else {
      Cite2Urn qUrn = new Cite2Urn(urn.reduceToCollection())
      return gvrForCollection(qUrn,versionString)
    }
  }

  Map gvrForCollection(Cite2Urn urn, String versionString)
  throws Exception {
    def replyMap = [:]
    ArrayList replyArray = []
    String qs
    String qUrnString = urn.reduceToCollection()
    Cite2Urn qUrn = new Cite2Urn(qUrnString)
    replyMap['resolvedUrn'] = qUrn

    if ( (versionString =="") || ( versionString == null)){
      replyMap['versionString'] = ""
      if (isOrdered(qUrn)){
        qs = QueryBuilder.getGVROrderedCollectionQuery(qUrn)
      } else {
          qs = QueryBuilder.getGVRCollectionQuery(qUrn)
      }
    } else {
        replyMap['versionString'] = versionString
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
      replyMap['urns'] = replyArray
      return replyMap
    }

  Map gvrForCollection(Cite2Urn urn)
  throws Exception {
    return gvrForCollection(urn, null)
  }

  Map gvrForRange(Cite2Urn urn)
  throws Exception {
    def replyMap = [:]
    ArrayList replyArray = []
    replyMap['versionString'] = ""

    // is not range
    if (!urn.isRange()){
        replyMap['resolvedUrn'] = urn
        if(urn.hasCollectionVersion()){
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
        replyMap['resolvedUrn'] = resolveVersion(urn)
        Cite2Urn rStartUrn = resolveVersion(new Cite2Urn(rStart))
        Cite2Urn rEndUrn = resolveVersion(new Cite2Urn(rEnd))
        String vs = rStartUrn.objectVersion
        // replyArray = ["for range; ordered collection"]
        ArrayList tempArray = gvrForCollection(rStartUrn, vs)['urns']


        def startIndex = tempArray.findIndexOf { s -> s.toString() == rStartUrn.toString() }
        def endIndex = tempArray.findIndexOf { e -> e.toString() == rEndUrn.toString() }

        if (startIndex > endIndex){
          throw new Exception( "CcGraph.getValidReffForRange: ${urn.toString()}. This is an ordered collection, but the start-URN follows the end-URN.")
        }

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
         Cite2Urn rangeStart = new Cite2Urn(urn.getRangeBegin())
         Cite2Urn rangeEnd = new Cite2Urn(urn.getRangeEnd())
        if (rangeStart.hasCollectionVersion()){
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
    replyMap['urns'] = replyArray
    return replyMap
  }


  /* Returns a CiteCollection
  * @param Cite2Urn
  * @returns CiteCollection object
  */
  CiteCollection getCollection(Cite2Urn urn){
    String tempUrn = urn.reduceToCollection()


  	Cite2Urn collUrn = new Cite2Urn(tempUrn)

  	CiteProperty idProp = getCollectionIdProp(collUrn)
    CiteProperty labelProp = getCollectionLabelProp(collUrn)
    String collectionLabel = labelProp.label
    CiteProperty orderedByProp = null
    if (isOrdered(collUrn)){
    	orderedByProp = getCollectionOrderedByProp(collUrn)
    }
    ArrayList collProps = getPropertiesInCollection(collUrn)

  	ArrayList extensions = getCollectionExtensions(collUrn)

  	Map nss = getCollectionNamespace(collUrn)
  	String nsAbbr = nss['abbr']
  	String nsFull = nss['full']

    try {
    CiteCollection cc = new CiteCollection(collUrn, collectionLabel, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

    return cc
    } catch (Exception e) {
      throw new Exception( "CcGraph.getCollection: ${urn.toString()}. Could not create collection. ${e}")
    }
  }

  /** Returns a CiteProperty with the Canonical Id property
  * of a Cite collection
  * @param Cite2Urn
  * @returns CiteProperty
  */
  CiteProperty getCollectionIdProp(Cite2Urn urn)
  throws Exception {

        Cite2Urn collUrn = new Cite2Urn(urn.reduceToCollection())

        String qs = QueryBuilder.getCanonicalIdPropQuery(collUrn)
        String reply = sparql.getSparqlReply("application/json", qs)
        String tempUrnString = ""
        JsonSlurper slurper = new groovy.json.JsonSlurper()
        def parsedReply = slurper.parseText(reply)

        String pName
        String pLabel
        CitePropertyType pType

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
            CiteProperty canonicalId = new CiteProperty(pName,pType,pLabel)
            return canonicalId
        } catch (Exception e) {
          throw new Exception( "CcGraph.getCollectionIdProp: ${urn.toString()}. Could not make property out of name = ${pName}, type = ${pType}, label = ${pLabel}.")
        }

  }

  /** Returns a CiteProperty with the Label property
  * of a Cite collection
  * @param Cite2Urn
  * @returns CiteProperty
  */
  CiteProperty getCollectionLabelProp(Cite2Urn urn)
  throws Exception {
    Cite2Urn collUrn = new Cite2Urn(urn.reduceToCollection())

    String qs = QueryBuilder.getCollectionLabelPropQuery(collUrn)
    String reply = sparql.getSparqlReply("application/json", qs)
    String tempUrnString = ""
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)

    String pName
    String pLabel
    CitePropertyType pType

    if (parsedReply.results.bindings[0].name){
      String ts1
      String ts2
      ts1 = parsedReply.results.bindings[0].name.value.toString()
      ts2 = ts1.substring(ts1.lastIndexOf("/") + 1)
      pName = ts2.tokenize("_")[1]
    } else {
      throw new Exception( "CcGraph.getCollectionLabelProp: ${urn.toString()}. Failed to get property name.")
    }
    if (parsedReply.results.bindings[0].type){
      pType = CitePropertyType.STRING // it has to be
    } else {
      throw new Exception( "CcGraph.getCollectionLabelProp: ${urn.toString()}. Failed to get property type.")
    }
    if (parsedReply.results.bindings[0].label){
      pLabel = parsedReply.results.bindings[0].label.value
    } else {
      throw new Exception( "CcGraph.getCollectionLabelProp: ${urn.toString()}. Failed to get property label.")
    }
    try {
        CiteProperty canonicalId = new CiteProperty(pName,pType,pLabel)
        return canonicalId
    } catch (Exception e) {
      throw new Exception( "CcGraph.getCollectionLabelProp: ${urn.toString()}. Could not make property out of name = ${pName}, type = ${pType}, label = ${pLabel}.")
    }

}


  /** Returns a CiteProperty with the OrderedBy property
  * of an ordered Cite collection. Exception if the collections
  * is not ordered
  * @param Cite2Urn
  * @returns CiteProperty
  */
  CiteProperty getCollectionOrderedByProp(Cite2Urn urn)
  throws Exception {
    Cite2Urn collUrn = new Cite2Urn(urn.reduceToCollection())

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
  * @param Cite2Urn
  * @returns CiteProperty
  */
  ArrayList getCollectionExtensions(Cite2Urn urn)
  throws Exception {
    def exts = []
    Cite2Urn collUrn = new Cite2Urn(urn.reduceToCollection())
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
  * @param Cite2Urn
  * @returns Map "full:" and "abbr:"
  */
  Map getCollectionNamespace(Cite2Urn urn)
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
  * @param Cite2Urn
  * @returns ArrayList of Map
  */
  ArrayList getPropertiesInCollection(Cite2Urn urn){
      Cite2Urn collUrn = new Cite2Urn(urn.reduceToCollection())
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
      CitePropertyType thisType
      switch (tempMap['type']){
          case "http://www.homermultitext.org/cite/rdf/Cite2Urn":
            thisType = CitePropertyType.CITE_URN
            break
          case "http://www.homermultitext.org/cite/rdf/CtsUrn":
            thisType = CitePropertyType.CTS_URN
            break
          case "http://www.homermultitext.org/cite/rdf/String":
            thisType = CitePropertyType.STRING
            break
          case "http://www.homermultitext.org/cite/rdf/Numeric":
            thisType = CitePropertyType.NUM
            break
          case "http://www.homermultitext.org/cite/rdf/Boolean":
            thisType = CitePropertyType.BOOLEAN
            break
          case "http://www.homermultitext.org/cite/rdf/Markdown":
            thisType = CitePropertyType.MARKDOWN
            break
          default:
            break
          }

      collProps << new CiteProperty(tempMap['property'],thisType,tempMap['label'])
    }

    return collProps
  }

  /* Returns a CmeiteCollectionObject
  * @param Cite2Urn
  * @returns CiteCollectionObject object
  */
  CiteCollectionObject getObject(Cite2Urn urn)
  throws Exception {
    if (urn.isRange()){
         throw new Exception( "CcGraph.getObject: ${urn.toString()}. Cannot construct a Cite Colletion Object from a range-urn.")
    }
    Cite2Urn objUrn = resolveVersion(urn)
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
      collectionObject = new CiteCollectionObject(objUrn,thisCollection,thisProperties,getPrevUrn(objUrn)['prevUrn'],getNextUrn(objUrn)['nextUrn'])
    } else {

      collectionObject = new CiteCollectionObject(objUrn,thisCollection,thisProperties)
    }

    return collectionObject
  }


  /** Returns a Map of properties and their values for an object
  * in a CITE Collection
  * @param Cite2Urn
  * @returns Map
  */
  Map getPropertiesForObject(Cite2Urn urn){
    def propMap = [:]
    Cite2Urn objUrn = resolveVersion(urn)
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

  /** Given a Cite2Urn, returns a CCOSet object, containing a
  * Collection-URN and an Array of CiteCollectionObjects
  * @param Cite2Urn
  * @returns ArrayList of CiteCollectionObjects
  */
  ArrayList getRange(Cite2Urn paramUrn){
    Cite2Urn urn
    if (paramUrn.hasObjectId()){
      urn = resolveVersion(paramUrn)
    } else {
      urn = paramUrn
    }
    ArrayList validReff = getValidReff(urn)['urns']
    def objects = []
    validReff.each{ vr ->
      objects << getObject(new Cite2Urn(vr))
    }
    return objects

}

/**Given a Cite2Urn, an offset, and a limit, returns a Map, containing a
* Collection-URN and an Array of Cite2Urns. The urn of the CCOSet
* will reflect the data actually returned.
* @param Cite2Urn
* @returns Map ['urn':Cite2Urn, 'offset':integer, 'limit':integer, 'size':BigInteger 'objects':ArrayList]
*/
Map getPagedReff(Cite2Urn paramUrn, Integer offset, Integer limit){
  return getPagedReff(paramUrn, offset, limit, "")
}

/**Given a Cite2Urn, an offset, a limit, and a version-string, returns a Map, containing a
* Collection-URN and an Array of Cite2Urns. The urn of the CCOSet
* will reflect the data actually returned.
* @param Cite2Urn
* @returns Map ['urn':Cite2Urn, 'offset':integer, 'limit':integer, 'size':BigInteger 'objects':ArrayList]
*/
Map getPagedReff(Cite2Urn paramUrn, Integer offset, Integer limit, String versionString)
throws Exception {

  if ( (offset < 1) || (limit < 1)){
         throw new Exception( "CcGraph.getPagedReff: ${paramUrn.toString()}. Neither parameter 'offset' nor 'limit' may be less than 1. offset = ${offset}; limit=${limit}")
  }

  //System.err.println("-----------------")
  //System.err.println("${paramUrn}")
  def pagedReff = [:]
  Cite2Urn urn
  if (paramUrn.hasObjectId()){
    urn = resolveVersion(urn)
    } else {
      urn = paramUrn
    }
  //  System.err.println("${urn}")
    pagedReff['resolvedUrn'] = urn
    pagedReff['offset'] = offset
    pagedReff['limit'] = limit
    pagedReff['versionString'] = versionString

    def urns = [] // will hold the CiteCollectionObjects

    ArrayList firstArray = getValidReff(urn,versionString)['urns']
    pagedReff['size'] = firstArray.size()

    // let's get absolute values for start and end
    def startIndex = offset - 1
    def endIndex = startIndex + limit - 1

  //  System.err.println("0. From ${startIndex} to ${endIndex}, out of ${firstArray.size()}")

    if (offset > firstArray.size()){
      pagedReff['urns'] = urns // that is, an empty array

      //System.err.println("Offset too big. From ${startIndex} to ${endIndex}, out of ${firstArray.size()}")

    } else {
      if ( endIndex > firstArray.size()){


        endIndex = firstArray.size() - 1
        pagedReff['limit'] = firstArray.size() - offset + 1

  //      System.err.println("endIndex too big. From ${startIndex} to ${endIndex}, out of ${firstArray.size()}")
      }
      for (i in (startIndex..endIndex) ){
  //      System.err.println("[${i}] ${firstArray[i]}")
        urns << new Cite2Urn(firstArray[i])
      }
      pagedReff['urns'] = urns
    }

    return pagedReff
  }

/**Given a Cite2Urn, an offset, and a limit, returns a Map, containing a
* a resolvedUrn, an offset, a limit, and an array of CiteCollectionObjects. The resolvedUrn will reflect the data actually returned.
* @param Cite2Urn
* @returns Map ['urn':Cite2Urn, 'offset':integer, 'limit':integer, 'size':BigInteger 'objects':ArrayList]
*/
Map getPaged(Cite2Urn paramUrn, Integer offset, Integer limit)
throws Exception {
  Map pagedReff = getPagedReff(paramUrn, offset, limit)
  Map pagedObjects = [:]
  def citeObjects = []
  pagedObjects['resolvedUrn'] = pagedReff['resolvedUrn']
  pagedObjects['offset'] = pagedReff['offset']
  pagedObjects['limit'] = pagedReff['limit']
  pagedObjects['size'] = pagedReff['size']
  pagedReff['urns'].each { uu ->
    citeObjects << getObject(uu)

  }
  pagedObjects['objects'] = citeObjects
  return pagedObjects

}


/* Requests

GetObject
GetObjectPlus
GetPrevNextUrn
GetNext
GetPrev
GetCollectionSize
GetValidReff
GetLast
GetFirst
GetRange
GetPaged
*/

/** Takes a CITE Collection Service request and returns
* a properly formatted reply, as a string.
* @param String request
* @param Map params
* @returns String
*/
String formatXmlReply(String request, Cite2Urn requestUrn, Map params )
throws Exception {
  try {

    String replyString = ""

    replyString += """<${request} xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite" >\n\n<cite:request>\n"""

    params.each{  pp ->
  //  System.err.println("${pp.key} ${pp.value}")
  }

    params.each { pm ->
      if (pm.key == "urn") {
        replyString += "<requestUrn>${pm.value}</requestUrn>\n"
      } else if (pm.key == "request"){
        replyString += "<request>${request}</request>\n"
      } else {
        replyString+= "<${pm.key}>${pm.value}</${pm.key}>\n"
      }
        //replyString+= "<${pm.key}>${pm.value}</${pm.key}>\n"
      //System.err.println("Param: ${pm.key} : ${pm.value}")
    }
    // We don't close <cite:request> yet, so we can add request-specific stuff below.

    switch(request) {
// GetObject -----------------------------------
      case "GetObject":
        CiteCollectionObject cco = getObject(requestUrn)
        replyString += "<resolvedUrn>${cco.urn}</resolvedUrn>\n"
        replyString += "</cite:request>\n<cite:reply>\n"
        replyString += xmlFormatObject(cco)
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"
      break;

// GetObjectPlus -----------------------------------
      case "GetObjectPlus":
        CiteCollectionObject cco = getObject(requestUrn)
        replyString += "<resolvedUrn>${cco.urn}</resolvedUrn>\n"
        replyString += "</cite:request>\n<cite:reply>\n"
        replyString += xmlFormatObject(cco)
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"
      break;

// GetPrevNextUrn -----------------------------------
      case "GetPrevNextUrn":
        Map getPrevMap = getPrevUrn(requestUrn)
        Map getNextMap = getNextUrn(requestUrn)
        replyString += "<resolvedUrn>${getNextMap['resolvedUrn']}</resolvedUrn>\n"
        replyString += "</cite:request>\n<cite:reply>\n"
        replyString += "<prevUrn>${getPrevMap['prevUrn']}</prevUrn>\n"
        replyString += "<nextUrn>${getNextMap['nextUrn']}</nextUrn>\n"
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"
      break;

// GetNextUrn -----------------------------------
      case "GetNextUrn":
        Map getNextMap = getNextUrn(requestUrn)
        replyString += "<resolvedUrn>${getNextMap['resolvedUrn']}</resolvedUrn>\n"
        replyString += "</cite:request>\n<cite:reply>\n"
        replyString += "<nextUrn>${getNextMap['nextUrn']}</nextUrn>\n"
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"
      break;

// GetPrevUrn -----------------------------------
      case "GetPrevUrn":
        Map getPrevMap = getPrevUrn(requestUrn)
        replyString += "<resolvedUrn>${getPrevMap['resolvedUrn']}</resolvedUrn>\n"
        replyString += "</cite:request>\n<cite:reply>\n"
        replyString += "<prevUrn>${getPrevMap['prevUrn']}</prevUrn>\n"
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"
      break;

// GetCollectionSize -----------------------------------
      case "GetCollectionSize":
        Map getCollectionSizeMap
        if (params['version']){
          getCollectionSizeMap = getCollectionSize(requestUrn,params['version'])
        } else {
          getCollectionSizeMap = getCollectionSize(requestUrn)
        }
        replyString += "<resolvedUrn>${getCollectionSizeMap['resolvedUrn']}</resolvedUrn>\n"
        replyString += "</cite:request>\n<cite:reply>\n"
        replyString += "<count>${getCollectionSizeMap['size']}</count>\n"
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"
      break;

// GetValidReff -----------------------------------
      case "GetValidReff":

        // Lets assemble the params we need, all at once
        String gvrVersionString
        Boolean gvrSafeMode

        if (params['version']){
          if (params['version'] == null){
              gvrVersionString = ""
          } else if (params['version'] == "" ){
              gvrVersionString = ""
          } else {
            gvrVersionString = params['version']
          }
        }
        if (params['safemode']){
          if (params['safemode'] == "on"){
            gvrSafeMode = true
          } else {
            gvrSafeMode = false
          }
        } else {
            gvrSafeMode = false
        }


      Map gvr

     gvr = getValidReff(requestUrn,gvrVersionString)

      if(gvrSafeMode == true ){

          // Check to see if there are more than 50 urns
          if ( gvr['urns'].size() > 50 ){
            // We're sending this off to getPagedValidReff
            // so we need to make a new set of params
            String pgvrOffset = "1"
            String pgvrLimit = "50"
            def pgvrParams = [:]
            pgvrParams['offset'] = pgvrOffset
            pgvrParams['limit'] = pgvrLimit
            pgvrParams['request'] = "GetPagedValidReff"
            pgvrParams['urn'] = requestUrn.toString()

            replyString = formatXmlReply("GetPagedValidReff", requestUrn, pgvrParams)
            break;
          }
      }

      replyString += "<resolvedUrn>${gvr['resolvedUrn']}</resolvedUrn>\n"
      if ( params['version']){
        if ( (params['version'] == null) || (params['version'] == "")){
          replyString += "<version>${gvr['versionString']}</version>\n"
        }
      }
      replyString += "</cite:request>\n<cite:reply>\n"
      gvr['urns'].each { uu ->
        replyString += "<urn>${uu.toString()}</urn>\n"
      }
      replyString += "</cite:reply>\n"
      replyString += "</${request}>"


      break;

// GetPagedValidReff -----------------------------------
      case "GetPagedValidReff":

        // Sort out params
        Integer limit
        Integer offset
        String versionString
        if (params['limit']){
          limit = params['limit'].toInteger()
        } else {
          throw new Exception("SparqlCC: GetPagedValidReff. Missing paramter 'limit'.")
        }
        if (params['offset']){
          offset = params['offset'].toInteger()
        } else {
          throw new Exception("SparqlCC: GetPagedValidReff. Missing paramter 'offset'.")
        }
        if (params['version']){
          if (params['version'] != null){
            versionString = params['version']
          } else {
            versionString = ""
          }
        } else {
          versionString = ""
        }

        Map pagedGvr = getPagedReff(requestUrn, offset, limit, versionString)

        Map pagedNav = calculatePagedNavigation(new BigInteger(pagedGvr['size']),offset,limit)


        replyString += "<resolvedUrn>${pagedGvr['resolvedUrn']}</resolvedUrn>\n"
        replyString += "<count>${pagedGvr['size']}</count>\n"

        replyString += "<prevOffset>${pagedNav['prevOffset']}</prevOffset>\n"
        replyString += "<prevLimit>${pagedNav['prevLimit']}</prevLimit>\n"
        replyString += "<nextOffset>${pagedNav['nextOffset']}</nextOffset>\n"
        replyString += "<nextLimit>${pagedNav['nextLimit']}</nextLimit>\n"

        replyString += "</cite:request>\n<cite:reply>\n"
        pagedGvr['urns'].each{ uu ->
          replyString += "<urn>${uu.toString()}</urn>\n"
        }
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"

      break;

// GetLast -----------------------------------
      case "GetLastUrn":
        Map getLastMap = getLastUrn(requestUrn)
        replyString += "<resolvedUrn>${getLastMap['resolvedUrn']}</resolvedUrn>\n"
        replyString += "</cite:request>\n<cite:reply>\n"
        replyString += "<lastUrn>${getLastMap['lastUrn']}</lastUrn>\n"
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"

      break;

// GetFirst -----------------------------------
      case "GetFirstUrn":
        Map getFirstMap = getFirstUrn(requestUrn)
        replyString += "<resolvedUrn>${getFirstMap['resolvedUrn']}</resolvedUrn>\n"
        replyString += "</cite:request>\n<cite:reply>\n"
        replyString += "<firstUrn>${getFirstMap['firstUrn']}</firstUrn>\n"
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"
      break;

// GetVersionsOfObject -----------------------------------
      case "GetVersionsOfObject":
        ArrayList urns = getVersionsOfObject(requestUrn)
        replyString += "<resolvedUrn>${requestUrn}</resolvedUrn>\n"
        replyString += "</cite:request>\n<cite:reply>\n"
        replyString += "<versions>\n"
        urns.each{ uu ->
          replyString += "<version>${uu}</version>\n"
        }
        replyString += "</versions>\n"
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"

      break;

// GetRange -----------------------------------
      case "GetRange":
        ArrayList ccos = getRange(requestUrn)
        Cite2Urn rurn = resolveVersion(requestUrn)
        Boolean grSafeMode

        if (params['safemode']){
          if (params['safemode'] == "on"){
            grSafeMode = true
          } else {
            grSafeMode = false
          }
        } else {
            grSafeMode = false
        }

        if(grSafeMode == true ){
            Map gvr
            gvr = getValidReff(requestUrn)

            // Check to see if there are more than 50 urns
            if ( gvr['urns'].size() > 20 ){
              // We're sending this off to getPaged
              // so we need to make a new set of params
              String pgvrOffset = "1"
              String pgvrLimit = "20"
              def pgvrParams = [:]
              pgvrParams['offset'] = pgvrOffset
              pgvrParams['limit'] = pgvrLimit
              pgvrParams['request'] = "GetPaged"
              pgvrParams['urn'] = requestUrn.toString()

              replyString = formatXmlReply("GetPaged", requestUrn, pgvrParams)
              break;
            }
        }


        replyString += "<resolvedUrn>${rurn}</resolvedUrn>\n"
        replyString += "</cite:request>\n<cite:reply>\n"
        replyString += "<citeObjects>\n"
        ccos.each { cco ->
          replyString += xmlFormatObject(cco)
        }
        replyString += "</citeObjects>\n"
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"
      break;

// GetPaged -----------------------------------
      case "GetPaged":
        // Sort out params
        Integer limit
        Integer offset
        if (params['limit']){
          limit = params['limit'].toInteger()
        } else {
          throw new Exception("SparqlCC: GetPaged. Missing paramter 'limit'.")
        }
        if (params['offset']){
          offset = params['offset'].toInteger()
        } else {
          throw new Exception("SparqlCC: GetPaged. Missing paramter 'offset'.")
        }

        Map ccos = getPaged(requestUrn, offset, limit)
        Map pagedNav = calculatePagedNavigation(new BigInteger(ccos['size']),offset,limit)

        replyString += "<resolvedUrn>${requestUrn}</resolvedUrn>\n"
        replyString += "<count>${ccos['size']}</count>\n"
        replyString += "<prevOffset>${pagedNav['prevOffset']}</prevOffset>\n"
        replyString += "<prevLimit>${pagedNav['prevLimit']}</prevLimit>\n"
        replyString += "<nextOffset>${pagedNav['nextOffset']}</nextOffset>\n"
        replyString += "<nextLimit>${pagedNav['nextLimit']}</nextLimit>\n"
        replyString += "</cite:request>\n<cite:reply>\n"
        replyString += "<citeObjects>\n"
        ccos['objects'].each { cco ->
          replyString += xmlFormatObject(cco)
        }
        replyString += "</citeObjects>\n"
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"
      break;

// Default -----------------------------------
      default:
        throw new Exception("${request} is not a recognized request.")
      break;
    }

    return replyString

  } catch (Exception e){
    String replyString = """ <CITEError xmlns:cts="http:chs.harvard.edu/xmlns/cite" xmlns="http://chs.harvard.edu/xmlns/cite">
				<message>INVALID REQUEST. Request: ${request}, with parameters ${params}</message>
				<error>${e}</error>
				</CITEError> """
    return replyString
  }

}

/** Formats a CiteCollectionObject into an  XML replyString
* @param CiteCollectionObject
* @returns String
*/

String xmlFormatObject(CiteCollectionObject cco){
  String replyString = ""
  replyString += """<citeObject urn="${cco.urn.toString()}">\n"""
  cco.objectProperties.each{ op ->
    String propType = ""
    replyString += """<citeProperty name="${op['key']}" label="${cco.collection.propertyForName(op['key']).label}" type="${xmlTranslatePropertyType("${cco.collection.getPropertyType(op['key'])}")}">"""
    replyString += """${op['value']}</citeProperty>\n"""
  }

  // Prev and Next, if present
  if (cco.prevUrn != null){
      replyString += "<prevUrn>${cco.prevUrn}</prevUrn>\n"
  }
  if (cco.nextUrn != null){
      replyString += "<nextUrn>${cco.nextUrn}</nextUrn>\n"
  }

  // Collection extensions
  if ( (cco.collection.extendedBy != null) && (cco.collection.extendedBy.size() > 0) ){
      cco.collection.extendedBy.each{ ext ->
        replyString += "<extension>${ext}</extension>\n"
    }

  }
  replyString += "</citeObject>\n"
  return replyString
}

/** Translates the CitePropertyType Strings as received
* from RDF into the values expected by the CITE Collection inventory
* schema
* @param typeString
* @returns String
*/

String xmlTranslatePropertyType(String typeString){
  String replyString = ""

  switch (typeString){

    case "NUM":
      replyString = "number"
      break;
    case "BOOLEAN" :
      replyString = "boolean"
      break;
    case "STRING" :
      replyString = "string"
      break;
    case "MARKDOWN" :
      replyString = "markdown"
      break;
    case "CITE_URN" :
      replyString = "Cite2Urn"
      break;
    case "CTS_URN" :
      replyString = "ctsurn"
      break;
  }
  return replyString
}

/** Given an offset, a limit, and a collection-size, returns a Map
* giving appropriate values for previous- and next- paged requests.
* If either request would exceed the bounds of the collection, limit and
* offset are returned as 0.
* @param BigInteger size
* @param BigInteger offset
* @param Integer limit
* @returs Map ['prevOffset','prevLimit','nextOffset','nextLimit']
*/

Map calculatePagedNavigation(BigInteger size, BigInteger offset, Integer limit){
  def returnMap = [:]

  // previous
  if ( offset == 1 ){
      returnMap['prevOffset'] = 0
      returnMap['prevLimit'] = 0
  } else if ( offset < limit ){
      returnMap['prevOffset'] = 1
      returnMap['prevLimit'] = offset - 1
  } else {
      returnMap['prevOffset'] = offset - limit
      returnMap['prevLimit'] = limit
  }


  //next
  if ( (offset + limit) > size ){
    returnMap['nextOffset'] = 0
    returnMap['nextLimit'] = 0
  } else if ( ( size - ( offset + limit)) < limit  ){
    returnMap['nextOffset'] = offset + limit
    returnMap['nextLimit'] = size - (offset + limit - 1)
  } else {
      returnMap['nextOffset'] = offset + limit
      returnMap['nextLimit'] = limit
  }

  return returnMap

}



}
