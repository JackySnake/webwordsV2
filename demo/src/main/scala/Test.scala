/**
 * Created by joseph on 8/30/15.
 */
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

import java.util.concurrent.Executors
import java.lang.System.{ nanoTime => now }

object Test extends App {
  //implicit val xc = ExecutionContext.global
  implicit val xc = ExecutionContext fromExecutorService (Executors.newSingleThreadExecutor)

  def timed[A](body: =>A): A = {
    val start = now
    val res = body
    val end = now
    Console println (Duration fromNanos end-start).toMillis + " " + res
    res
  }
  println("Creating futureList")

  val timeout = 1500 millis
  val futures = List(1000, 1500, 1200, 800, 2000) map { ms =>
    val f = future {
      timed {
        blocking(Thread sleep ms)
        ms toString
      }
    }
    Future firstCompletedOf Seq(f, fallback(timeout))
  }

  println("Creating waitinglist")
  val waitingList = Future sequence futures
  println("Created")

  timed {
    val results = Await result (waitingList, 2 * timeout * futures.size)
    println(results)
  }
  xc.shutdown

  def fallback(timeout: Duration) = future {
    timed {
      blocking(Thread sleep (timeout toMillis))
      "-1"
    }
  }
}
