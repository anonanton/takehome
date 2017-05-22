package com.redbubble.takehome.data.schema

import com.redbubble.takehome.data.schema.json.JsonSchemaValidator

/**
  * Validates data (Json, xml, etc) against a schema
  *
  * @author vdonets
  */
trait SchemaValidator{

  private[data] def validate(data: String): Boolean
}

object SchemaValidator {

  /**
    * Creates a new JsonSchemaValidator
    * @param schemaPath path to Json schema
    * @return a new JsonSchemaValidator
    */
  def forJson(schemaPath: String) = new JsonSchemaValidator(schemaPath)
}
