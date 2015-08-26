package com.hamrah.streamsplayground

import com.typesafe.config.ConfigFactory

/**
 * Created by joseph on 25/08/2015.
 */
object Config {
  private val config = ConfigFactory.load()
  lazy val streams = config.getConfig("streams-playground")

  lazy val interface = streams.getString("interface")
  lazy val port = streams.getInt("port")
}
