ThisBuild / scalaVersion     := "2.12.10"
ThisBuild / version          := git.gitHeadCommit.value.getOrElse("1.0")
ThisBuild / organization     := "com.commercetools"
ThisBuild / organizationName := "commercetools"

import Dependencies._
import NativePackagerHelper._

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    name                 := "ctp-smoke-tests",
    libraryDependencies  ++= smokeTests,
    testFrameworks += new TestFramework("com.github.agourlay.cornichon.framework.CornichonFramework"),
    resolvers            += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    scalacOptions        += "-Xmacro-settings:materialize-derivations",
    mainClass in Compile := Some("com.github.agourlay.cornichon.framework.MainRunner"),

    scriptClasspath ++= {
      fromClasspath((managedClasspath in Test).value, ".", _ => true).map(_._2) :+
        (sbt.Keys.`package` in Test).value.getName
    },

    mappings in Universal ++= {
      val testJar = (sbt.Keys.`package` in Test).value
      val func = testJar -> s"lib/${testJar.getName}"
      fromClasspath((managedClasspath in Test).value, "lib", _ => true) :+
        (testJar -> s"lib/${testJar.getName}")
    },

    noPackageDoc,
    dockerCmd := Seq(
      "--packageToScan=cornichonTests",
      "--reportsOutputDir=/target/test-reports"
    ),
    dockerPublishingSettings
  )

//skip javadoc.jar build for performance
lazy val noPackageDoc = Seq(mappings in (Compile, packageDoc) := Seq())

// docker publishing to public repo: https://console.cloud.google.com/gcr/images/ct-images
// needs a docker login to the GCE container registry first
lazy val dockerPublishingSettings = Seq(
  dockerRepository     := Some("gcr.io/ct-images"),
  dockerUpdateLatest   := true
)
