/*
Generate all ORCA TTL from two-column input source like
"tabs-tokenized.tsv".

Usage: groovy ttlFrom2cols.groovy <FILENAME>

Writes output to stdout, so redirect as you like.

 */

if (args.size() != 1) {
  System.err.println "Usage: groovy ttlFrom2cols.groovy <FILENAME>"
  System.exit(-1)
}
File tabsFile = new File(args[0])


// All libs available from beta.hpcc.uh.edu:
@GrabResolver(name='beta', root='http://beta.hpcc.uh.edu/nexus/content/repositories/releases')
@Grab(group='edu.harvard.chs', module='cite', version='0.95.2')
// necessary imports
import edu.harvard.chs.cite.CtsUrn

// Prefixes

def prefixes = """
@prefix cts:        <http://www.homermultitext.org/cts/rdf/> .  
@prefix cite:        <http://www.homermultitext.org/cite/rdf/> . 
@prefix citedata:        <http://www.homermultitext.org/hmt/citedata/> . 
@prefix dcterms: <http://purl.org/dc/terms/> . 
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.  
@prefix  xsd: <http://www.w3.org/2001/XMLSchema#> . 
@prefix olo:     <http://purl.org/ontology/olo/core#> . 
@prefix rdfs:   <http://www.w3.org/2000/01/rdf-schema#> . 
@prefix orca: <http://www.homermultitext.org/orca/rdf/> . 

"""


println "${prefixes} \n\n"

def tsvFile = new File('tabs-tokens.tsv')
def ctsUrn = ""
def analysisUrn = ""
def idUrnBase = "urn:cite:hmt:VenAIliad_classifiedTokens."
def idUrn = ""

// Write Classified Tokens ORCA Collection Info
//		The Collection

println """<http://www.homermultitext.org/hmt/rdf> cite:abbreviatedBy "hmt" .  """
println """<http://www.homermultitext.org/hmt/rdf> rdf:type cite:DataNs .  """
println """<urn:cite:hmt:VenAIliad_classifiedTokens> rdf:type	cite:CiteCollection .  """
println """<urn:cite:hmt:VenAIliad_classifiedTokens> rdf:type	cite:ORCA .  """
println """<urn:cite:hmt:VenAIliad_classifiedTokens> rdf:label "Classified Tokenization of diplomatic editions from the Homer Multitext." .  """
println """<urn:cite:hmt:VenAIliad_classifiedTokens> cite:ordered "true" .  """

//		Its Properties

println """<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:collProperty		<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_URN> . """
println """<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:collProperty		<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Label> . """
println """<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:collProperty		<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Sequence> . """
println """<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:collProperty		<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Analysis> . """
println """<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:collProperty		<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_AnalyzedText> . \n"""

println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_URN>	cite:propLabel "Object URN" . """
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_URN> rdf:type cite:Property . """
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_URN> cite:propType cite:CiteUrn  . \n"""

println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Label>	cite:propLabel "Label" . """
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Label> rdf:type cite:Property . """
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Label> cite:propType "string" . \n"""
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Sequence>	cite:propLabel "Sequence" . """
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Sequence> rdf:type cite:Property . """
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Sequence> cite:propType "number" . \n"""
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Analysis>	cite:propLabel "Analysis" . """
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Analysis> rdf:type cite:Property . """
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Analysis> cite:propType cite:CiteUrn  . \n"""
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_AnalyzedText>	cite:propLabel "Analyzed Text" . """
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_AnalyzedText> rdf:type cite:Property . """
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_AnalyzedText> cite:propType cite:CtsUrn  . \n"""
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_ResultingText>	cite:propLabel "Resulting Text" . """
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_ResultingText> rdf:type cite:Property . """
println """<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_ResultingText> cite:propType "string" . \n"""



//		Important Properties

println """<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:canonicalId		<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_URN> . """
println """<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:orderedBy <http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Sequence> . \n"""



def count = 1

def prevUrn = ""
def nextUrn = ""

tsvFile.eachLine { l ->
	
	// Write ORCA Specific Stuff	
	ctsUrn = new CtsUrn(l.tokenize("\t")[0])
	String citationUrnString = ctsUrn.toString().tokenize("@")[0]
	citationUrn = new CtsUrn(citationUrnString)
	analysisUrn = l.tokenize("\t")[1]
	idUrn = "${idUrnBase}${count}"
	println "<${idUrn}> olo:item ${count} . \n\n"
	if (prevUrn != ""){
	  println "<${idUrn}> olo:previous <${prevUrn}> . \n"
	  println "<${prevUrn}> olo:next <${idUrn}> . \n\n"
	}
	println "<${idUrn}> cite:analyzes <${ctsUrn}> . \n"
	println "<${ctsUrn}> cite:analyzedBy <${idUrn}> . \n"
	println "<${idUrn}> cite:hasAnalysis <${analysisUrn}> . \n"
	println "<${analysisUrn}> cite:analysisFor <${idUrn}> . \n"
	println """<${idUrn}> cite:textResult "${ctsUrn.getSubref()}" . \n"""
	println """<${ctsUrn}> cts:isSubstringOf <${citationUrn}> .\n"""
	println """<${citationUrn}> cts:hasSubstring <${ctsUrn}> .\n"""
	println "\n"

	// Write General Collection Stuff
println """<urn:cite:hmt:VenAIliad_classifiedTokens> cite:possesses <${idUrn}> .\n"""
println """<${idUrn}> cite:belongsTo <urn:cite:hmt:VenAIliad_classifiedTokens> .\n"""
println """<${idUrn}> rdf:label "Classified Token #${count}." .\n"""
println """<${idUrn}>	<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Label> "Classified Token #${count}." .\n"""
println """<${idUrn}>	<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Sequence> ${count} . \n"""
println """<${idUrn}>	<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Analysis> <${analysisUrn}> . \n"""
println """<${idUrn}>	<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_AnalyzedText> <${ctsUrn}> . \n"""
println """<${idUrn}>	<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_ResultingText> "${ctsUrn.getSubref()}" . \n\n\n"""



	// clean up
	prevUrn = idUrn
	count++

/* 
`<ORCA Object URN> cite:analyzes <passage analyzed CTS URN> .` inverse: `cite:analyzedBy`

`<ORCA Object URN> cite:hasAnalysis <analysis CITE URN> .` inverse: `cite:analysisFor` 

`<ORCA Object URN> cite:textResult "text result" .` inverse: `cite:textResultFor`
*/
			
}

