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
				ArrayList vfc = versionsForCollection(collUrn)
				if (vfc.size() > 0){
					Cite2Urn testUrn
					vfc.each{ curn ->
							testUrn = new Cite2Urn("${curn}${urn.objectId_1}-${urn.objectId_2}")
							if (objectExists(testUrn)){
									returnUrn = testUrn
							}
					}

				} else {
					throw new Exception("CcGraph, resolveVersion: No versions of collection ${urn.reduceToCollection()} exist in data.")
				}

			} else {

				if (urn.hasObjectId()){
					ArrayList vfo = versionsForObject(urn)
					if (vfo.size() > 0){
						returnUrn = vfo[0]
					} else {
						throw new Exception("CcGraph, resolveVersion: No versions of collection ${urn.reduceToCollection()} contain an object identified as ${urn}")
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
      String collectionUrnStr = urn.reduceToCollectionVersion()
      Cite2Urn collectionUrn = new Cite2Urn(collectionUrnStr)
      String qs = QueryBuilder.isOrderedQuery(collectionUrn)
      String reply = sparql.getSparqlReply("application/json", qs)
      JsonSlurper slurper = new groovy.json.JsonSlurper()
      def parsedReply = slurper.parseText(reply)
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
      rurn = resolveVersion(urn.getRangeBegin())
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
			if (tempUrnString.contains("urn:cite2:")){
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
      rurn = resolveVersion(urn.getRangeEnd())
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
      if (tempUrnString.contains("urn:cite2:")){
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
        Cite2Urn collUrn = resolveVersion(urn).reduceToCollectionVersion()
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
          replyMap['resolvedUrn'] = collUrn
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
        Cite2Urn collUrn = resolveVersion(urn).reduceToCollectionVersion()
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
          replyMap['resolvedUrn'] = collUrn
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
  * @returns Map ['urn':Cite2Urn, 'size': BigInteger]
  */

	Map getCollectionSize(Cite2Urn urn)
	throws Exception {
		Cite2Urn qUrn = resolveVersion(urn).reduceToCollectionVersion()

		String qs = QueryBuilder.getCollectionSizeQuery(qUrn)

		String reply = sparql.getSparqlReply("application/json", qs)
		JsonSlurper slurper = new groovy.json.JsonSlurper()
		def parsedReply = slurper.parseText(reply)
		if (parsedReply.results.bindings.size) {
			Map returnMap = [:]
			returnMap['resolvedUrn'] = qUrn
			returnMap['size'] = new BigInteger(parsedReply.results.bindings[0].size.value)
			return returnMap
		} else {
			throw new Exception( "CcGraph.getCollectionSize: ${urn.toString()}. Failed to get count.")
		}
	}


  /** Returns all valid Cite2Urns in a collection.
	* If the collection is un-versioned, resolves to a version.
	* If the URN expresses a range, returns all valid reffs between the identified objects.
  * @param Cite2Urn
  * @returns Map ['resolvedUrn': Cite2Urn, 'urns': ArrayList of Cite2Urns]
  */
  Map getValidReff(Cite2Urn urn)
		throws Exception {
			Cite2Urn rUrn = resolveVersion(urn)
			Cite2Urn collUrn = rUrn.reduceToCollectionVersion()
    // Is it an object or a range?
    if ( urn.isRange() ){
			if (isOrdered(collUrn) == false ){
				throw new Exception("CcGraph. getValidReff. ${collUrn} is not an ordered collection, so it cannot have a range-URN.")
			}
      return gvrForRange(urn)
		} else if ( urn.hasObjectId()){
			def returnMap = [:]
			returnMap["resolvedUrn"] = rUrn
			def gvrUrns = []
			gvrUrns << rUrn
			returnMap["urns"] = gvrUrns
			return returnMap
    } else {
      return gvrForCollection(urn)
    }
  }

  Map gvrForCollection(Cite2Urn urn)
  throws Exception {
		def returnMap = [:]
		def gvrUrns = []
		Cite2Urn qUrn = resolveVersion(urn).reduceToCollectionVersion()
		returnMap["resolvedUrn"] = qUrn

		String qs = QueryBuilder.getGVRQuery(qUrn)

		String reply = sparql.getSparqlReply("application/json", qs)
		JsonSlurper slurper = new groovy.json.JsonSlurper()
		def parsedReply = slurper.parseText(reply)
		if (parsedReply.results.bindings.size) {
			parsedReply.results.bindings.each{ bb ->
				gvrUrns << new Cite2Urn(bb.urn.value)
			}
			returnMap["urns"] = gvrUrns
		} else {
			throw new Exception( "CcGraph.getValidReff: ${urn.toString()}, resolved to ${qUrn} did not yield any valid URNs..")
		}

		return returnMap
  }

  Map gvrForRange(Cite2Urn urn)
  throws Exception {
		def returnMap = [:]
		def gvrUrns = []
		Cite2Urn qUrn = resolveVersion(urn)
		returnMap['resolvedUrn'] = qUrn
		if (urn.isRange() == false ){
			throw new Exception("CcGraph. gvrForRange. ${urn} does not specify a range .")
		}

		Cite2Urn range1 = qUrn.getRangeBegin()
		Cite2Urn range2 = qUrn.getRangeEnd()
		Cite2Urn collUrn = qUrn.reduceToCollectionVersion()

		Integer startSeq = getSequenceForObject(range1)
		Integer endSeq = getSequenceForObject(range2)

		String qs = QueryBuilder.getGVRRangeQuery(collUrn, startSeq, endSeq)

		String reply = sparql.getSparqlReply("application/json", qs)
		JsonSlurper slurper = new groovy.json.JsonSlurper()
		def parsedReply = slurper.parseText(reply)
		if (parsedReply.results.bindings.size) {
			parsedReply.results.bindings.each{ bb ->
				gvrUrns << new Cite2Urn(bb.urn.value)
			}
			returnMap["urns"] = gvrUrns
		} else {
			throw new Exception( "CcGraph.getValidReff: ${urn.toString()}, resolved to ${qUrn} did not yield any valid URNs..")
		}

		return returnMap
  }


  /** Returns the olo:item sequence number for an object.
	*   An exception if the object is not from an ordered collection,
	*   or if the URN is a range, or lacking an ObjectId.
	*   @param Cite2Urn
	*   @returns Integer
	*/
	Integer getSequenceForObject(Cite2Urn urn){

		if (urn.isRange()){
			throw new Exception("CcGraph.getSequenceForObject: ${urn} is a range.")
		}
		if (urn.hasObjectId() == false){
			throw new Exception("CcGraph.getSequenceForObject: ${urn} lacks an object-ID.")
		}

		Cite2Urn qUrn = resolveVersion(urn)
		Integer seq

		String qs = QueryBuilder.getSequenceQuery(qUrn)

		String reply = sparql.getSparqlReply("application/json", qs)
		JsonSlurper slurper = new groovy.json.JsonSlurper()
		def parsedReply = slurper.parseText(reply)
		if (parsedReply.results.bindings.size) {
			if (parsedReply.results.bindings[0].seq.value.isInteger()){
				seq = parsedReply.results.bindings[0].seq.value.toInteger()
			} else {
				throw new Exception( "CcGraph.getSequenceForObject: ${urn.toString()}, yielded a sequence that was unaccountably not an integer.")
			}
		} else {
			throw new Exception( "CcGraph.getSequenceForObject: ${urn.toString()}, did not yield a sequence.")
		}

			return seq
	}



  /* Returns a CiteCollection
  * @param Cite2Urn
  * @returns CiteCollection object
  */
  CiteCollection getCollection(Cite2Urn urn){
    Cite2Urn collUrn = resolveVersion(urn).reduceToCollectionVersion()

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

        Cite2Urn collUrn = resolveVersion(urn).reduceToCollectionVersion()

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
          pType = CitePropertyType.CITE2_URN // it has to be
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
    Cite2Urn collUrn = resolveVersion(urn).reduceToCollectionVersion()

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
    Cite2Urn collUrn = urn.reduceToCollectionVersion()

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
    Cite2Urn collUrn = resolveVersion(urn).reduceToCollectionVersion()
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
      Cite2Urn collUrn = urn.reduceToCollectionVersion()
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
            thisType = CitePropertyType.CITE2_URN
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

    // Make a CiteCollection object
		CiteCollectionObject collectionObject
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
    String reply = sparql.getSparqlReply("application/json", qs)
    JsonSlurper slurper = new groovy.json.JsonSlurper()
    def parsedReply = slurper.parseText(reply)
    parsedReply.results.bindings[0].each{ bb ->
        propMap["${bb.key}"] = bb.value.value
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
      objects << getObject(vr)
    }
    return objects

}


/**Given a Cite2Urn, an offset, a limit, and a version-string, returns a Map, containing a
* Collection-URN and an Array of Cite2Urns. The urn of the CCOSet
* will reflect the data actually returned.
* @param Cite2Urn
* @returns Map ['urn':Cite2Urn, 'offset':integer, 'limit':integer, 'size':BigInteger 'objects':ArrayList]
*/
Map getPagedReff(Cite2Urn paramUrn, Integer offset, Integer limit)
throws Exception {

  if ( (offset < 1) || (limit < 1)){
         throw new Exception( "CcGraph.getPagedReff: ${paramUrn.toString()}. Neither parameter 'offset' nor 'limit' may be less than 1. offset = ${offset}; limit=${limit}")
  }

  def pagedReff = [:]
  Cite2Urn urn
	System.err.println("Going to call resolveVersion on ${urn}")
  urn = resolveVersion(paramUrn)
  pagedReff['resolvedUrn'] = urn
  pagedReff['offset'] = offset
  pagedReff['limit'] = limit

  def urns = [] // will hold the CiteCollectionObjects

  ArrayList firstArray = getValidReff(urn)['urns']
  pagedReff['size'] = firstArray.size()

  // let's get absolute values for start and end
  def startIndex = offset - 1
  def endIndex = startIndex + limit - 1


  if (offset > firstArray.size()){
    pagedReff['urns'] = urns // that is, an empty array


  } else {
    if ( endIndex > firstArray.size()){


      endIndex = firstArray.size() - 1
      pagedReff['limit'] = firstArray.size() - offset + 1

    }
    for (i in (startIndex..endIndex) ){
      urns << firstArray[i]
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
        getCollectionSizeMap = getCollectionSize(requestUrn)

        replyString += "<resolvedUrn>${getCollectionSizeMap['resolvedUrn']}</resolvedUrn>\n"
        replyString += "</cite:request>\n<cite:reply>\n"
        replyString += "<count>${getCollectionSizeMap['size']}</count>\n"
        replyString += "</cite:reply>\n"
        replyString += "</${request}>"
      break;

// GetValidReff -----------------------------------
      case "GetValidReff":

        // Lets assemble the params we need, all at once
        Boolean gvrSafeMode

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

     gvr = getValidReff(requestUrn)
     System.err.println("-----\n${gvr}\n-----")
		 System.err.println("resolvedUrn: ${gvr['resolvedUrn']}\n")
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

        Map pagedGvr = getPagedReff(requestUrn, offset, limit)

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
        ArrayList urns = versionsForObject(requestUrn)
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

				System.err.println("Going to call getPaged with:\n${requestUrn}\n${offset}\n${limit}\n-----")
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
    case "CITE2_URN" :
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

}
