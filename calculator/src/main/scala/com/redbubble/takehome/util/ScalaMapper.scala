package com.redbubble.takehome.util

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

/**
  * An object mapper appropriate for use with Scala classes
  * and pre configured to by used by calculator.
  *
  * @author vdonets
  */
private[takehome] class ScalaMapper extends ObjectMapper with ScalaObjectMapper {

  this.registerModule(DefaultScalaModule)
  this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  this.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
}

private[takehome] object ScalaMapper {

  /**
    * The default mapper instance for the application
    */
  lazy val default_instance = new ScalaMapper
}
