package com.redbubble.takehome.calculator.pricecalc

import com.redbubble.takehome.data.structure.CartItem

/**
  * Base trait for a price function a calculator can use
  * to calculate total cart price. A PriceFunction implementation
  * should calculate the price for one CartItem given the CartItem
  * and base price of product with given options
  *
  * @see [[com.redbubble.takehome.calculator.Calculator]]
  * @author vdonets
  */
trait PriceFunction extends ((CartItem, Double) => Double) {

}

object PriceFunction {

  val default_price_function = new PriceFunction {

    override def apply(item: CartItem, basePrice: Double): Double =
      (basePrice + (basePrice / 100.0 * item.markup).toInt) * item.quantity
  }
}
