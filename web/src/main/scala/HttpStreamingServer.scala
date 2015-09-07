import com.twitter.concurrent.exp.AsyncStream
import com.twitter.finagle.{Httpx, Service}
import com.twitter.finagle.httpx.{Status, Response, Request}
import com.twitter.io.{Buf, Reader}
import com.twitter.util.{Await, Future, JavaTimer}
import com.twitter.conversions.time._
import scala.util.Random

/**
 * Created by joseph on 9/6/15.
 */
object HttpStreamingServer {
  val random = new Random
  implicit val timer = new JavaTimer

  // Int, sleep, repeat.
  def ints(): AsyncStream[Int] =
    random.nextInt +::
      AsyncStream.fromFuture(Future.sleep(100.millis)).flatMap(_ => ints())

  def main(args: Array[String]): Unit = {
    val service = new Service[Request, Response] {
      // Only one stream exists.
      @volatile private[this] var messages: AsyncStream[Buf] =
        ints().map(n => Buf.Utf8(n.toString))

      // Allow the head of the stream to be collected.
      messages.foreach(_ => messages = messages.drop(1))

      def apply(request: Request) = {
        val writable = Reader.writable()
        // Start writing thread.
        messages.foreachF(writable.write)
        Future.value(Response(request.version, Status.Ok, writable))
      }
    }

    Await.result(Httpx.server
      // Translate buffered writes into HTTP chunks.
      .withStreaming(enabled = true)
      // Listen on port 8080.
      .serve("0.0.0.0:8080", service)
    )
  }
}
