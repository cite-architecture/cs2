package edu.holycross.shot.orca

import edu.holycross.shot.citeservlet.Sparql

import edu.harvard.chs.cite.CtsUrn
import groovy.json.JsonSlurper

/** A class interacting with a SPARQL endpoint to
 * to resolve SPARQL replies into objects in the abstract data
 * model of ORCA.
 */
class OrcaGraph {

  /** SPARQL endpoint object from citeservlet lib. */
  Sparql sparql


  /** Constructor with required SPARQL endpoint object */  
  OrcaGraph(Sparql endPoint) {
    sparql = endPoint
  }

  /* ////////////////// Occurrences of an analysis within text /////////////////////*/

  // gets an ordered list of AnalysisRecord objects,
  // in which the text passages area analyzed as analysis
  ArrayList getOccurrences(CiteUrn analysis) {
  }


  // gets the idx'th AnalysisRecord from the ordered list of AnalysisRecord objects,
  // in which the text passages area analyzed as analysis
  AnalysisRecord getOccurrence(CiteUrn analysis, Integer idx) {
  }

  
  // gets an ordered list of AnalysisRecord objects,
  // in which the text identified by textFilter area
  // is analyzed as analysis
  ArrayList getOccurrences(CiteUrn analysis, CtsUrn textFilter) {
  }


  // gets the idx'th AnalysisRecord from the ordered list of AnalysisRecord objects
  // in which the text identified by textFilter area
  // is analyzed as analysis
  AnalysisRecord getOccurrence(CiteUrn analysis, CtsUrn textFilter, Integer idx) {
  }



  /* ////////////////// Analyses for a given textual source  ///////////////////// */

  // gets an ordered list of AnalysisRecord objects,
  // in which the analysis falls within textSource
  ArrayList getAnalyses(CtsUrn textSource) {
  }


  // gets the idx'th AnalysisRecord from the ordered list of AnalysisRecord objects,
  // in which the analysis falls within textSource
    AnalysisRecord getOccurrence(CtsUrn textSource, Integer idx) {
  }

  
  // gets an ordered list of AnalysisRecord objects,
  // in which the analysis mathces analysisFilter and falls within textSourcea
  ArrayList getOccurrences(CtsUrn textSource, CiteUrn analysisFilter) {
  }

  // gets the idx'th AnalysisRecord from the ordered list of AnalysisRecord objects,
  // in which the analysis mathces analysisFilter and falls within textSourcea
  AnalysisRecord getOccurrence(CtsUrn textSource, CiteUrn analysisFilter, Integer idx) {
  }


  
}
