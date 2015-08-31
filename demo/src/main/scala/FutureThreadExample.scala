import java.util.concurrent.Executors

import akka.actor.ActorSystem

import scala.concurrent.{ExecutionContext, Await, Future}

import scala.concurrent.duration._

/**
 * Created by joseph on 8/30/15.
 */
object FutureThreadExample extends App {


//    implicit val system = ActorSystem("FutureSystem")

    val pool = Executors.newCachedThreadPool()
    implicit val ec = ExecutionContext.fromExecutorService(pool)

    val future = Future {
      1 + 1
    }

    val result = Await.result(future, 1 second)

    println(result)

    pool.shutdown()
}
