package com.redbubble.takehome.command

import com.redbubble.takehome.calculator.Calculator
import com.redbubble.takehome.data.DataSource
import com.redbubble.takehome.data.deserializer.json.JsonDeserializer
import com.redbubble.takehome.data.schema.json.JsonSchemaValidator
import com.redbubble.takehome.data.structure.PriceEntry
import com.redbubble.takehome.price.PriceTree
import com.redbubble.takehome.util.LazyLogging
import com.redbubble.takehome.{CommandLineCalculator, Constants}

/**
  * Inits underlying CommandLineCalculator
  *
  * @param cli the CLI that will execute this command
  */
class Init(cli: CommandLineCalculator)
  extends Command[CommandLineCalculator](cli) with LazyLogging {

  override def key: Char = '1'

  override val name: String = "Configure"

  /**
    * Initializes the underlying CommandLineCalculator
    *
    * @param params work dir, price data path, price schema path
    */
  override def execute(params: Map[String, String]): Unit = {
    logger.info("Executing init with " + params)
    try {
      paramNames().foreach(param => {
        val value = params.get(param)
        if (value.isDefined && value.get.length != 0) {
          logger.debug("Setting [" + param + "] to [" + value.get + "]")
          cli.config.put(param, value.get)
        }
      })
      logger.debug("config = " + cli.config)
      cli.cartSchemaValidator = new JsonSchemaValidator(
        cli.absPath(cli.config(Constants.CART_SCHEMA)))
      cli.calculator = new Calculator(new PriceTree(
        DataSource.fromFile.load[Array[PriceEntry]](cli.absPath(cli.config(Constants.PRICE_DATA)))
          .validateWith(new JsonSchemaValidator(cli.absPath(cli.config(Constants.PRICE_SCHEMA))))
          .deserializeWith(new JsonDeserializer).get
      ))

    } catch {
      case t: Throwable => t.printStackTrace()
    }
  }

  override def paramNames(): Seq[String] = Seq(Constants.WORK_DIR, Constants.PRICE_SCHEMA,
    Constants.PRICE_DATA, Constants.CART_SCHEMA)
}
