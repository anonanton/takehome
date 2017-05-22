package com.redbubble.takehome.command

import java.io.FileNotFoundException

import com.redbubble.takehome.data.DataSource
import com.redbubble.takehome.data.deserializer.json.JsonDeserializer
import com.redbubble.takehome.data.structure.CartItem
import com.redbubble.takehome.util.LazyLogging
import com.redbubble.takehome.{CommandLineCalculator, Constants}

/**
  * Command to calculate price of a cart
  * @param cli the CLI that will execute this command
  */
class Calculate(cli: CommandLineCalculator)
  extends Command(cli) with LazyLogging {
  /**
    * Names of parameters this command needs
    *
    * @return
    */
  override def paramNames(): Seq[String] = Seq(Constants.CART_DATA)

  /**
    * Key this command should be retrieved by
    *
    * @return
    */
  override def key: Char = '2'

  /**
    * Names of this command
    */
  override val name: String = "Calculate"

  /**
    * Calculates a cart
    *
    * @param params cart data
    */
  override def execute(params: Map[String, String]): Unit = {
    logger.info("Executing calculate with " + params)
    try {
      if (cli.calculator == null) {
        logger.error("Calculator not initialized")
        println("Must initialize calculator first")
      } else {
        println(cli.calculator.calculate({
          val builder = ds.load[Array[CartItem]](cli.absPath(params(Constants.CART_DATA)))
            .deserializeWith(deserializer)
          if (cli.cartSchemaValidator != null) {
            builder.validateWith(cli.cartSchemaValidator)
          } else
            logger.warn("No cart schema validator set")
          builder.get
        }))
      }
    } catch {
      case t: Throwable => t.printStackTrace()
    }
  }

  private val ds = DataSource.fromFile
  private val deserializer = new JsonDeserializer
}
