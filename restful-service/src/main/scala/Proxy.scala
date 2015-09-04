import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Await, Future}
import java.net.InetSocketAddress
import org.jboss.netty.handler.codec.http._
import com.twitter.finagle.builder.{ClientBuilder, ServerBuilder}

//#app
object Proxy extends App {
  /*val client: Service[HttpRequest, HttpResponse] =
    Http.newService("joseph.com:80")

  val server = Http.serve(":8088", client)
  Await.ready(server)*/

  val client: Service[HttpRequest, HttpResponse] =
    Http.newService("localhost:8080")

  val server = Http.serve("joseph.com", client)
  Await.ready(server)

  /*val client: Service[HttpRequest, HttpResponse] =
    Http.newService("localhost:8080")

  val server = Http.serve("127.0.0.1:80", client)
  Await.ready(server)*/
}