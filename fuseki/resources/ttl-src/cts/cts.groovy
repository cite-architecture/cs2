/*
Simple groovy script to convert a CTS repository of local XML files to :

1. tabular representation in structured text files 
2. graph representation in RDF (TTL)

Usage: groovy cts.groovy <INVENTORY> <ARCHIVEROOT> <SCHEMA>

where INVENTORY is a CTS TextInventory file, ARCHIVEROOT is the root directory
where XML editions are stored, and SCHEMA is the Relax NG schema for validating
the TextInventory.

Run this in a directory where you have write permission; tabular text files
will be put a in a directory named "cts-tabulated" which will be created if it does
not exist.  RDF output will be in a file named "cts.ttl" in the current directory.


*/

@GrabResolver(name='beta', root='http://beta.hpcc.uh.edu/nexus/content/repositories/releases')
@Grab(group='edu.holycross.shot', module='hocuspocus', version='0.13.5')

import edu.holycross.shot.hocuspocus.Corpus

if (args.size() != 3) {
  System.err.println "Usage: groovy cts.groovy <INVENTORY> <ARCHIVEROOT> <SCHEMA>"
  System.exit(-1)
}

// Inputs:
File inv =  new File(args[0])
File textArchive = new File(args[1])
File schema = new File(args[2])

// Outputs:
File tabs = new File("cts-tabulated")
if (! tabs.exists()) {
  tabs.mkdir()
}
File ttl = new File("cts.ttl")

Corpus c = new Corpus(inv, textArchive, schema)
c.ttl(ttl, true, tabs)