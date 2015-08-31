import akka.actor.ActorSystem

import scala.concurrent.duration._
import scala.concurrent.{Promise, Await, ExecutionContext, Future}

import ExecutionContext.Implicits.global

/**
  * Created by joseph on 8/30/15.
  */
object FutureActorExample extends App {

     implicit val system = ActorSystem("FutureSystem")

     /*val future = Future {
       1 + 1
     }

     val result = Await.result(future, 1 second)

     println(result)*/

  val fut1 = Future {
      List.range(1,300).foreach(println("+++ 1 %s",_))
      3
  }
  val fut2 = Promise.failed(new RuntimeException("boo")).future
  /*val fut2 = Future {
      List.range(1,400).foreach(println("=== 2 %s",_))
      2
  }*/

  val fut3 = Future {
      List.range(1,100).foreach(println("### 3 %s",_))
      4
  }

  val fs = List(fut1, fut2, fut3)

  val result2: Future[List[Int]] =  all(fs)

  val result = Await.result(result2, 3 second)
//  result.

//  result2.foreach(f => f.foreach(print(_)))

  /*val p = Promise[T]

  fs.foreach(_.onFailure{case i => p.tryFailure(i)})
  Future.sequence(fs).foreach(p trySuccess _) // It's a sequence of results, not sequence of effects
  p.future*/

  def all[T](fs: List[Future[T]]): Future[List[T]] = {
    val p = Promise[List[T]]()
    fs.foreach(_.onFailure{case i => p.tryFailure(i)})
    Future.sequence(fs).foreach(p trySuccess _) // It's a sequence of results, not sequence of effects
    p.future
  }

 }