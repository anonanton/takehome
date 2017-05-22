package com.redbubble.takehome.data

import com.redbubble.takehome.data.deserializer.Deserializer
import com.redbubble.takehome.data.schema.SchemaValidator

/**
  * Handles deserialization and optionally validation
  * sequence of some data input.
  *
  * @see [[SchemaValidator]]
  * @see [[Deserializer]]
  * @see [[DataSource]]
  * @author vdonets
  */
trait DataSetBuilder[R] {

  protected type T <: DataSetBuilder[R]

  /**
    * Sets the validator we want to use
    *
    * @param schemaValidator the validator we want to use
    * @return this
    */
  def validateWith(schemaValidator: SchemaValidator): T

  /**
    * Sets the deserializer we want to use
    *
    * @param deserializer the deserializer we want to use
    * @return this
    */
  def deserializeWith(deserializer: Deserializer): T

  /**
    * Gets data using given deserializer and validator
    *
    * @return data
    */
  def get: R
}
