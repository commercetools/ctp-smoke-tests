ThisBuild / scalaVersion     := "2.12.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "authTest",
    libraryDependencies ++= (
      "com.github.agourlay"   %% "cornichon-test-framework" % "0.18.1" ::
      "eu.timepit"            %% "refined"                  % "0.9.10" ::
      "com.github.pureconfig" %% "pureconfig"               % "0.12.0" ::
      Nil
      ) map ( _ % Test))
    testFrameworks += new TestFramework ("com.github.agourlay.cornichon.framework.CornichonFramework")

