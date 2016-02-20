import java.text.*

def uriString = ~/<(urn:[^@]+)(@[^>]+)>/
String tempString

OutputStreamWriter osr = new OutputStreamWriter(System.out, "UTF-8")

if (args.size() < 1){
	println "Usage: groovy encode-ttl.groovy FILENAME"
} else {
	File infile = new File(args[0])
	infile.eachLine{
			if ( it =~ /^@prefix/ ){
				tempString = it
			} else if (it =~ uriString) {
				tempString = it.replaceAll(uriString){ fullMatch, urnPart, subRef ->
				return "<${urnPart}${URLEncoder.encode(subRef,'UTF-8')}>"
				}
			} else {
				tempString = it
			}
			osr.write(tempString)
			osr.write("\n")
	}
}

osr.close()

