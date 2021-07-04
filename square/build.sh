#!/usr/bin/env bash
# export DEBUG=true
# shellcheck disable=SC2046
START=$( cd $(dirname $0) && pwd )
echo "starting in $START"
mkdir -p ${START}/target/classes/META-INF/native-image
mvn -DskipTests=true -Pnative -f $START/pom.xml clean  package && $START/target/square
