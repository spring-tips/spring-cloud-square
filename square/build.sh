#!/usr/bin/env bash
export DEBUG=true
mvn -DskipTests=true -Pnative clean  package && ./target/square
