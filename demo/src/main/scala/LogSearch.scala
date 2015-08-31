import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
import scala.util.{ Success, Failure }
import akka.actor._
import akka.pattern.{ after, ask, pipe }
import akka.util.Timeout

object LogSearch extends App {

  println("Starting actor system")
  val system = ActorSystem("futures")

  println("Starting log search")
  try {
    // timeout for each search task
    val fallbackTimeout = 2 seconds

    // timeout use with akka.patterns.ask
    implicit val timeout = new Timeout(5 seconds)

    require(fallbackTimeout < timeout.duration)

    // Create SearchActor
    val search = system.actorOf(Props[LogSearchActor])

    // Test worktimes for search
    val worktimes = List(1000, 1500, 1200, 800, 2000, 600, 3500, 8000, 250)

    // Asking for results
    val futureResults = (search ? Search(worktimes, fallbackTimeout))
      // Cast to correct type
      .mapTo[List[String]]
      // In case something went wrong
      .recover {
      case e: TimeoutException => List("timeout")
      case e: Exception => List(e getMessage)
    }
      // Callback (non-blocking)
      .onComplete {
      case Success(results) =>
        println(":: Results ::")
        results foreach (r => println(s"  $r"))
        system shutdown ()
      case Failure(t) =>
        t printStackTrace ()
        system shutdown ()
    }

  } catch {
    case t: Throwable =>
      t printStackTrace ()
      system shutdown ()
  }

  // Await end of programm
  system awaitTermination (20 seconds)
}

class LogSearchActor extends Actor {

  def receive = {
    case Search(worktimes, timeout) =>
      // Doing all the work in one actor using futures
      val searchFutures = worktimes map { worktime =>
        val searchFuture = search(worktime)
        val fallback = after(timeout, context.system.scheduler) { Future successful s"$worktime ms > $timeout" }
        Future firstCompletedOf Seq(searchFuture, fallback)
      }

      // Pipe future results to sender
      (Future sequence searchFutures) pipeTo sender
    case _ =>
  }

  def search(worktime: Int): Future[String] = future {
    Thread sleep worktime
    s"found something in $worktime ms"
  }
}

case class Search(worktime: List[Int], timeout: FiniteDuration)