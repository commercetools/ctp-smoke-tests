import sbt._

object Dependencies {

  lazy val smokeTests =
      "com.github.agourlay"   %% "cornichon-test-framework" % "0.19.5" ::
      "eu.timepit"            %% "refined"                  % "0.9.16" ::
      "eu.timepit"            %% "refined-pureconfig"       % "0.9.16" ::
      "com.github.pureconfig" %% "pureconfig"               % "0.14.0" ::
      "com.github.pureconfig" %% "pureconfig"               % "0.13.0" ::
      Nil map (_ % Test)
}
