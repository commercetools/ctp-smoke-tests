#!/bin/bash

set -euo pipefail

rm -rf test-reports && mkdir test-reports

img=gcr.io/ct-images/ctp-smoke-tests:master

docker pull $img
docker run \
  --rm \
  --mount type=bind,source="$(pwd)"/test-reports,destination=/tmp/test-reports,consistency=cached \
  -e AUTH_URL \
  -e CLIENT_ID \
  -e CLIENT_SECRET \
  -e PROJECT_KEY \
  $img
