#!/usr/bin/env bash

echo "Starting bot..."
cd "$(dirname "$0")/../.." || exit 1
exec ./mvnw -q -pl examples/jetty-webhook-echobot -am exec:java -Dexec.args="$*"
