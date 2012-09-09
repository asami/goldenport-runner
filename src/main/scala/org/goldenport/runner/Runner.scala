package org.goldenport.runner

import java.io.File
import java.net.URL
import java.net.URLClassLoader
import scalaz._, Scalaz._
import scalax.file._
import org.goldenport.sexpr._
import org.goldenport.sexpr.SExpr._

class Runner(val args: Array[String]) {
    def run() {
    _parse_option(args.toList) match {
      case home :: main :: appargs => _run(home, main, appargs.toArray)
      case _ => _usage
    }
  }

  private def _usage {
    println("usage: grun directory classname")
  }

  private def _run(home: String, main: String, args: Array[String]) {
    val homedir = Path(home)
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

  // http://d.hatena.ne.jp/tototoshi/20110518/1305737939
  private def _parse_option(args: List[String]): List[String] = {
    args
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
