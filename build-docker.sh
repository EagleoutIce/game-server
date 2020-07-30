#!/usr/bin/env bash
mvn clean install -DskipTests -Dmaven.javadoc.skip=true
docker build -t server020 .