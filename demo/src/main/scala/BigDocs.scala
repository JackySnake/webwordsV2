import java.net.URL

import com.kohlschutter.boilerpipe.extractors.CommonExtractors
import com.kohlschutter.boilerpipe.sax.HTMLHighlighter

import scala.collection.generic.CanBuildFrom
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source

/**
 * Created by joseph on 21/09/2015.
 */
object BigDocs extends App {

    val stream = getClass.getResourceAsStream("/files.list")
    val lines = Source.fromInputStream(stream).getLines().toList
    lines.foreach(println)

//    lines.foreach(getText(_).value)

  lines map getText

   Await.result(Future.traverse(lines)(getText), 30 second)


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
        println(">>> " + link)
        val url: URL = new URL(link)
        val extractor = CommonExtractors.ARTICLE_EXTRACTOR
        val hh = HTMLHighlighter.newExtractingInstance()
        val  result = hh.process(url, extractor)
      println(result)

      result
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
