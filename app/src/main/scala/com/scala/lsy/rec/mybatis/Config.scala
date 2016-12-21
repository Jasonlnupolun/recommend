package com.scala.lsy.rec.mybatis

/**
  * Created by Administrator on 2016/12/12.
  */
import org.mybatis.scala.config._

object Config {

  // Load datasource configuration
  val config = Configuration("mybatis.xml")

  // Create a configuration space, add the data access method
  config.addSpace("item") { space =>
    space ++= ItemDAO
  }

  // Build the session manager
  val persistenceContext = config.createPersistenceContext


}