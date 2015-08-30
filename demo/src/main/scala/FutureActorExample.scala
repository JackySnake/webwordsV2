import akka.actor.ActorSystem

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

import ExecutionContext.Implicits.global

/**
  * Created by joseph on 8/30/15.
  */
object FutureActorExample extends App {

     implicit val system = ActorSystem("FutureSystem")

     val future = Future {
       1 + 1
     }

     val result = Await.result(future, 1 second)

     println(result)

 }