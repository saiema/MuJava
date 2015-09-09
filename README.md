muJava++
======

An extension of [muJava](https://github.com/tradi/MuJava) : A Mutation Testing Tool for Java

**How to use it**

    java -cp <paths to jar files inside lib folder>:<mujava++.jar path>:<proyect's bin path>:[<tests bin path>] mujava.app.Console [PATH TO CUSTOM .PROPERTIES FILE]

* **_JUnit is not included, you should have installed in your system_**
* **_To list all mutation operators and their information call mujava with --args -h as parameters_**

**Suggested VM Arguments**

    -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled

**Usage example**

* Create a folder (in this case we will use mjexample)
* Download [mujava.jar](https://github.com/saiema/MuJava/releases/download/1.5/mujava.jar) inside mjexample
* Download and extract [lib.zip](https://github.com/saiema/MuJava/releases/download/1.5/lib.zip) inside mjexample
* Download and extract [properties.rar](https://github.com/saiema/MuJava/releases/download/1.5/properties.rar) inside mjexample
* Download and extract [test.rar](https://github.com/saiema/MuJava/releases/download/1.3/test.rar)

You should end with the following folder structure

* mjexample
  * mujava.jar
  * lib/
  * test/
  * properties/

Inside test folder you should have

* utils
  * BooleanOps.java
* mutationScore
  * BooleanOpsXorTests.java
  * BooleanOpsXnorTests.java
  * BooleanOpsOrTests.java
  * BooleanOpsAndTests.java

Go to the test folder and run **_javac utils/*.java_** and **_javac -cp <path to junit> mutationScore/*.java_**

Now from the folder mjexample you can run

_java -cp mujava.jar:\<paths to each jar inside lib/\*\>:\<path to junit.jar\>:test/ mujava.app.Main \[ARGS]>_

where **ARGS** can be one of the following:

\-p[roperties] : to give a specific .properties file to mujava++

\-o[perators] : prints all mutation operators information

\-h[elp] : prints mujava++ help

\* : except from javadoc jars

For further information visit the [wiki](https://github.com/saiema/MuJava/wiki/muJavapp).
