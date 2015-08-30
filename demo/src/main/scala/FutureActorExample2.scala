import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future, Promise}

/**
  * Created by joseph on 8/30/15.
  */
object FutureActorExample2 extends App {

     implicit val system = ActorSystem("FutureSystem")

     /*val future = Future {
       1 + 1
     }

     val result = Await.result(future, 1 second)

     println(result)*/

  val fut1 = Future{Thread.sleep(3000);1}
  val fut2 = Promise.failed(new RuntimeException("boo")).future //Future{Thread.sleep(2000);1}
//  val fut2 = Future{Thread.sleep(2000);1}

  val fut3 = Future{Thread.sleep(1000);3}

  val fs = List(fut1, fut2, fut3)

  val result2: Future[List[Int]] =  all(fs)
  result2.foreach(println(_))

  println(result2.value)
  println("+++++++++++++++++++")

  /*val p = Promise[T]

  fs.foreach(_.onFailure{case i => p.tryFailure(i)})
  Future.sequence(fs).foreach(p trySuccess _) // It's a sequence of results, not sequence of effects
  p.future*/

  def all[T](fs: List[Future[T]]): Future[List[T]] = {
    val successful = Promise[List[T]]()
    successful.success(Nil)
    fs.foldRight(successful.future) {
      (f, acc) => for { x <- f; xs <- acc } yield x :: xs
    }
  }

  /*def all[T](fs: List[Future[T]]): Future[List[T]] = {
    val sequence = Promise[List[T]]()
    fs.foldRight(sequence.success(Nil).future) { (f, fs) =>
      for {x <- f; xs <- fs} yield x :: xs
    }
  }*/

 }