import com.kohlschutter.boilerpipe.extractors.ArticleExtractor
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
//          val link: ArticleLink = parse(jsonContent).extract[ArticleLink]

          val defaultsJson = Extraction.decompose()
          val valueJson = JsonUtil.jValue(jsonContent)
          val link: ArticleLink = (defaultsJson merge valueJson).extract[ArticleLink]

          val res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)

//          val message = "{\"message\": \"A friend of mine led his company from nothing to over $1 billion in revenue in record time by relentlessly pursuing his product vision. He did so by intimately involving himself in the intricate details of his company’s product planning and execution. This worked brilliantly up to about 500 employees. Then, as the company continued to scale, things started to degenerate. He went from being the visionary product founder who kept cohesion and context across and increasingly complex product line to the seemingly arbitrary decision maker and product bottleneck. This frustrated employees and slowed development. In reaction to that problem and to help the company scale, he backed off and started delegating all the major product decisions and direction to the team. And then he ran smack into the Product CEO Paradox: The only thing that will wreck a company faster than the product CEO being highly engaged in the product is the product CEO disengaging from the product.\"}"

//          val url: URL = new URL("http://www.npr.org/sections/health-shots/2013/10/18/236211811/brains-sweep-themselves-clean-of-toxins-during-sleep")

          val url: URL = new URL(link.link)

          //  System.out.println(ArticleExtractor.INSTANCE.getText(url))
          val article = ArticleExtractor.INSTANCE.getText(url)

          val document = new InMemoryDocument(article);
          val extraction = new KeywordExtraction(document);
          val terms: Array[Term] = extraction.extractKeywords();
          terms.foreach(x => println("sdlfsdfj %s %s", x.getConcept, x.getConfidence))

          val message = write(ArticleContent(article))

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
  val address: SocketAddress = new InetSocketAddress(10000)
  val server: Server = ServerBuilder()
    .codec(Http())
    .bindTo(address)
    .name("HttpServer")
    .build(rootService)
}
