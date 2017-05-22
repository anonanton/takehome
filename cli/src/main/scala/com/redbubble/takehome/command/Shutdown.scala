package com.redbubble.takehome.command

import com.redbubble.takehome.CommandLineCalculator

/**
  * Shuts down the application
  *
  * @param cli not actually needed
  */
class Shutdown(cli: CommandLineCalculator) extends Command(cli) {
  /**
    * Names of parameters this command needs
    *
    * @return
    */
  override def paramNames(): Seq[String] = Seq()

  /**
    * Key this command should be retrieved by
    *
    * @return
    */
  override def key: Char = '0'

  /**
    * Names of this command
    */
  override val name: String = "Exit"

  /**
    * What it actually does
    *
    * @param params what ever params this command needs
    */
  override def execute(params: Map[String, String]): Unit = System.exit(0)
}
