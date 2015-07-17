#!/bin/bash

MGR_PATH='/cite/mgr/'; # Edit path to match local installation of CITE Manager!!
TTL_PATH='/cite/mgr/build/ttl/all.ttl';
CONF_PATH="/cite/cs2/fuseki/resources/ttl-src/integrated/hmt.gradle";
echo $CONF_PATH;
 
cd $MGR_PATH gradle clean;
gradle -Pconf=$CONF_PATH ttl;
cat $TTL_PATH ttl-additions/*.ttl > test-all.ttl;
cd -;

echo "Done. TTL file:";
wc -l test-all.ttl;




