package com.redbubble.takehome.exception

/**
  * Thrown when data being loaded by a datasource
  * is not valid.
  *
  * @author vdonets
  */
class InvalidDataException(s: String) extends IllegalArgumentException(s)

/**
  * Thrown if data fails validation by a schema
  *
  * @param s
  */
class SchemaValidationException(s: String) extends InvalidOptionsException(s)

/**
  * Thrown if multiple PriceEntries have same price paths
  * (same product with same options) that have a price
  * value associated with them
  *
  * @param s
  */
class PricesCollisionException(s: String) extends InvalidOptionsException(s)
