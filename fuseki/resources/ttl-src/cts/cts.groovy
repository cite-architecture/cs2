/*
 Illustrates how to use grapes to grab all dependencies need to convert
 XML texts of Greek like ancient Perseus texts from beta code representation
 of Greek to the polytonic Greek range of Unicode in UTF-8.


 You should be able to run "groovy betaToUtf8Xml.groovy <FILENAME>" unaltered on 
 any system that has groovy installed to write to standard output a UTF-8
 version of the XML file <FILENAME>.

 */

// 
@GrabResolver(name='beta', root='http://beta.hpcc.uh.edu/nexus/content/repositories/releases')
@Grab(group='edu.holycross.shot', module='hocuspocus', version='0.13.1')

import edu.holycross.shot.hocuspocus.Corpus

if (args.size() != 3) {
  System.err.println "Usage: groovy cts.groovy <INVENTORY> <ARCHIVEROOT> <SCHEMA>"
  System.exit(-1)
}

File inv =  new File(args[0])
File textArchive = new File(args[1])
File schema = new File(args[2])

System.out.println "Found local files."

