package edu.holycross.shot.sparqlcc

import edu.harvard.chs.cite.Cite2Urn

abstract class QueryBuilder {


  static String prefixPhrase  = """
  PREFIX cts:        <http://www.homermultitext.org/cts/rdf/>
  PREFIX cite:        <http://www.homermultitext.org/cite/rdf/>
  PREFIX hmt:        <http://www.homermultitext.org/hmt/rdf/>
  PREFIX citedata:        <http://www.homermultitext.org/hmt/citedata/>
  PREFIX dcterms: <http://purl.org/dc/terms/>
  PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
  PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
  PREFIX olo:     <http://purl.org/ontology/olo/core#>
  PREFIX lex:        <http://data.perseus.org/rdfverbs/>
  PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>
  PREFIX owl:        <http://www.w3.org/2002/07/owl#>
  PREFIX dse:        <http://www.homermultitext.org/dse/rdf>
  PREFIX orca: <http://www.homermultitext.org/orca/rdf/>

  """

  static String getExampleQuery(Cite2Urn urn) {
    return "Query about ${urn}"
  }

  /** Generates a Sparql query for versions of an object
  * @param Cite2Urn
  * @returns String
  */
  static String resolveVersionQuery(Cite2Urn urn){
    String queryString = prefixPhrase
    queryString += """
    select ?v where {

      <${urn.toString()}> cite:hasVersion ?v

    }
    """
    return queryString
  }


  /** Returns a Sparql query counting distinct notional objects in a collecction
  * @param Cite2Urn
  * @returns String
  */
  static String getCollectionSizeQuery(Cite2Urn collUrn) {
    String queryString = prefixPhrase
    queryString += """
    SELECT (COUNT(distinct ?urn) AS ?size) WHERE {
      ?urn cite:belongsTo <${collUrn}> .
      ?urn cite:hasVersion ?version .
    }
    """
    return queryString
  }

  /** Returns a Sparql query counting versioned objects in a collecction
  * @param Cite2Urn
  * @returns String
  */
  static String getVersionedCollectionSizeQuery(Cite2Urn collUrn, String vString) {
    String queryString = prefixPhrase
    queryString += """
    SELECT (COUNT(distinct ?urn) AS ?size) WHERE {
      ?urn cite:belongsTo <${collUrn}> .
      ?urn cite:isVersionOf ?notional .
      FILTER(regex(str(?urn), ".${vString}\$"))
    }
    """
    return queryString
  }

  /** Generates a Sparql query for the presence of a cite:orderedBy statement
  * @param Cite2Urn
  * @returns String
  */
  static String isOrderedQuery(Cite2Urn urn){
    String queryString = prefixPhrase
    queryString += """
    ask {
      <${urn.toString()}> cite:ordered "true" .
    }
    """
    return queryString
  }

  /** Generates a Sparql query for finding the previous object
  * in an ordered collection
  * @param Cite2Urn
  * @returns String
  */
  static String getPreviousQuery(Cite2Urn urn){
    String queryString = prefixPhrase
    queryString += """
    select ?u where {
      <${urn.toString()}> olo:previous  ?u
    }
    """
    return queryString
  }

  /** Generates a Sparql query for finding the next object
  * in an ordered collection
  * @param Cite2Urn
  * @returns String
  */
  static String getNextQuery(Cite2Urn urn){
    String queryString = prefixPhrase
    queryString += """
    select ?u where {
      <${urn.toString()}> olo:next  ?u
    }
    """
    return queryString
  }


  /** Generates a Sparql query for finding the last object
  * in an ordered collection
  * @param Cite2Urn must be a collection-level urn
  * @returns String
  */
  /* Using ?urn at the top and in the nested SELECT cuts 30% off execution time, for some reason */
  static String getLastQuery(Cite2Urn collUrn) {
    String queryString = prefixPhrase
    queryString += """
    SELECT ?urn WHERE {
      ?urn cite:belongsTo <${collUrn}> .
      ?urn olo:item ?seq .
      { SELECT (MAX (?s) as ?max )
        WHERE {
          ?urn olo:item ?s .
          ?urn cite:belongsTo <${collUrn}> .
        }
      }
      FILTER (?seq = ?max).
    }
    """
    return queryString
  }



  /** Generates a Sparql query for finding the first object
  * in an ordered collection; ignores versions, so you
  * get whatever object has the lowest sequence in the collection.
  * @param Cite2Urn must be a collection-level urn
  * @returns String
  */
  /* Using ?urn at the top and in the nested SELECT cuts 30% off execution time, for some reason */
  static String getFirstQuery(Cite2Urn collUrn) {
    String queryString = prefixPhrase
    queryString += """
    SELECT ?urn WHERE {
      ?urn cite:belongsTo <${collUrn}> .
      ?urn olo:item ?seq .
      { SELECT  (MIN (?s) as ?min)
        WHERE {
          ?urn olo:item ?s .
          ?urn cite:belongsTo <${collUrn}> .
        }
      }
      FILTER (?seq = ?min) .
    }
    """
    return queryString
  }

  /** Generates a Sparql query for finding every versioned URN in a collection
  * in an ordered collection
  * @param Cite2Urn must be a collection-level urn
  * @returns String
  */
  /* Using ?urn at the top and in the nested SELECT cuts 30% off execution time, for some reason */
  static String getVersionedObjectsQuery(Cite2Urn collUrn) {
    String queryString = prefixPhrase
    queryString += """
    select distinct ?v where {
      <${collUrn}> cite:hasVersion ?c .
			?c cite:possesses ?o .
      ?o cite:hasVersion ?v .
    }
    """
    return queryString
  }

  /** Generates a Sparql query for finding every version of an identified
  * collection
  * @param Cite2Urn must be a collection-level urn
  * @returns String
  */
  /* Using ?urn at the top and in the nested SELECT cuts 30% off execution time, for some reason */
  static String getVersionsForCollectionQuery(Cite2Urn urn) {
    String queryString = prefixPhrase
    queryString += """
    select ?cv where {
      <${urn}> cite:hasVersion ?cv .
    }
    """
    return queryString
  }

  /** Generates a Sparql query for testing whether a Cite2 object-level URN
  * is represented in the data
  * @param Cite2Urn
  * @returns String
  */
  static String getObjectExistsQuery(Cite2Urn urn) {
    String queryString = prefixPhrase
    queryString += """
    ask {
      <${urn}> cite:belongsTo ?cv .
    }
    """
    return queryString
  }

  /** Generates a Sparql query for finding all objects, with all
  * versions, in a collection.
  * @param Cite2Urn
  * @returns String
  */
  static String getGVRCollectionQuery(Cite2Urn urn){
    String queryString = prefixPhrase
    queryString += """
    SELECT distinct ?urn WHERE {
           ?urn cite:belongsTo <${urn}> .
      		?urn cite:isVersionOf ?notional .
          }
    ORDER BY ?urn
    """
    return queryString
  }

  /** Generates a Sparql query for finding all objects, with all
  * versions, in an ordered collection.
  * @param Cite2Urn
  * @returns String
  */
  static String getGVROrderedCollectionQuery(Cite2Urn urn){
    String queryString = prefixPhrase
    queryString += """
    SELECT distinct ?urn WHERE {
           ?urn cite:belongsTo <${urn}> .
          ?urn cite:isVersionOf ?notional .
          ?urn olo:item ?seq .
          }
    ORDER BY ?seq
    """
    return queryString
  }

  /** Generates a Sparql query for finding all object-URNs, with
  * a given version, in a collection.
  * @param Cite2Urn
  * @param String
  * @returns String
  */
  static String getGVRCollectionVersionedQuery(Cite2Urn urn, String vString){
    String queryString = prefixPhrase
    queryString += """
    SELECT distinct ?urn WHERE {
           ?urn cite:belongsTo <${urn}> .
      		?urn cite:isVersionOf ?notional .
          FILTER(regex(str(?urn), ".${vString}\$"))
      }
    ORDER BY ?urn
    """
    return queryString
  }

  /** Generates a Sparql query for finding all objects, with a
  * given version-string, in an ordered collection.
  * @param Cite2Urn
  * @returns String
  */
  static String getGVROrderedCollectionVersionedQuery(Cite2Urn urn, String vString){
    String queryString = prefixPhrase
    queryString += """
    SELECT distinct ?urn WHERE {
           ?urn cite:belongsTo <${urn}> .
          ?urn cite:isVersionOf ?notional .
          ?urn olo:item ?seq .
          FILTER(regex(str(?urn), ".${vString}\$"))
          }
    ORDER BY ?seq
    """
    return queryString
  }

  /** Generates a Sparql query for finding all object-URNs
  * defined by a range, for an ordered collection
  * @param Cite2Urn
  * @param String
  * @returns String
  */
  static String getGVRRangeQuery(Cite2Urn urn, String vString){
    String queryString = prefixPhrase
    queryString += """

    """
    return queryString
  }

  /** Generates a Sparql query for finding all properties
  * their values, and their labels for a CITE object
  * @param Cite2Urn
  * @returns String
  */
  static String getObjectQuery(Cite2Urn urn){
    String queryString = prefixPhrase
    queryString += """
    SELECT ?propval ?property ?label ?type WHERE {
      <?{urn}> cite:belongsTo ?coll  .
      ?coll cite:collProperty ?property .
      <${urn}> ?property ?propval .
      ?property cite:propLabel ?label .
      ?property cite:propType ?type .
      } """
      return queryString
    }

    /** Generates query for finding the full namespace, given a NS-abbreviation
    * @param String
    * @returns String
    */
  static String getNamespaceQuery(String abbr){
    String queryString = prefixPhrase
    queryString += """
    SELECT ?full  WHERE {
       ?full cite:abbreviatedBy "${abbr}" .
      } """
      return queryString
    }


    /** Generates a query for finding the properties of a collection, their types, and labels.
    * @param Cite2Urn (collection-level)
    * @returns String.
    */
    static String getPropertiesForCollectionQuery(Cite2Urn urn){
      String queryString = prefixPhrase
      queryString += """
      SELECT ?property ?label ?type  WHERE {
             <${urn}> cite:collProperty ?property .
             ?property cite:propType ?type .
             ?property cite:propLabel ?label .
        } """
        return queryString
    }

    /** Generates a query for finding the canlnicalId property
    * of a collection, its type, and label.
    * @param Cite2Urn (collection-level)
    * @returns String.
    */
    static String getCanonicalIdPropQuery(Cite2Urn urn){
      String queryString = prefixPhrase
      queryString += """
      select ?name ?type ?label where {
      <${urn}> cite:idPropName ?name .
      ?name cite:propType ?type .
      ?name cite:propLabel ?label .
    } """
        return queryString
    }

    /** Generates a query for finding the extensions
    * of a collection
    * @param Cite2Urn (collection-level)
    * @returns String.
    */
    static String getExtensionsQuery(Cite2Urn urn){
      String queryString = prefixPhrase
      queryString += """
      select ?ext where {
      <${urn}> cite:extendedBy ?ext .
    } """
        return queryString
    }

    /** Generates a query for finding the label
    * of a collection
    * @param Cite2Urn (collection-level)
    * @returns String.
    */
    static String getCollectionLabelPropQuery(Cite2Urn urn){
      String queryString = prefixPhrase
      queryString += """
      select ?name ?type ?label where {
      <${urn}> cite:labelPropName ?name .
      ?name cite:propType ?type .
      ?name cite:propLabel ?label .
    } """
        return queryString
    }

    /** Generates a query for finding the orderedBy property
    * of a collection
    * @param Cite2Urn (collection-level)
    * @returns String.
    */
    static String getOrderedByPropQuery(Cite2Urn urn){
      String queryString = prefixPhrase
      queryString += """
      select ?name ?type ?label where {
      <${urn}> cite:orderingPropName ?name .
      ?name cite:propType ?type .
      ?name cite:propLabel ?label .
    } """
        return queryString
    }

    /** Generates a query for finding the values of all properties
    * of an object. Needs an Array of `citedata:…` verbs, generated from
    * the collection.
    * @param Cite2Urn
    * @param ArrayList of citedata: namespaces verbsList
    * @param ArrayList of property names
    * @returns String
    */
    static String getPropertiesForObjectQuery(Cite2Urn urn, ArrayList verbs, ArrayList props){
      String queryString = prefixPhrase
      queryString += "SELECT "
      props.each{ pp ->
        queryString += "?${pp} "
      }
      queryString += "WHERE { "
      verbs.eachWithIndex{ vv, i ->
        queryString += """optional { <${urn}> <${vv}> ?${props[i]} . }
        """
      }
      queryString += " }"
      return queryString

    }

}
