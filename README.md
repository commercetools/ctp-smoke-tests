# ctp-smoke-tests

[![CircleCI](https://circleci.com/gh/commercetools/ctp-smoke-tests.svg?style=svg)](https://circleci.com/gh/commercetools/ctp-smoke-tests)

## How to run the smoke tests

The following environment variables have to be defined to run the tests:

```
$ AUTH_URL=<auth url>
$ API_URL=<api url>
$ CLIENT_ID=<client id>
$ CLIENT_SECRET=<client secret>
$ PROJECT_KEY=<project key>
```

Run the smoke tests as docker container:

```
$ docker run -it --rm -e AUTH_URL -e API_URL -e CLIENT_ID -e CLIENT_SECRET -e PROJECT_KEY gcr.io/ct-images/ctp-smoke-tests:master
```

## Docker set up (sbt-native-packager)

### Create a local docker image:

```
# sbt docker:publishLocal
```

### Publishing a docker image

We publish images to the [public CTP docker registry on google cloud](https://console.cloud.google.com/gcr/images/ct-images/GLOBAL/ctp-smoke-tests).

#### Automatic publishing

The CI pipeline automatically publishes a docker image for each PR and for master.

Docker images are tagged with the git commit and the git branch.

The CI pipeline uses [this account](https://github.com/commercetools/ops-terraform/blob/master/ct-images/users-manager/service-accounts/ctp_smoke_tests.tf).

#### Manually publish an image

To manually publish an image, we need permissions to do so.

Those permissions are managed in [ops-terraform](https://github.com/commercetools/ops-terraform/blob/master/ct-images/container-registry/main.tf).

```
# set up permissions to push to the goole repository (has to be done only once)
$ gcloud auth configure-docker

$ sbt docker:publish
```

## External libraries

* [Cornichon](https://github.com/agourlay/cornichon)
* [Refined](https://github.com/fthomas/refined)
* [Pureconfig](https://github.com/pureconfig/pureconfig/)
* [SBT Native Packager](https://www.scala-sbt.org/sbt-native-packager/index.html)

