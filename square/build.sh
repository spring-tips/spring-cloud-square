#!/usr/bin/env bash
export DEBUG=true
mkdir -p target/classes/META-INF/native-image
#mvn -X -DskipTests=true -Pnative clean  package && ./target/square
mvn   -DskipTests=true -Pnative clean  package && ./target/square
