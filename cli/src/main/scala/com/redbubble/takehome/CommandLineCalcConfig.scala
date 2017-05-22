package com.redbubble.takehome

import org.springframework.boot.context.properties.ConfigurationProperties

/**
  * Boot config
  *
  * @author vdonets
  */
@ConfigurationProperties("cli")
class CommandLineCalcConfig {

  private[takehome] var priceData: String = null
  private[takehome] var priceSchema: String = null
  private[takehome] var cartSchema: String = null

  def setPriceData(data: String) = this.priceData = data

  def setPriceSchema(schema: String) = this.priceSchema = schema

  def setCartSchema(schema: String) = this.cartSchema = schema
}

