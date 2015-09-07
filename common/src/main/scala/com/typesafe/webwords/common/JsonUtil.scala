package com.typesafe.webwords.common

import org.json4s._
import java.io.{ByteArrayInputStream, InputStreamReader}
import java.nio.charset.StandardCharsets.UTF_8

object JsonUtil {

  def jValue(json: String): JValue = {
    jValue(json.getBytes(UTF_8))
  }

  def jValue(json: Array[Byte]): JValue = {
    val reader = new InputStreamReader(new ByteArrayInputStream(json), UTF_8)
    native.JsonParser.parse(reader)
  }
}