package com.redbubble.takehome

/**
  * @author vdonets
  */
object TestUtil {

  /**
    * Gets absolute path of a file on class path
    *
    * @param name
    */
  def getFilePath(name: String): String =
    getClass.getClassLoader.getResource(name).getFile
}
