#!/bin/bash

MGR_PATH='/cite/mgr/'; # Edit path to match local installation of CITE Manager!!
TTL_PATH='/cite/mgr/build/ttl/all.ttl';
CONF_PATH="/cite/cs2/fuseki/resources/ttl-src/integrated-reduced/hmt.gradle";
echo $CONF_PATH;
 
cd $MGR_PATH 
gradle clean;
gradle -Pconf=$CONF_PATH ttl;
cd -
rm ../../test-all.ttl
cat $TTL_PATH ttl-additions/*.ttl > ../../test-all.ttl;

echo "Done. TTL file:";
wc -l ../../test-all.ttl;




