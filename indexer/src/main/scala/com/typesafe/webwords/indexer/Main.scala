package com.typesafe.webwords.indexer

import java.net.{URL}
import util.Properties

import com.kohlschutter.boilerpipe.extractors.CommonExtractors
import com.kohlschutter.boilerpipe.sax.HTMLHighlighter
import com.twitter.finagle.{Httpx, Service}
import com.twitter.finagle.httpx.{Status, Response, Request}

import com.twitter.util.{Await, Future}
import com.typesafe.webwords.common.JsonUtil
import org.json4s.native.Serialization.write
import org.json4s.{DefaultFormats, _}
import uk.ac.shef.dcs.jate.model.{InMemoryDocument, Term}
import uk.ac.shef.dcs.jate.processing.KeywordExtraction

/**
 * This is the main() object for the web process. It starts up an embedded
 * Jetty web server which delegates all the work to Akka HTTP.
 */
object Main extends App {
  // Define our service: OK response for root, 404 for other paths
  val rootService = new Service[Request, Response] {
    def apply(request: Request): Future[Response] = {
      val res = request.path match {
        case "/api/read" => {
          println("=============>>>>>>")

          // This is how you parse request parameters
          //          val params = new QueryStringDecoder(request.getUri()).getParameters()

          /*val jsonContent = request.asInstanceOf[Request].getContentString()

          implicit val formats = DefaultFormats

          val defaultsJson = Extraction.decompose()
          val valueJson = JsonUtil.jValue(jsonContent)
          val link: ArticleLink = (defaultsJson merge valueJson).extract[ArticleLink]*/

          val link: String = getLinkRequest(request)
          val url: URL = new URL(link)

          //          val url: URL = new URL("http://www.npr.org/sections/health-shots/2013/10/18/236211811/brains-sweep-themselves-clean-of-toxins-during-sleep")

          val extractor = CommonExtractors.ARTICLE_EXTRACTOR

          val article = extractor.getText(url)


          val document = new InMemoryDocument(article);
          val extraction = new KeywordExtraction(document);
          val terms: Array[Term] = extraction.extractKeywords();
          terms.take(15)
            .foreach(x => println("%s %s", x.getConcept, x.getConfidence))

          val keyword = terms.take(15).map(x => x.getConcept).mkString(", ")

          println("%s", keyword)

          // choose the operation mode (i.e., highlighting or extraction)
          val hh = HTMLHighlighter.newExtractingInstance()
          val articleHTML = hh.process(url, extractor)
          val message = write(ArticleContent(articleHTML, keyword))

          // val res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
          // res.setContent(copiedBuffer(message, Utf8))

          val response = Response(request.version, Status.Ok)
          response.setContentTypeJson();
          response.setContentString(message);
          Future.value(response)
        }

        case "/" => {
          //                    val res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
          //                    res.setContent(copiedBuffer("hello world", Utf8))
          //                    res
          val response = Response(request.version, Status.Ok)
          response.setContentString("hello world")
          Future.value(response)
        }

        case _ =>
          //                    val res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND)
          Future.value(Response(request.version, Status.NotFound))
      }
      res
    }
  }

  // Serve our service on a port
//  val address: SocketAddress = new InetSocketAddress(10001)

  /*val server: Server = ServerBuilder()
    .codec(Http())
    .bindTo(address)
    .name("HttpServer")
    .build(rootService)*/

  println("Starting server ... it works")

  val port = Properties.envOrElse("PORT", "8080").toInt
  println("Starting on port:" + port)

  val server = Httpx.serve(":" + port, rootService)
  Await.ready(server)

  //HTTP endpoint
  /*val socketAddress = new InetSocketAddress(8080)
  val server = ServerBuilder()
    .codec(new RichHttp[Request](Http()))
    .bindTo(socketAddress)
    .name("HTTP endpoint")
    .build(rootService)*/

  def getLinkRequest(request: Request): String = {
    implicit val formats = DefaultFormats

    val jsonContent = request.asInstanceOf[Request].getContentString()
    val defaultsJson = Extraction.decompose()
    val valueJson = JsonUtil.jValue(jsonContent)
    val link: ArticleLink = (defaultsJson merge valueJson).extract[ArticleLink]

    link.link
  }
}
