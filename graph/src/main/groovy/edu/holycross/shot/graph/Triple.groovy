package edu.holycross.shot.graph

import edu.harvard.chs.cite.CiteUrn

/** Class representing a triple in a directed
 * graph as subject-verb-object (i.e, vertex1->edge->vertext2)
 */
class Triple {

  /** Subject of the triple. */
  URI subj
  /** Verb of the triple. */
  URI verb

  /** Object of the triple may be a  URI or String.*/
  Object obj
  

   /** Constructor (overloaded) for Triple object requiring all member properties. 
   * @param subj uri for the subject.
   * @param verb uri for the verb.
   * @param obj number data for the verb, if it is a number
   * @throws Exception if subj or verb are empty; or if not in URI form; 
   * or if obj is null. 
   */

  Triple(URI subj, URI verb, Number obj)
  throws Exception {
    if (subj == null) {
      throw new Exception("Triple: URI for subject cannot be null.")
    } else {
      this.subj = subj
    }
    if (verb == null) {
      throw new Exception("Triple: URI for verb cannot be null.")
    } else {
      this.verb = verb
    }
    if (obj == null) {
      throw new Exception("Triple: Number value for object cannot be null.")
    } else {
      this.obj = obj
    }
  }

  
 /** Constructor (overloaded) for Triple object requiring all member properties. 
   * @param subj uri for the subject.
   * @param verb uri for the verb.
   * @param obj string data for the verb, if it is raw data.
   * @throws Exception if subj or verb are empty; or if not in URI form; 
   * or if obj is null or empty.
   */

  Triple(URI subj, URI verb, String obj)
  throws Exception {
    if (subj == null) {
      throw new Exception("Triple: URI for subject cannot be null.")
    } else {
      this.subj = subj
    }
    if (verb == null) {
      throw new Exception("Triple: URI for verb cannot be null.")
    } else {
      this.verb = verb
    }
    if ((obj == null)||(obj == "")) {
      throw new Exception("Triple: String value for object cannot be empty or null.")
    } else {
      this.obj = obj
    }
  }

 /** Constructor (overloaded) for Triple object requiring all member properties. 
   * @param subj uri for the subject.
   * @param verb uri for the verb.
   * @param obj uri for the verb, if it is a URI.
   * @throws Exception if subj or verb are empty; or if not in URI form; 
   * or if both obj is null or not in URI form.
   */

  Triple(URI subj, URI verb, URI obj)
  throws Exception {
    if (subj == null) {
      throw new Exception("Triple: URI for subject cannot be null.")
    } else {
      this.subj = subj
    }
    if (verb == null) {
      throw new Exception("Triple: URI for verb cannot be null.")
    } else {
      this.verb = verb
    }
    if (obj == null) {
      throw new Exception("Triple: URI value for object cannot be null.")
    } else {
      this.obj = obj
    }
  }

  /**
   * Overrides default toString() method.
   * @returns .ttl formatted expession, e.g. "<sub> <verb> <obj> ." 
   */
  String toString() {
	  if (obj.getClass() == URI){
		return """<${subj}> <${verb}> <${obj.toString()}> ."""
	  }  else {
		return """<${subj}> <${verb}> "${obj}" ."""
	  }
  }


 
}
