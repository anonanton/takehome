package com.redbubble.takehome.cli

import com.redbubble.takehome.command.Command
import com.redbubble.takehome.util.LazyLogging

/**
  * Base trait that holds default implementations
  * of common command line client features
  *
  * @author vdonets
  */
trait CLI extends LazyLogging {

  /**
    * Commands this CLI can perform
    */
  protected val commands: Iterable[Command[_ <: CLI]]

  //Inits a conveniently accessible map of commands
  private lazy val cmds: Map[Char, Command[_ <: CLI]] = commands.map(cmd => {
    (cmd.key, cmd)
  }).toMap

  /**
    * Draws basic interface to standard out
    */
  protected def drawInterface(): Unit = {
    println("Select:")
    cmds.foreach(e => {
      println(e._1 + " - " + e._2.name)
    })
  }

  /**
    * Reads input from standard in
    *
    * @return a line of input
    */
  protected def getInput: String = {
    val s = scala.io.StdIn.readLine
    logger.debug("read input [" + s + "]")
    s
  }

  /**
    * Gets a parameter needed for a command to execute from user
    *
    * @param name name of param to get
    * @return Option with tuple with (name, value) of the needed parameter
    *         if param name is present, None otherwise
    */
  protected def getParam(name: String): Option[(String, String)] = {
    if (name != null && name.length != 0) {
      println("Provide " + name + ":")
      return Some(name, getInput)
    } else
      None

  }

  /**
    * Basic run loop that will show UI and get input
    */
  protected def runLoop(): Unit = {
    while (true) {
      drawInterface()
      val in = getInput
      if (in.length != 0 && !in.contains('\\') && !in.contains('/')) {
        val cmd = cmds.get(in.charAt(0))
        if (cmd.isDefined) {
          cmd.get.execute(
            cmd.get.paramNames().flatMap(paramName => {
              getParam(paramName)
            }).toMap
          )
        }
      }
    }
  }
}
