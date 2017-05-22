package com.redbubble.takehome

import com.redbubble.takehome.calculator.Calculator
import com.redbubble.takehome.cli.CLI
import com.redbubble.takehome.command.{Calculate, Command, Init, Shutdown}
import com.redbubble.takehome.data.schema.SchemaValidator
import com.redbubble.takehome.util.LazyLogging
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.{CommandLineRunner, SpringApplication}
import org.springframework.context.annotation.ImportResource

import scala.collection.mutable

/**
  * The CLI implementation that will execute all calculations
  *
  */
@EnableAutoConfiguration
@ImportResource(value = Array("classpath*:applicationContext.xml"))
class CommandLineCalculator
(private val conf: CommandLineCalcConfig)
  extends CLI with CommandLineRunner with LazyLogging {

  /**
    * Prepends work directory to a file path if
    * path is not absolute.
    *
    * @param filePath file path relative to work dir or absolute path
    * @return absolute path
    */
  private[takehome] def absPath(filePath: String): String =
    if (filePath.startsWith("/"))
      return filePath
    else
      this.config(Constants.WORK_DIR) + "/" + filePath

  private[takehome] val config = mutable.HashMap(
    Constants.WORK_DIR -> System.getProperty("user.dir"),
    Constants.CART_SCHEMA -> conf.cartSchema,
    Constants.PRICE_DATA -> conf.priceData,
    Constants.PRICE_SCHEMA -> conf.priceSchema
  )

  private[takehome] var cartSchemaValidator: SchemaValidator = null
  private[takehome] var calculator: Calculator = null

  logger.info("Initialized CommandLineCalculator. Runtime config = " + config)

  override def run(strings: String*): Unit = runLoop()

  override protected val commands: Seq[Command[CommandLineCalculator]] =
    Seq(new Init(this), new Calculate(this), new Shutdown(this))

  //initially creates validator and calculator
  commands(0).execute(config.toMap)
}

object CommandLineCalculator extends LazyLogging {

  def main(args: Array[String]): Unit = {
    logger.info("Booting...")
    SpringApplication.run(classOf[CommandLineCalculator], args: _*);
  }


}

object Constants {

  val WORK_DIR = "work directory"

  val PRICE_SCHEMA = "price schema"
  val PRICE_DATA = "price data"

  val CART_DATA = "cart data"
  val CART_SCHEMA: String = "cart schema"
}
