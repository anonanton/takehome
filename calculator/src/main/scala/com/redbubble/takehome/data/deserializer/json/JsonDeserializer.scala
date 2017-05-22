package com.redbubble.takehome.data.deserializer.json

import com.redbubble.takehome.data.deserializer.Deserializer
import com.redbubble.takehome.util.ScalaMapper

/**
  * Json implementation of deserializer
  *
  * @author vdonets
  */
class JsonDeserializer extends Deserializer {

  import scala.reflect.runtime.universe
  import scala.reflect.runtime.universe.TypeTag

  override def deserialize[T: TypeTag](data: String): T = {
    return ScalaMapper.default_instance.readValue(data,
      universe.runtimeMirror(getClass.getClassLoader)
        .runtimeClass(universe.typeOf[T]).asInstanceOf[Class[T]])
  }
}