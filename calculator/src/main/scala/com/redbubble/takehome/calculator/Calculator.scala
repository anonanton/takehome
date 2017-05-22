package com.redbubble.takehome.calculator

import com.redbubble.takehome.calculator.pricecalc.PriceFunction
import com.redbubble.takehome.data.structure.CartItem
import com.redbubble.takehome.price.PriceTree
import com.redbubble.takehome.util.LazyLogging

/**
  * Calculates prices of carts using given prices and priceFunction
  *
  * @param prices        pricing tree this calculator will use to
  *                      determine base prices
  * @param priceFunction the price function this calculator will use
  *                      to calculate price of each item in cart
  * @see [[CartItem]]
  * @see [[PriceTree]]
  * @see [[PriceFunction]]
  * @author vdonets
  */
class Calculator
(private val prices: PriceTree,
 private val priceFunction: PriceFunction = PriceFunction.default_price_function)
  extends LazyLogging {

  /**
    * Calculates total price of a cart using
    * this calculators underlying prices
    *
    * @param cart cart to calculate price for
    * @return total price of cart
    */
  def calculate(cart: Iterable[CartItem]): Double = {
    logger.debug("Calculating price for [" + cart.size + "] item cart")
    var result = 0.0
    cart.foreach(item => {
      logger.trace("item = " + item)
      val basePrice = prices.getPrice(item.prodType, item.options)
      result += priceFunction(item, basePrice)
    })
    return result
  }
}
