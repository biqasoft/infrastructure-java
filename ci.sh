#!/usr/bin/env bash

# require maven, xmlstarlet
# linux: apt-get install xmlstarlet
# windows: https://freefr.dl.sourceforge.net/project/xmlstar/xmlstarlet/1.6.1/xmlstarlet-1.6.1-win32.zip

set -e
# set version of base module
mvn install -f base/pom.xml org.codehaus.mojo:versions-maven-plugin:2.3:set -DnewVersion=${PROJECT_VERSION}

BASE_PATH=`pwd`

# hacks to build all
if [[ $1 == "dev" ]]
    then
        mvn clean install -f ../microservice-communicator -DskipTests=true -Dgpg.skip=true
        mvn clean install -f ../bindings-java/authmicroservicecommunication
        mvn clean install -f ../bindings-java/auth-context
fi

# set parent version of each module to the latest base module
find . -name 'pom.xml' | while read line; do

# do not edit version of base pom
    if [[ $line != "./base/pom.xml" ]]
        then
            xmlstarlet ed --inplace -N p=http://maven.apache.org/POM/4.0.0 -u "/p:project/p:parent/p:version" -v ${PROJECT_VERSION} ${line}
            echo "Processing module '$line'"
        fi
done

# build all
if [[ $1 == "dev" ]]
   then
       mvn clean install
fi