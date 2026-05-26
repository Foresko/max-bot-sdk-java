#!/usr/bin/env bash

echo "Starting bot..."
cd "$(dirname "$0")/../.." || exit 1
exec ./mvnw -q -pl examples/tomcat-webhook-echobot -am exec:java -Dexec.args="$*"
