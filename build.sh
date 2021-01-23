#!/bin/bash

mvn -f services/image-uploader compile jib:dockerBuild && \
mvn -f services/face-finder compile jib:dockerBuild && \
mvn -f services/mouth-finder compile jib:dockerBuild && \
mvn -f services/nose-finder compile jib:dockerBuild && \
mvn -f services/mask-detection-notifier compile jib:dockerBuild && \
# mvn -f tools/client clean package && \
docker-compose -f docker/docker-compose.yml up -d
# mvn -f tools/client clean package && \
# docker-compose -f docker/docker-compose.yml scale mouth-finder-app=3
# docker-compose -f docker/docker-compose.yml scale nose-finder-app=2
# docker-compose -f docker/docker-compose.yml scale face-finder-app=5
