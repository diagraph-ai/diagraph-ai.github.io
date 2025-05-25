enablePlugins(ScalaJSPlugin)

name := "diagraph-ai"
scalaVersion := "3.5.1"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.8.0"
libraryDependencies += "com.lihaoyi" %%% "upickle" % "3.1.0"
libraryDependencies += "org.scala-lang" %%% "toolkit" % "0.1.7"


// Add support for the DOM in `run` and `test`
jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()

// uTest settings
libraryDependencies += "com.lihaoyi" %%% "utest" % "0.8.2" % "test"
testFrameworks += new TestFramework("utest.runner.Framework")
