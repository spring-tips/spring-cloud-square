#!/usr/bin/env bash

mvn -DskipTests=true -Pnative package && ./target/square
