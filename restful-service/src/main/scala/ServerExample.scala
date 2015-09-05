import java.io.PrintWriter

import com.kohlschutter.boilerpipe.BoilerpipeExtractor
import com.kohlschutter.boilerpipe.extractors.{CommonExtractors, ArticleExtractor}
import com.kohlschutter.boilerpipe.sax.HTMLHighlighter
import com.ssreader.service.model.{ArticleContent, ArticleLink}
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Http}
import com.twitter.io.Charsets._
import com.twitter.util.Future
import org.jboss.netty.buffer.ChannelBuffers._
import org.jboss.netty.handler.codec.http._
import java.net.{URL, SocketAddress, InetSocketAddress}
import com.twitter.finagle.builder.{Server, ServerBuilder}
import org.json4s.DefaultFormats
import org.json4s._
import org.json4s.native.Serialization.{write}

import uk.ac.shef.dcs.jate.model.{Term, InMemoryDocument}
import uk.ac.shef.dcs.jate.processing.KeywordExtraction


/**
 * Created by joseph on 01/09/2015.
 */
object ServerExample extends App {
  // Define our service: OK response for root, 404 for other paths
  val rootService = new Service[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest) = {
      val r = request.getUri match {
        case "/api/read" => {
          println("=============>>>>>>")

          // This is how you parse request parameters
//          val params = new QueryStringDecoder(request.getUri()).getParameters()

          val jsonContent = request.asInstanceOf[Request].getContentString()

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

          res.setContent(copiedBuffer(message, Utf8))

          res
        }

        case "/" => {
          val res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
          res.setContent(copiedBuffer("hello world", Utf8))
          res
        }

        case _ => new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND)
      }
      Future.value(r)
    }
  }

  // Serve our service on a port
  val address: SocketAddress = new InetSocketAddress(10001)
  val server: Server = ServerBuilder()
    .codec(Http())
    .bindTo(address)
    .name("HttpServer")
    .build(rootService)
}
