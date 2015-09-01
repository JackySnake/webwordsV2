import org.jboss.netty.handler.codec.http.{DefaultHttpRequest, HttpRequest, HttpResponse, HttpVersion, HttpMethod}
import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.Http

/**
 * Created by joseph on 01/09/2015.
 */
object ClientExample extends App {
  // Don't worry, we discuss this magic "ClientBuilder" later
  val client: Service[HttpRequest, HttpResponse] = ClientBuilder()
    .codec(Http())
    .hosts("localhost:10000") // If >1 host, client does simple load-balancing
    .hostConnectionLimit(1)
    .build()

  val req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/")

  val f = client(req) // Client, send the request

  // Handle the response:
  f onSuccess { res =>
    println("got response", res)
  } onFailure { exc =>
    println("failed :-(", exc)
  }
}
