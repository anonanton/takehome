package com.redbubble.takehome.data

/**
  * Base data loader trait. Loads and optionally
  * validates data.
  *
  * @author vdonets
  */
trait DataSource {

  /**
    * Loads data from given path
    *
    * @param path path to load from
    * @tparam T data type
    * @return a data set builder that will need a deserializer
    *         and optionally a validator
    */
  def load[T: scala.reflect.runtime.universe.TypeTag](path: String): DataSetBuilder[T]
}

object DataSource {

  def fromFile: DataSource = new FileDataSource
}