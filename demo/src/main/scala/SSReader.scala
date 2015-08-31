import java.net.URL

import com.kohlschutter.boilerpipe.extractors.ArticleExtractor
import uk.ac.shef.dcs.jate.model.{Term, InMemoryDocument}
import uk.ac.shef.dcs.jate.processing.KeywordExtraction

/**
 * Created by joseph on 31/08/2015.
 */
object SSReader extends App {
    val url: URL = new URL("http://www.npr.org/sections/health-shots/2013/10/18/236211811/brains-sweep-themselves-clean-of-toxins-during-sleep")
//  System.out.println(ArticleExtractor.INSTANCE.getText(url))
    val article = ArticleExtractor.INSTANCE.getText(url)

    val document = new InMemoryDocument(article);
    val extraction = new KeywordExtraction(document);
    val terms: Array[Term] = extraction.extractKeywords();
    terms.foreach(x => println("sdlfsdfj %s %s", x.getConcept, x.getConfidence))


}
