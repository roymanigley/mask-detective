#!/bin/bash
mvn -f tools/load-test gatling:test \
    -Dtarget.host=localhost \
    -Dtarget.port=8080 \
    -Dtest.total-users=1 \
    -Dtest.time-frame=1
