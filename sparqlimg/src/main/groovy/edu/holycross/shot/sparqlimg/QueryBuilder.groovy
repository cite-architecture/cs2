package edu.holycross.shot.sparqlimg

// strip off RoI? on all queries?

import edu.harvard.chs.cite.CiteUrn


/** A class using knowledge of the CHS Image extension to generate appropriate
* SPARQL queries.
*/
class QueryBuilder {

    String prefix = """prefix hmt:        <http://www.homermultitext.org/hmt/rdf/>
prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
prefix cite:        <http://www.homermultitext.org/datans/>
prefix citedata:         <http://www.homermultitext.org/citedata/>
"""

    /** Empty constructor.*/
    QueryBuilder() {
    }


    String binaryPathQuery(CiteUrn img) {

        return """${prefix}

        SELECT ?path WHERE {
           <${img}> cite:belongsTo ?coll .
           ?coll hmt:path ?path .
         }
         """
    }

    String getRightsProp(CiteUrn img) {
        return """${prefix}
       SELECT ?prop WHERE {
        <${img}> cite:belongsTo ?collection .
        ?collection hmt:imageRightsProperty ?prop .
        }
        """
    }

    String getCaptionProp(CiteUrn img) {
        return """${prefix}
       SELECT ?prop WHERE {
        <${img}> cite:belongsTo ?collection .
        ?collection hmt:imageCaptionProperty ?prop .
        }
        """
    }

    String getImageInfo(CiteUrn img, String captionVerb, String rightsVerb) {
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

    String getGroupInfo (CiteUrn group) {
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
