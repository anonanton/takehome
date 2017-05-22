package com.redbubble.takehome.data.structure

import com.fasterxml.jackson.annotation.JsonProperty

import scala.annotation.meta.param

/**
  * A single entry in the cart containing a single product
  * with a single set of options
  *
  * @param prodType type of product
  * @param options  options for the product
  * @param markup   artist mark up - percentage
  * @param quantity quantity of products with options (multiplier)
  * @see [[com.redbubble.takehome.calculator.Calculator]]
  * @author vdonets
  */
case class CartItem
(@JsonProperty("product-type") @param prodType: String,
 @JsonProperty @param options: Map[String, String],
 @JsonProperty("artist-markup") @param markup: Double,
 @JsonProperty("quantity") @param quantity: Int)
