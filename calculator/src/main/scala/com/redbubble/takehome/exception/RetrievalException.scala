package com.redbubble.takehome.exception

/**
  * Thrown when arguments are not valid to retrieve a price
  *
  * @author vdonets
  */
class RetrievalException(s: String) extends IllegalArgumentException(s)

/**
  * Thrown when options are not valid during price retrieval
  *
  * @author vdonets
  */
class InvalidOptionsException(s: String) extends RetrievalException(s)

/**
  * Thrown when product type is not valid during price retrieval
  *
  * @author vdonets
  */
class InvalidProductTypeException(s: String) extends RetrievalException(s)