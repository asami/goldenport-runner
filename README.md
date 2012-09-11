goldenport-runner
=================

A goldenport-runner is a command line launcher to execute a application
in a sbt project directory.

You can use the application under development as ordinary program.

Moreover, you can also use the program developed using sbt like a script.

Installation
------------

You can install goldenport-runner to download a jar file shown below and copy a library directory.

- [download](https://github.com/downloads/asami/goldenport-runner/goldenport-runner_2.9.2-0.1.0-one-jar.jar)

You can also get the jar file from the source.

    $ git clone https://github.com/asami/goldenport-runner.git
    $ cd goldenport-runner
    $ sbt one-jar

You can get the goldenport-runner as target/scala-2.9.2/goldenport-runner_2.9.2-[version]-one-jar.jar

Usage
-----

First you must produce an [ensime](https://github.com/aemoncannon/ensime) configuration file .ensime for your sbt project:

    $ cd ~/src/mycommand
    $ sbt "ensime project"

You can execute the goldenport-runner using the java command directly:

    $ java -jar goldenport-runner_2.9.2-0.1.0-one-jar.jar ~/src/mycommand com.example.mycommand.Main args

It's useful to create a short script in your bin directory like below:

```sh
#! /bin/sh

JAVA_OPTS="-Xmx512m -XX:MaxPermSize=256m -Dfile.encoding=UTF-8"

java $JAVA_OPTS -jar $HOME/lib/goldenport-runner_2.9.2-0.1.0-one-jar.jar $HOME/src/mycommand com.example.mycommand.Main "$@"
```
