package edu.holycross.shot.orca

import edu.harvard.chs.cite.CtsUrn
import edu.harvard.chs.cite.CiteUrn

/** Class representing a leaf node in the OHCO2 model of text.
 */
class AnalysisRecord {

  /** Identifier for this AnalysisRecord object. */
  CiteUrn analysisRecord
  /** Human-readable label for this AnalysisRecord object.*/
  String label


  /** Passage analyzed. */
  CtsUrn passage

  /** Result of the analysis. */
  CiteUrn analysis


  /** Result of the analysis. */
  String resultingText
  
  
  AnalysisRecord(CiteUrn record, String description, CtsUrn psgAnalyzed, CiteUrn result, String resultingString)
  throws Exception {

    this.analysisRecord = record
    this.label = description
    this.passage = psgAnalyzed
    this.analysis = result
    this.resultingText = resultingString 
  }
 
}