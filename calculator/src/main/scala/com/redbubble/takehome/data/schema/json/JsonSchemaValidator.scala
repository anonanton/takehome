package com.redbubble.takehome.data.schema.json

import java.nio.file.{Files, Paths}
import java.util.function.Consumer

import com.networknt.schema.{JsonSchema, ValidationMessage}
import com.redbubble.takehome.data.schema.SchemaValidator
import com.redbubble.takehome.data.schema.json.JsonSchemaValidator.factory
import com.redbubble.takehome.util.{LazyLogging, ScalaMapper}

/**
  * Schema validator implementation for JSON data.
  *
  * @author vdonets
  */
class JsonSchemaValidator(schemaLocation: String) extends SchemaValidator with LazyLogging {

  logger.debug("Creating json validator from [" + schemaLocation + "]")

  private val schema: JsonSchema = {
    val in = Files.newInputStream(Paths.get(schemaLocation))
    try {
      factory.getSchema(in)
    } finally {
      in.close()
    }
  }

  /**
    * Validates a Json string
    *
    * @param data Json to validate
    * @return true if valid false otherwise
    */
  override private[data] def validate(data: String): Boolean = {
    val msgs = schema.validate(ScalaMapper.default_instance.readTree(data))
    if (!msgs.isEmpty) {
      logger.error("Unable to validate: " + data)
      msgs.forEach(new Consumer[ValidationMessage] {
        override def accept(m: ValidationMessage): Unit = {
          logger.error("[" + m.getCode + "] - [" + m.getMessage
            + "] of type [" + m.getType + "] at [" + m.getPath + "]")
        }
      })
      return false
    } else {
      logger.trace("Successfully validated " + data)
      return true
    }

  }
}

private[data] object JsonSchemaValidator {

  import com.networknt.schema.JsonSchemaFactory

  private[json] val factory = new JsonSchemaFactory

}
