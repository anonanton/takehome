package com.redbubble.takehome.calculator

import com.redbubble.takehome.TestUtil
import com.redbubble.takehome.data.DataSource
import com.redbubble.takehome.data.deserializer.json.JsonDeserializer
import com.redbubble.takehome.data.schema.json.JsonSchemaValidator
import com.redbubble.takehome.data.structure.PriceEntry
import com.redbubble.takehome.exception.{PricesCollisionException, SchemaValidationException}
import com.redbubble.takehome.price.PriceTree
import com.redbubble.takehome.util.LazyLogging
import org.scalatest.FlatSpec

/**
  * @author vdonets
  */
class PriceTreeFailureSpec extends FlatSpec with LazyLogging {

  private val ds = DataSource.fromFile
  private val validator = new JsonSchemaValidator(TestUtil.getFilePath("test-prices.schema.json"))
  private val deserializer = new JsonDeserializer

  "PriceTree" should "fail when there are duplicate prices" in {
    assertThrows[PricesCollisionException](
      new PriceTree(
        ds.load[Array[PriceEntry]](TestUtil.getFilePath("treeTest/prices-collision.json"))
          .validateWith(validator)
          .deserializeWith(deserializer).get))


  }

  it should "fail when there are invalid price entries" in {
    assertThrows[SchemaValidationException](
      new PriceTree(
        ds.load[Array[PriceEntry]](TestUtil.getFilePath("treeTest/prices-invalid.json"))
          .validateWith(validator)
          .deserializeWith(deserializer).get)
    )


  }

}