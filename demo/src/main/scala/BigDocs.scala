import java.io.{FileWriter, BufferedWriter, File}
import java.net.URL

import com.kohlschutter.boilerpipe.extractors.CommonExtractors

import scala.collection.generic.CanBuildFrom
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source
import scala.util.{Failure, Success}

/**
 * Created by joseph on 21/09/2015.
 */
object BigDocs extends App {

  val timeout = 1500 millis

    val stream = getClass.getResourceAsStream("/files.list")
    val lines = Source.fromInputStream(stream).getLines().toList
    lines.zipWithIndex.foreach {
      case (value, index) => println(index, value)
    }


  println("Creating futureList")
  val futures = lines.map {
    getText
  }

  println("Creating waitinglist")
  val waitingList = Future sequence futures
  println("Created")


  val results = Await result(waitingList, 2 * timeout * futures.size)

  val location = getClass.getResource("/data").getPath

  results.zipWithIndex.foreach {
      case(v, i) =>
          println(v)
          val file = new File(location + "/" + i + ".txt")
          val bw = new BufferedWriter(new FileWriter(file))
          bw.write(v)
          bw.close()
  }

//    lines.foreach(getText(_).value)

  println("===================================================================================================")

//  lines map getText

  println("===================================================================================================")

//  val result = Future.traverse(lines)(getText)

  println("===================================================================================================")

  /*result.onComplete {
    case Success(value) =>
      println(s"Got the callback, size = ${value.size}")
//      println(s"Got the callback, meaning = $value")

    case Failure(e) => e.printStackTrace
  }

  val start = System.currentTimeMillis

   Await.result(result, 30 second)

  val timeElapsed = System.currentTimeMillis - start

  println(timeElapsed)*/


//  result.value.foreach(println)

  /*val start = System.currentTimeMillis
  val doubled = Await.result({
    serialiseFutures(List(10, 20)) { i ⇒
      Future {
        Thread.sleep(i)
        i * 2
      }
    }
  }, 10 second)

  val timeElapsed = System.currentTimeMillis - start
//  timeElapsed should be >= (30l)
//  doubled should be(List(20, 40))


  doubled.foreach(println)*/



    def getText(link: String) = Future {
        println(s"Processing ... $link")
        val url: URL = new URL(link)
        val extractor = CommonExtractors.ARTICLE_EXTRACTOR

      val article = extractor.getText(url)

//        val hh = HTMLHighlighter.newExtractingInstance()
//        val  result = hh.process(url, extractor)

//      println(article)

      article
    }


  def serialiseFutures[A, B, C[A] <: Iterable[A]]
  (collection: C[A])(fn: A ⇒ Future[B])(
    implicit ec: ExecutionContext,
    cbf: CanBuildFrom[C[B], B, C[B]]): Future[C[B]] = {
    val builder = cbf()
    builder.sizeHint(collection.size)

    collection.foldLeft(Future(builder)) {
      (previousFuture, next) ⇒
        for {
          previousResults ← previousFuture
          next ← fn(next)
        } yield previousResults += next
    } map { builder ⇒ builder.result }
  }

}
