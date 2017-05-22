package com.redbubble.takehome.data.deserializer

import com.redbubble.takehome.data.deserializer.json.JsonDeserializer

/**
  * Base trait for deserializers of various data types
  *
  * @author vdonets
  */
trait Deserializer {

  /**
    * Deserializes given data
    *
    * @param data data to deserailize
    * @return deserialized data
    */
  def deserialize[T: scala.reflect.runtime.universe.TypeTag](data: String): T
}

object Deserializer {

  def forJson = new JsonDeserializer
}
