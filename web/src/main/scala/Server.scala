/*
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.httpx._
import com.twitter.finagle.Service
import com.twitter.util.Future
import java.net.InetSocketAddress
*/

import java.net.URL

import com.kohlschutter.boilerpipe.extractors.CommonExtractors
import com.kohlschutter.boilerpipe.sax.HTMLHighlighter
import com.ssreader.service.model.{ArticleContent, ArticleLink}
import com.twitter.finagle.http.Request
import com.twitter.finagle.{Httpx, Service}
import com.twitter.finagle.httpx
import com.twitter.io.Charsets._
import com.twitter.util.{Await, Future}
import com.typesafe.webwords.common.JsonUtil
import org.jboss.netty.buffer.ChannelBuffers._
import org.jboss.netty.handler.codec.http.{HttpResponseStatus, HttpVersion, DefaultHttpResponse}
import org.json4s.native.Serialization._
import org.json4s.{Extraction, DefaultFormats}
import uk.ac.shef.dcs.jate.model.{Term, InMemoryDocument}
import uk.ac.shef.dcs.jate.processing.KeywordExtraction

object Server extends App {

  /*val service = new Service[Request, Response] {
    def apply(request: Request): Future[Response] = {
      val response = Response()
      response.setContentString("Hello from Finagle\n")
      Future.value(response)
    }
  }

  val address = new InetSocketAddress(10000)

  def start() = ServerBuilder()
    .codec(new RichHttp[Request](Http()))
    .name("HttpServer")
    .bindTo(address)
    .build(service)

  def main(args: Array[String]){
    println("Start HTTP server on port 10000")
    val server = start()
  }*/

  val service = new Service[httpx.Request, httpx.Response] {
    def apply(request: httpx.Request): Future[httpx.Response] = {

      val res = request.uri match {
        case "/api/read" => {
          println("=============>>>>>>")

          // This is how you parse request parameters
          //          val params = new QueryStringDecoder(request.getUri()).getParameters()

          /*val jsonContent = request.asInstanceOf[Request].getContentString()

          implicit val formats = DefaultFormats

          val defaultsJson = Extraction.decompose()
          val valueJson = JsonUtil.jValue(jsonContent)
          val link: ArticleLink = (defaultsJson merge valueJson).extract[ArticleLink]

          val url: URL = new URL(link.link)

          //          val url: URL = new URL("http://www.npr.org/sections/health-shots/2013/10/18/236211811/brains-sweep-themselves-clean-of-toxins-during-sleep")

          val extractor = CommonExtractors.ARTICLE_EXTRACTOR

          val article = extractor.getText(url)
          val document = new InMemoryDocument(article);
          val extraction = new KeywordExtraction(document);
          val terms: Array[Term] = extraction.extractKeywords();
          terms.take(10).foreach(x => println("%s %s", x.getConcept, x.getConfidence))

          // choose the operation mode (i.e., highlighting or extraction)
          val hh = HTMLHighlighter.newExtractingInstance()
          val articleHTML = hh.process(url, extractor)
          val message = write(ArticleContent(articleHTML))

          val res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)

          res.setContent(copiedBuffer(message, Utf8))*/

//          res

          Future.value(
            httpx.Response(request.version, httpx.Status.Ok)
          )
        }

        case "/" => {
          val res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
          res.setContent(copiedBuffer("hello world", Utf8))
//          res

          Future.value(
            httpx.Response(request.version, httpx.Status.Ok)
          )
        }

        case _ =>
          new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND)
          Future.value(
            httpx.Response(request.version, httpx.Status.Ok)
          )
      }

      res


      /*Future.value(
        httpx.Response(request.version, httpx.Status.Ok)
      )*/
    }
  }

  val server = Httpx.serve(":8080", service)
  Await.ready(server)
}