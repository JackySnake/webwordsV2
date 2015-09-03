import org.json4s._

object JsonUtil {
  import java.nio.charset.StandardCharsets.UTF_8
  import java.io.{InputStreamReader, ByteArrayInputStream}

  def jValue(json: String): JValue = {
    jValue(json.getBytes(UTF_8))
  }

  def jValue(json: Array[Byte]): JValue = {
    val reader = new InputStreamReader(new ByteArrayInputStream(json), UTF_8)
    native.JsonParser.parse(reader)
  }
}