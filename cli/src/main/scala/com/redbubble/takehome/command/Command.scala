package com.redbubble.takehome.command

import com.redbubble.takehome.cli.CLI

/**
  * Base trait for any type of command.
  *
  * @param cli the CLI that will execute this command
  * @tparam T concrete type of CLI
  */
abstract class Command[T <: CLI](protected val cli: T) {

  /**
    * Names of parameters this command needs
    *
    * @return
    */
  def paramNames(): Seq[String]

  /**
    * Key this command should be retrieved by
    *
    * @return
    */
  def key: Char

  /**
    * Names of this command
    */
  val name: String

  /**
    * What it actually does
    *
    * @param params what ever params this command needs
    */
  def execute(params: Map[String, String]): Unit

}
