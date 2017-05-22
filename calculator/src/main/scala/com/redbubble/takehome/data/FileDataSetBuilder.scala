package com.redbubble.takehome.data

import com.redbubble.takehome.data.deserializer.Deserializer
import com.redbubble.takehome.data.schema.SchemaValidator
import com.redbubble.takehome.exception.SchemaValidationException
import com.redbubble.takehome.util.LazyLogging

/**
  * Data set builder implementation to work with local files.
  * A data set builder is intended as a one-time use builder
  * for a data set, even though it is reusable. <br>
  * NOTE: If reuse is desired, this implementation is not thread safe
  *
  * @author vdonets
  */
private[data] class FileDataSetBuilder[R: scala.reflect.runtime.universe.TypeTag](private val filePath: String)
  extends DataSetBuilder[R] with LazyLogging {

  logger.trace("Creating file set builder for [" + filePath + "]")

  private val data = scala.io.Source.fromFile(filePath).mkString

  logger.trace("Loaded " + data)
  override protected type T = this.type

  private var schema: SchemaValidator = null
  private var deserializer: Deserializer = null

  override def validateWith(schema: SchemaValidator): T = {
    logger.trace("setting schema")
    this.schema = schema
    return this
  }

  override def deserializeWith(deserializer: Deserializer): T = {
    logger.trace("setting deserializer")
    this.deserializer = deserializer
    return this
  }

  override def get: R = {
    if (schema != null) {
      if (!schema.validate(data)) {
        logger.debug("Unable to validate " + data + " with a " + schema.getClass)
        throw new SchemaValidationException("Unable to validate data")
      }
    } else
      logger.debug("No validator")

    return deserializer.deserialize[R](data)
  }
}
