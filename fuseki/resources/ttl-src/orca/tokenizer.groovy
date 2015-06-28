/*
Simple groovy script to tokenize a tab file with HmtEditorialTokenization

Usage: groovy tokenizer.groovy <TABFILE>

*/

@GrabResolver(name='beta', root='http://beta.hpcc.uh.edu/nexus/content/repositories/releases')
@Grab(group='org.homermultitext', module='hmt-utils', version='0.6.0j7')

import org.homermultitext.utils.HmtEditorialTokenization

if (args.size() != 1) {
  System.err.println "Usage: groovy tokenizer.groovy <TABFILE>"
  System.exit(-1)
}

// Inputs:
File tabs =  new File(args[0])
// Outputs:
File tokens = new File("tabs-tokenized.csv")
if (! tabs.exists()) {
  tabs.mkdir()
}
HmtEditorialTokenization toker = new HmtEditorialTokenization()
def tokenizationResults = toker.tokenizeTabFile(tabs,"#")

tokenizationResults.each { res ->
  tokensFile.append('"' + res[0] + '","' + res[1] + '"\n')
}
