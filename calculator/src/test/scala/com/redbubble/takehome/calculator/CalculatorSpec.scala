package com.redbubble.takehome.calculator

import com.redbubble.takehome.TestUtil
import com.redbubble.takehome.data.DataSource
import com.redbubble.takehome.data.deserializer.json.JsonDeserializer
import com.redbubble.takehome.data.schema.json.JsonSchemaValidator
import com.redbubble.takehome.data.structure.{CartItem, PriceEntry}
import com.redbubble.takehome.price.PriceTree
import com.redbubble.takehome.util.LazyLogging
import org.scalatest.FlatSpec
import org.scalatest.prop.TableDrivenPropertyChecks

/**
  * @author vdonets
  */
class CalculatorSpec extends FlatSpec with LazyLogging with TableDrivenPropertyChecks {

  private val ds = DataSource.fromFile
  private val priceValidator = new JsonSchemaValidator(TestUtil.getFilePath("test-prices.schema.json"))
  private val cartValidator = new JsonSchemaValidator(TestUtil.getFilePath("calcTest/prices.schema.json"))
  private val deserializer = new JsonDeserializer
  private val priceData = ds.load[Array[PriceEntry]](TestUtil.getFilePath("basePrices.json"))
    .validateWith(priceValidator)
    .deserializeWith(deserializer).get

  private val calculator = new Calculator(new PriceTree(priceData))

  val parameters =
    Table(
      ("input", "output"),
      ("cart-4560.json", 4560.0),
      ("cart-9363.json", 9363.0),
      ("cart-9500.json", 9500.0),
      ("cart-11356.json", 11356.0)
    )

  "Calculator" should "calculate prices for carts" in {
    forAll(parameters) {
      (input, output) => {
        logger.info("calculating price for [" + input + "] expecting [" + output + "]")
        val cart = ds.load[Array[CartItem]](TestUtil.getFilePath("calcTest/" + input))
          .validateWith(cartValidator)
          .deserializeWith(deserializer)
          .get
        val result = calculator.calculate(cart)
        logger.info("result = " + result)
        assert(result == output)
      }
    }
  }
}