package edu.holycross.shot.sparqlcc

import edu.harvard.chs.cite.CiteUrn
import edu.holycross.shot.prestochango.*
import groovy.json.JsonSlurper

/** A class representing the implementation of a set of CITE Collection Objects
 */
class CCOSet {


  /** CITE URN for this set */
  public CiteUrn urn = null

  /** CITE URN for this first and last elements of this set; may be the same */
  public CiteUrn startUrn = null
  public CiteUrn endUrn = null

  /** CITE Collection to which this object belongs **/
  public CiteCollection collection = null

  /** ArrayList of one or more CiteCollectionObjects
	* One object for uordered collection, more than one for ranges in an ordered collection
  **/
  public ArrayList ccos = []

  /** Constructor for CCOS, a CiteCollectionObjectSet
   * @param CiteCollection collection A CiteCollection
   * @param ArrayList ccos an arrayList of CiteCollectionObjects
   */
  CCOSet(
    CiteCollection collection,
    ArrayList ccos ) throws Exception {

    //Test ccos for unique URNs
    def testUrns = ccos.collect { it.urn.toString() } as Set
    if (testUrns.size() != ccos.size()) {
      throw new Exception("CiteCollectionObjectSet: Set of Cite Collection Objects must have unique URNs.")
    }

    //Test ccos for unique sequence numbers
    if (collection.isOrderedCollection){
      def seqList = []
      ccos.each { cco ->
        seqList << cco.getSequence()
      }
      def uniquedSeqList = seqList as Set

      if (uniquedSeqList.size() != ccos.size()) {
        throw new Exception("CiteCollectionObjectSet: Set of Cite Collection Objects must have unique sequenceNumbers.")
      }
    }

    /** Start **/
    // Trying to sort under both jdk7 and jdk8

    // fails on jdk7 or is it Groovy version?
    //ccos.sort( { objA, objB -> objA.getSequence() <=> objB.getSequence() } as Comparator)*.key
    if (collection.isOrderedCollection){
      ccos.sort {
        CiteCollectionObject o1, CiteCollectionObject o2 -> o1.getSequence().compareTo(o2.getSequence())
      }
    }

    //String sequenceProp = this.collection.orderedByProp
    //ccos.sort( { objA, objB -> objA.objectProperties[sequenceProp] <=> objB.objectProperties[sequenceProp] } as Comparator)*.key

    this.ccos = ccos
    /** End **/

    // get boundary urns for easy access
    this.startUrn = ccos[0].urn
    this.endUrn = ccos[-1].urn

    //Construct URN out of first and last cco
    String tempUrnString = this.startUrn.toString()
    if (this.startUrn.toString() != this.endUrn.toString() ) {
      tempUrnString += "-${this.endUrn.getObjectWithoutCollection()}"
    }
    this.urn = new CiteUrn(tempUrnString)

  }

  /** Return the number of objects in this set.
   * @returns BigInteger
   */
  public BigInteger countObjects(){
    BigInteger howMany = this.ccos.size()
  }

}
