import sbt._

object Dependencies {

  lazy val smokeTests =
      "com.github.agourlay"   %% "cornichon-test-framework" % "0.19.6" ::
      "eu.timepit"            %% "refined"                  % "0.9.22" ::
      "eu.timepit"            %% "refined-pureconfig"       % "0.9.22" ::
      "com.github.pureconfig" %% "pureconfig"               % "0.14.0" ::
      "com.github.pureconfig" %% "pureconfig"               % "0.14.0" ::
      Nil map (_ % Test)
}
