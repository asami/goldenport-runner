package org.goldenport.runner

import java.io.File
import java.net.URL
import java.net.URLClassLoader
import scalaz._, Scalaz._
import scalax.file._
import org.goldenport.sexpr._
import org.goldenport.sexpr.SExpr._

object Runner {
  def main(args: Array[String]) {
    val home = args(0)
    val main = args(1)
    val appargs = args.drop(2)

    val homedir = Path(home)
    val classpath = homedir / ".ensime"
    val ensime = SExprParser(classpath.string)
    val cp = getClasspath(ensime.get)
    val cl = new URLClassLoader(cp.map(x => new File(x).toURI.toURL).toArray)
    println(cl.getURLs.toList)
    val klass = cl.loadClass(main)
    println(klass)
    println(appargs)
    val method = klass.getMethod("main", args.getClass)
    method.invoke(null, appargs)
  }

  def getClasspath(s: SExpr) = {
    val subprojects = getKeyword[List[SExpr]](s, "subprojects")
    val prj = subprojects.get(0)
    val deps = getKeyword[List[String]](prj, "runtime-deps")
    println(deps)
    println(getKeyword[SExpr](prj, "package"))
    val target = getKeyword[String](prj, "target")
    println(target)
    val mainClass = getKeyword[String](prj, "main-class")
    println(mainClass)
    target.orEmpty[List] ::: ~deps
  }
}
