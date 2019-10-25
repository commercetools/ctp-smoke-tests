# ctp-smoke-tests

[![CircleCI](https://circleci.com/gh/commercetools/ctp-smoke-tests.svg?style=svg)](https://circleci.com/gh/commercetools/ctp-smoke-tests)

## Prerequisites
The following environment variables have to be defined to run the tests:

```
auth-url: ${?AUTH_URL}
client-id: ${CLIENT_ID}
client-secret: ${CLIENT_SECRET}
project-key: ${PROJECT_KEY}
```
## Docker set up (sbt-native-packager)
Create a local docker image:
```
# sbt docker:publishLocal
```
Run the image with the environment variables defined either in a file:
```
# docker run -it --env-file <.env file> <image ID>
```
or explicitly, with variables defined in a local environment:
```
# AUTH_URL=<auth url>
# CLIENT_ID=<client id>
# CLIENT_SECRET=<client secret>
# PROJECT_KEY=<project key>
# docker run -it -e AUTH_URL -e CLIENT_ID -e CLIENT_SECRET -e PROJECT_KEY <image ID>
```

## External libraries
* [Cornichon](https://github.com/agourlay/cornichon)
* [Refined](https://github.com/fthomas/refined)
* [Pureconfig](https://github.com/pureconfig/pureconfig/)
* [SBT Native Packager](https://www.scala-sbt.org/sbt-native-packager/index.html)

