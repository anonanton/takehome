package com.redbubble.takehome.calculator

import com.redbubble.takehome.TestUtil
import com.redbubble.takehome.data.DataSource
import com.redbubble.takehome.data.deserializer.json.JsonDeserializer
import com.redbubble.takehome.data.schema.json.JsonSchemaValidator
import com.redbubble.takehome.data.structure.PriceEntry
import com.redbubble.takehome.exception.{InvalidOptionsException, InvalidProductTypeException}
import com.redbubble.takehome.price.PriceTree
import com.redbubble.takehome.util.LazyLogging
import org.scalatest.FlatSpec

/**
  * @author vdonets
  */
class PriceTreeSpec extends FlatSpec with LazyLogging {

  private val ds = DataSource.fromFile
  private val validator = new JsonSchemaValidator(TestUtil.getFilePath("test-prices.schema.json"))
  private val deserializer = new JsonDeserializer
  private val priceData = ds.load[Array[PriceEntry]](TestUtil.getFilePath("treeTest/simplePrices.json"))
    .validateWith(validator)
    .deserializeWith(deserializer).get

  private val tree = new PriceTree(priceData)
  "PriceTree" should "retrieve simple prices" in {
    val price1 = tree.getPrice("hoodie",
      Map("size" -> "small", "colour" -> "white",
        "print" -> "front", "producer" -> "myCompany"))
    assert(price1 == 100)

    val price2 = tree.getPrice("hoodie",
      Map("size" -> "small", "colour" -> "dark",
        "print" -> "front", "producer" -> "myCompany"))
    assert(price2 == 100)

    val price3 = tree.getPrice("hoodie",
      Map("size" -> "large", "colour" -> "white",
        "print" -> "front", "producer" -> "anotherCompany"))
    assert(price3 == 200)
  }

  it should "fail to retrieve unknown product" in {
    assertThrows[InvalidProductTypeException](tree.getPrice("pants", Map("abc" -> "123")))
    assertThrows[InvalidProductTypeException](tree.getPrice("sticker", Map("abc" -> "123")))

  }

  it should "fail to retrieve bad options" in {
    assertThrows[InvalidOptionsException](
      tree.getPrice("hoodie",
        Map("size" -> "someBadSize", "colour" -> "white",
          "print" -> "front", "producer" -> "badArgument"))
    )

    assertThrows[InvalidOptionsException](
      tree.getPrice("hoodie",
        Map("size" -> "large", "colour" -> "white",
          "print" -> "front", "producer" -> "badArgument"))
    )

    assertThrows[InvalidOptionsException](
      tree.getPrice("hoodie",
        Map("size" -> "large", "colour" -> "non-existent",
          "print" -> "front", "producer" -> "myCompany"))
    )
  }

  it should "fail to retrieve with missing options" in {
    assertThrows[InvalidOptionsException](
      tree.getPrice("hoodie",
        Map("size" -> "large", "colour" -> "white",
          "print" -> "front"))
    )

    assertThrows[InvalidOptionsException](
      tree.getPrice("hoodie",
        Map("colour" -> "white",
          "print" -> "front", "producer" -> "myCompany"))
    )

    assertThrows[InvalidOptionsException](
      tree.getPrice("hoodie",
        Map("size" -> "large",
          "print" -> "front", "producer" -> "myCompany"))
    )
  }

}