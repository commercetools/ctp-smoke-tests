ThisBuild / scalaVersion := "2.13.12"
ThisBuild / version := git.gitHeadCommit.value.getOrElse("1.0")
ThisBuild / organization := "com.commercetools"
ThisBuild / organizationName := "commercetools"
Test / parallelExecution := false
import Dependencies._
import NativePackagerHelper._
import com.typesafe.sbt.packager.Keys.dockerBaseImage

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    name := "ctp-smoke-tests",
    libraryDependencies ++= smokeTests,
    testFrameworks += new TestFramework("com.github.agourlay.cornichon.framework.CornichonFramework"),
    scalacOptions += "-Xmacro-settings:materialize-derivations",
    Compile / mainClass := Some("com.github.agourlay.cornichon.framework.MainRunner"),
    scriptClasspath ++= {
      fromClasspath((Test / managedClasspath).value, ".", _ => true).map(_._2) :+
        (Test / sbt.Keys.`package`).value.getName
    },
    Universal / mappings ++= {
      val testJar = (Test / sbt.Keys.`package`).value
      val func = testJar -> s"lib/${testJar.getName}"
      fromClasspath((Test / managedClasspath).value, "lib", _ => true) :+
        (testJar -> s"lib/${testJar.getName}")
    },
    noPackageDoc,
    dockerCmd := Seq(
      "--packageToScan=cornichonTests",
      "--reportsOutputDir=/tmp/test-reports"
    ),
    dockerPublishingSettings
  )

//skip javadoc.jar build for performance
lazy val noPackageDoc = Seq(Compile / mappings := Seq(), packageDoc / mappings := Seq())

// docker publishing to public repo: https://console.cloud.google.com/gcr/images/ct-images
// needs a docker login to the GCE container registry first
lazy val dockerPublishingSettings = Seq(
  // https://hub.docker.com/_/eclipse-temurin/
  dockerBaseImage := "eclipse-temurin:18.0.2.1_1-jre-jammy",
  dockerRepository := Some("gcr.io/ct-images"),
  dockerUpdateLatest := false
)
