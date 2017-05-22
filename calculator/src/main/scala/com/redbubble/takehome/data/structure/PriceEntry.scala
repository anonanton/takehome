package com.redbubble.takehome.data.structure

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}

import scala.annotation.meta.param

/**
  * A single price entry as it is loaded. A product with
  * some number of options. Options are mapper option name to
  * possible values
  *
  * @see [[com.redbubble.takehome.price.PriceTree]]
  * @author vdonets
  */
case class PriceEntry @JsonCreator()
(@JsonProperty("product-type") @param prodType: String,
 @JsonProperty @param options: Map[String, Iterable[String]],
 @JsonProperty("base-price") @param price: Double)
