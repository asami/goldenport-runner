package org.goldenport.runner

import java.io.File
import java.net.URL
import java.net.URLClassLoader
import scalaz._, Scalaz._
import scalax.file._
import org.goldenport.sexpr._
import org.goldenport.sexpr.SExpr._

/*
 * @since   Sep.  9, 2012
 * @version Sep. 12, 2012
 * @author  ASAMI, Tomoharu
 */
class Runner(val args: Array[String]) {
  val version = "0.1.0"
  val build = "20120912"
  var verbose: Boolean = false

  def run() {
    try {
      _run()
    } catch {
      case e: java.io.IOException => _error(e.getMessage)
      case e: java.lang.ClassNotFoundException => _error(e.getMessage + " (Class not found)")
    }
  }

  private def _usage {
    println("""usage: java $JAVA_OPTS -jar goldenport-runner_2.9.2-${version}-one-jar.jar directory classname args...

It's useful to create a short script in your bin directory like below:
---
#! /bin/sh

JAVA_OPTS="-Xmx512m -XX:MaxPermSize=256m -Dfile.encoding=UTF-8"

java $JAVA_OPTS -jar $HOME/lib/goldenport-runner_2.9.2-${version}-one-jar.jar $HOME/src/mycommand com.example.mycommand.Main "$@"
---""".replace("${version}", version))
  }

  private def _run() {
    _parse_options(args.toList) match {
      case home :: main :: appargs => _run(home, main, appargs.toArray)
      case _ => _usage
    }
  }

  private def _run(home: String, main: String, args: Array[String]) {
    val homedir = Path.fromString(home)
    val classpath = homedir / ".ensime"
    val ensime = SExprParser(classpath.string)
    val cp = _classpath(ensime.get)
    val cl = new URLClassLoader(cp.map(x => new File(x).toURI.toURL).toArray)
    val klass = cl.loadClass(main)
    val method = klass.getMethod("main", args.getClass)
    method.invoke(null, args)
  }

  private def _classpath(s: SExpr) = {
    val subprojects = getKeyword[List[SExpr]](s, "subprojects")
    val prj = subprojects.get(0)
    val deps = getKeyword[List[String]](prj, "runtime-deps")
    val target = getKeyword[String](prj, "target")
    val mainClass = getKeyword[String](prj, "main-class")
    target.orEmpty[List] ::: ~deps
  }

  private def _error(msg: String) {
    Console.err.println(msg)
  }

  private def _parse_options(args: List[String]): List[String] = {
    _parse_options(args, Nil)
  }

  private def _parse_options(in: List[String], out: List[String]): List[String] = {
    in match {
      case Nil => out
      case "-verbose" :: rest => {
        verbose = true
        _parse_options(rest, out)
      }
      case "-dir" :: arg :: rest => {
//        directory = arg.some
        _parse_options(rest, out)
      }
      case arg :: rest => _parse_options(rest, out :+ arg)
    }
  }
}

object Main {
  def main(args: Array[String]) {
    val runner = new Runner(args)
    runner.run()
  }
}

class AppMain extends xsbti.AppMain {
  def run(config: xsbti.AppConfiguration) = {
    val runner = new Runner(config.arguments)
    runner.run()
    new xsbti.Exit {
      val code = 0
    }
  }    
}
