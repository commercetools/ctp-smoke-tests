import sbt._

object Dependencies {

  lazy val smokeTests =
      "com.github.agourlay"   %% "cornichon-test-framework" % "0.19.3" ::
      "eu.timepit"            %% "refined"                  % "0.9.15" ::
      "eu.timepit"            %% "refined-pureconfig"       % "0.9.15" ::
      "com.github.pureconfig" %% "pureconfig"               % "0.12.3" ::
      Nil map (_ % Test)
}
