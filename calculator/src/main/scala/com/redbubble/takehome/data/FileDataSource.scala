package com.redbubble.takehome.data

/**
  * Data source implementation to use with local files
  *
  * @author vdonets
  */
class FileDataSource private[data]() extends DataSource {
  override def load[T: scala.reflect.runtime.universe.TypeTag](path: String): DataSetBuilder[T] = new FileDataSetBuilder[T](path)
}
