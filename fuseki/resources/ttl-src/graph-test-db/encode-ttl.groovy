import java.text.*

def uriString = ~/<urn:([^>]+)>/
String tempString


OutputStreamWriter osr = new OutputStreamWriter(System.out, "UTF-8")

if (args.size() < 1){
	println "Usage: groovy encode-ttl.groovy FILENAME"
} else {
	File infile = new File(args[0])
	infile.eachLine{
			if ( it =~ /^@prefix/ ){
				tempString = it
			} else {
				tempString = it.replaceAll(uriString){ fullMatch, justString ->
				return "<urn:${URLEncoder.encode(justString,'UTF-8')}>"
				}
			}
			osr.write(tempString)
			osr.write("\n")
	}
}

osr.close()

