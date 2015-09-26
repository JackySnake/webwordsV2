import java.util

import edu.stanford.nlp.ie.AbstractSequenceClassifier
import edu.stanford.nlp.ie.crf.CRFClassifier
import edu.stanford.nlp.io.IOUtils
import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.util.Triple

/**
 * Created by joseph on 9/27/15.
 */
object ScalaNERExample extends App {
    val serializedClassifier: String = "stanford_classifiers/english.all.3class.distsim.crf.ser.gz"
    val classifier: AbstractSequenceClassifier[CoreLabel] = CRFClassifier.getClassifier(serializedClassifier)
    val fileContents: String = IOUtils.slurpFile("data/test.txt")
    val list: util.List[Triple[String, Integer, Integer]] = classifier.classifyToCharacterOffsets(fileContents)

    import scala.collection.JavaConversions._

    for (item <- list.map(x => x.first + ": " + fileContents.substring(x.second, x.third)).distinct.sorted) {
        println(item)
    }
}
