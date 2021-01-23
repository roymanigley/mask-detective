#!/bin/bash
mvn -f tools/load-test clean gatling:test \
    -Dtarget.host=localhost \
    -Dtarget.port=8080 \
    -Dtest.total-users=50 \
    -Dtest.time-frame=1
