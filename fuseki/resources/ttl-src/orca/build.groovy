
// All libs available from beta.hpcc.uh.edu:
@GrabResolver(name='beta', root='http://beta.hpcc.uh.edu\nexus/content/repositories/releases')

@Grab(group='edu.harvard.chs', module='cite', version='0.95.2')




// necessary imports
import edu.harvard.chs.cite.CtsUrn

// Prefixes

def prefixes = """
@prefix cts:        <http://www.homermultitext.org/cts/rdf/> .  
@prefix cite:        <http://www.homermultitext.org/cite/rdf/> . 
@prefix hmt:        <http://www.homermultitext.org/hmt/rdf/> . 
@prefix citedata:        <http://www.homermultitext.org/hmt/citedata/> . 
@prefix dcterms: <http://purl.org/dc/terms/> . 
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.  
@prefix  xsd: <http://www.w3.org/2001/XMLSchema#> . 
@prefix olo:     <http://purl.org/ontology/olo/core#> . 
@prefix lex:        <http://data.perseus.org/rdfverbs/> . 
@prefix pal: <http://shot.holycrossedu/rdfverbs/pal/> . 
@prefix rdfs:   <http://www.w3.org/2000/01/rdf-schema#> . 
@prefix latepig: <http://shot.holycross.edu/rdf/latepig/> . 
@prefix orca: <http://www.homermultitext.org/orca/rdf/> . 

"""

OutputStreamWriter ors = new OutputStreamWriter(new FileOutputStream("orca.ttl"), "UTF-8")
ors.write("${prefixes} \n\n")

def tsvFile = new File('tabs-tokens.tsv')
def ctsUrn = ""
def analysisUrn = ""
def idUrnBase = "urn:cite:hmt:VenAIliad_classifiedTokens."
def idUrn = ""

// Write Classified Tokens ORCA Collection Info

//		The Collection

ors.write("""<http://www.homermultitext.org/hmt/rdf> cite:abbreviatedBy "hmt" .  \n""")
ors.write("""<http://www.homermultitext.org/hmt/rdf> rdf:type cite:DataNs .  \n""")
ors.write("""<urn:cite:hmt:VenAIliad_classifiedTokens> rdf:type	cite:CiteCollection .  \n""")
ors.write("""<urn:cite:hmt:VenAIliad_classifiedTokens> rdf:label "Classified Tokenization of diplomatic editions from the Homer Multitext." .  \n""")
ors.write("""<urn:cite:hmt:VenAIliad_classifiedTokens> cite:ordered "true" .  \n\n""")

//		Its Properties

ors.write("""<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:collProperty		<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_URN> . \n""")
ors.write("""<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:collProperty		<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Label> . \n""")
ors.write("""<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:collProperty		<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Sequence> . \n""")
ors.write("""<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:collProperty		<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Analysis> . \n""")
ors.write("""<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:collProperty		<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_AnalyzedText> . \n\n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_URN>	cite:propLabel "Object URN" . \n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_URN> rdf:type rdf:Property . \n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_URN> cite:propType cite:CiteUrn  . \n\n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Label>	cite:propLabel "Label" . \n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Label> rdf:type rdf:Property . \n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Label> cite:propType "string" . \n\n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Sequence>	cite:propLabel "Sequence" . \n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Sequence> rdf:type rdf:Property . \n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Sequence> cite:propType "number" . \n\n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Analysis>	cite:propLabel "Analysis" . \n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Analysis> rdf:type rdf:Property . \n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Analysis> cite:propType cite:CiteUrn  . \n\n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_AnalyzedText>	cite:propLabel "Analyzed Text" . \n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_AnalyzedText> rdf:type rdf:Property . \n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_AnalyzedText> cite:propType cite:CtsUrn  . \n\n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_ResultingText>	cite:propLabel "Resulting Text" . \n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_ResultingText> rdf:type rdf:Property . \n""")
ors.write("""<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_ResultingText> cite:propType "string" . \n\n""")



//		Important Properties

ors.write("""<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:canonicalId		<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_URN> . \n""")
ors.write("""<urn:cite:hmt:VenAIliad_classifiedTokens>	cite:orderedBy <http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Sequence> . \n\n""")



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
	ors.write("<${idUrn}> olo:item ${count} . \n\n")
	if (prevUrn != ""){
		ors.write("<${idUrn}> olo:previous <${prevUrn}> . \n")
		ors.write("<${prevUrn}> olo:next <${idUrn}> . \n\n")
	}
	ors.write("<${idUrn}> cite:analyzes <${ctsUrn}> . \n")
	ors.write("<${ctsUrn}> cite:analyzedBy <${idUrn}> . \n")
	ors.write("<${idUrn}> cite:hasAnalysis <${analysisUrn}> . \n")
	ors.write("<${analysisUrn}> cite:analysisFor <${idUrn}> . \n")
	ors.write("""<${idUrn}> cite:textResult "${ctsUrn.getSubref()}" . \n""")
	ors.write("""<${ctsUrn}> cts:isSubstringOf <${citationUrn}> .\n""")
	ors.write("""<${citationUrn}> cts:hasSubstring <${ctsUrn}> .\n""")
	ors.write("\n")

	// Write General Collection Stuff
ors.write("""<urn:cite:hmt:VenAIliad_classifiedTokens> cite:possesses <${idUrn}> .\n""")
ors.write("""<${idUrn}> cite:belongsTo <urn:cite:hmt:VenAIliad_classifiedTokens> .\n""")
ors.write("""<${idUrn}> rdf:label "Classified Token #${count}." .\n""")
ors.write("""<${idUrn}>	<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Label> "Classified Token #${count}." .\n""")
ors.write("""<${idUrn}>	<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Sequence> ${count} . \n""")
ors.write("""<${idUrn}>	<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_Analysis> <${analysisUrn}> . \n""")
ors.write("""<${idUrn}>	<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_AnalyzedText> <${ctsUrn}> . \n""") 
ors.write("""<${idUrn}>	<http://www.homermultitext.org/hmt/citedata/VenAIliad_classifiedTokens_ResultingText> "${ctsUrn.getSubref()}" . \n\n\n""") 



	// clean up
	prevUrn = idUrn
	count++

/* 
`<ORCA Object URN> cite:analyzes <passage analyzed CTS URN> .` inverse: `cite:analyzedBy`

`<ORCA Object URN> cite:hasAnalysis <analysis CITE URN> .` inverse: `cite:analysisFor` 

`<ORCA Object URN> cite:textResult "text result" .` inverse: `cite:textResultFor`
*/
			
}

ors.close()
