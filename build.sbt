// val coulombVersion              = "0.5.7"
// val catsEffectVersion           = "3.3.5"
// val catsTestkitScalaTestVersion = "2.1.5"
val catsVersion          = "2.7.0"
// val catsScalacheckVersion       = "0.3.1"
// val catsTimeVersion             = "0.3.4"
// val circeVersion                = "0.14.1"
// val cirisVersion                = "2.3.2"
// val clueVersion                 = "0.20.0"
// val http4sVersion               = "0.23.10"
// val http4sJdkHttpClientVersion  = "0.5.0"
// val fs2Version                  = "3.2.4"
// val kindProjectorVersion        = "0.13.2"
val lucumaCoreVersion    = "0.25.0"
val lucumaCatalogVersion = "0.10.1"
// val slf4jVersion                = "1.7.35"
// val log4catsVersion             = "2.2.0"
// val monocleVersion              = "3.1.0"
// val munitCatsEffectVersion      = "1.0.7"
// val refinedVersion              = "0.9.28"
// val grackleVersion              = "0.1.16"
// val natcchezHttp4sVersion       = "0.3.2"
// val natchezVersion              = "0.1.6"
val munitVersion         = "0.7.29"
// val disciplineMunitVersion      = "1.0.9"
// val gatlingVersion              = "3.7.4"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias(
  "fixImports",
  "; scalafix OrganizeImports; Test/scalafix OrganizeImports; scalafmtAll"
)

publish / skip := true

lazy val commonSettings = lucumaGlobalSettings ++ Seq(
  // libraryDependencies ++= Seq(
  //   "org.typelevel" %% "cats-testkit"           % catsVersion                 % "test",
  //   "org.typelevel" %% "cats-testkit-scalatest" % catsTestkitScalaTestVersion % "test"
  // ),
  Test / parallelExecution := false // tests run fine in parallel but output is nicer this way
//   scalacOptions ++= Seq(
//     "-Ymacro-annotations",
//     "-Ywarn-macros:after"
//   )
)

lazy val ags = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/ags"))
  .settings(commonSettings)
  .settings(
    name := "lucuma-ags",
    libraryDependencies ++= Seq(
      "edu.gemini"    %%% "lucuma-core"         % lucumaCoreVersion,
      "edu.gemini"    %%% "lucuma-catalog"      % lucumaCatalogVersion,
      "org.typelevel" %%% "cats-core"           % catsVersion,
      // "org.typelevel"  %% "cats-effect"              % catsEffectVersion,
      // "org.http4s"     %% "http4s-async-http-client" % http4sVersion,
      // "org.http4s"     %% "http4s-circe"             % http4sVersion,
      // "org.http4s"     %% "http4s-dsl"               % http4sVersion,
      // "io.circe"       %% "circe-literal"            % circeVersion,
      // "edu.gemini"     %% "clue-model"               % clueVersion,
      // "io.circe"       %% "circe-generic"            % circeVersion,
      // "org.tpolecat"   %% "natchez-http4s"           % natcchezHttp4sVersion,
      // "org.typelevel"  %% "log4cats-slf4j"           % log4catsVersion,
      // "com.manyangled" %% "coulomb"                  % coulombVersion,
      // "com.manyangled" %% "coulomb-cats"             % coulombVersion,
      // "org.typelevel"  %% "munit-cats-effect-3"      % munitCatsEffectVersion % Test
      "org.scalameta" %%% "munit"               % munitVersion      % Test,
      "org.scalameta" %%% "munit-scalacheck"    % munitVersion      % Test,
      "edu.gemini"    %%% "lucuma-core-testkit" % lucumaCoreVersion % Test
    )
  )
