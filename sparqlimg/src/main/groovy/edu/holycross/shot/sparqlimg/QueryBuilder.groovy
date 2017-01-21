package edu.holycross.shot.sparqlimg

// strip off RoI? on all queries?

import edu.harvard.chs.cite.Cite2Urn


/** A class using knowledge of the CHS Image extension to generate appropriate
* SPARQL queries.
*/
class QueryBuilder {

    String prefix = """
PREFIX cts:        <http://www.homermultitext.org/cts/rdf/>
PREFIX cite:        <http://www.homermultitext.org/cite/rdf/>
PREFIX hmt:        <http://www.homermultitext.org/hmt/rdf/>
PREFIX citedata:        <http://www.homermultitext.org/citedata/>
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

    /** Empty constructor.*/
    QueryBuilder() {
    }

		/** Generates a Sparql query for versions of an object
		* @param Cite2Urn
		* @returns String
		*/
		String resolveVersionQuery(Cite2Urn urn){
			String queryString = prefix
			queryString += """
			select ?v where {

				<${urn.toString()}> cite:hasVersion ?v

			}
			"""
			return queryString
		}

		String getExtendedCollectionQuery(String extensionString){
        return """${prefix}
			SELECT ?coll WHERE {
		?coll cite:extendedBy ${extensionString} .
		 }
		 """
		}

    String binaryPathQuery(Cite2Urn img) {

        return """${prefix}

        SELECT ?path WHERE {
           <${img}> cite:belongsTo ?coll .
           ?coll hmt:path ?path .
         }
         """
    }

    String getRightsProp(Cite2Urn img) {
        return """${prefix}
       SELECT ?prop WHERE {
        <${img}> cite:belongsTo ?collection .
        ?collection hmt:imageRightsProperty ?prop .
        }
        """
    }

    String getCaptionProp(Cite2Urn img) {
        return """${prefix}
       SELECT ?prop WHERE {
        <${img}> cite:belongsTo ?collection .
        ?collection hmt:imageCaptionProperty ?prop .
        }
        """
    }

    String getImageInfo(Cite2Urn img, String captionVerb, String rightsVerb) {
        return """${prefix}
       SELECT ?caption ?license WHERE {
        <${img}> ${captionVerb} ?caption .
        <${img}> ${rightsVerb} ?license .
        }
        """
    }

    String summarizeGroupsQuery() {
        return """${prefix}
        SELECT ?archv ?desc ?path (COUNT(?img) AS ?num) WHERE {
        ?archv rdf:type cite:ImageArchive .
        ?archv rdf:label ?desc .
        ?archv hmt:path ?path .
        ?img cite:belongsTo ?archv .
        }
        GROUP BY ?archv ?desc ?path
         """
     }

    String getGroupInfo (Cite2Urn group) {
        return """${prefix}
        SELECT ?archv ?desc ?path (COUNT(?img) AS ?num) WHERE {
        ?archv rdf:type cite:ImageArchive .
        ?archv rdf:label ?desc .
        ?archv hmt:path ?path .
        ?img cite:belongsTo ?archv .
        FILTER(str(?archv) = "${group}") .
        }
        GROUP BY ?archv ?desc ?path
        """
     }



}
