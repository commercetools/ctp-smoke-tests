# ctp-smoke-tests

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
or explicitly:
```
# docker run -it -e <VAR=VAL> -e <VAR=VAL> <image ID>
```

## External libraries
* [Cornichon](https://github.com/agourlay/cornichon)
* [Refined](https://github.com/fthomas/refined)
* [Pureconfig](https://github.com/pureconfig/pureconfig/)
* [SBT Native Packager](https://www.scala-sbt.org/sbt-native-packager/index.html)

