import edu.stanford.nlp.ie.crf.CRFClassifier
import scala.collection.JavaConversions._
//import scala.collection.JavaConverters._
import edu.stanford.nlp.ling.CoreAnnotations
//import java.util.ArrayList
//import java.util.HashMap
//import java.util.Map
//import scala.xml.XML
//import javax.xml.parsers.SAXParser
//import java.util.LinkedHashMap
import edu.stanford.nlp.ling.CoreLabel
//import java.util.List

//import scala.collection.immutable.Seq
//import scala.collection.Seq
import scala.collection.mutable.ListBuffer

/**
 * Created by joseph on 23/09/2015.
 */
object MainNERAnnotator extends App {

  val serializedClassifier = "stanford_classifiers/english.all.3class.distsim.crf.ser.gz"
  val classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier)

  val text = """
    The Stanford University, which is where the Stanford NER tool is from, is located in California.
    Leonardo Da Vinci was a famous italian painter, originally born in Vinci, Italy.
    Wikipedia is one very big source of information for playing with Named Entities;
    for example start reading the page: http://en.wikipedia.org/wiki/Named-entity_recognition.
             """

  val keywords = new ListBuffer[(String, String)]

  for (sentence <- classifier.classify(text)) {

    def recognized(word: CoreLabel): Boolean = {
      !word.get(classOf[CoreAnnotations.AnswerAnnotation]).equals("O") &&
        !word.get(classOf[CoreAnnotations.AnswerAnnotation]).equals("")
    }

    def annotation(word: CoreLabel) = word.get(classOf[CoreAnnotations.AnswerAnnotation])

    val annotatedWords = sentence.toList.filterNot(word => !recognized(word))

    val map = annotatedWords
      .groupBy(word => annotation(word))
      .map(e => e._1 match {
      case "LOCATION" => (e._1, e._2.mkString(", "))
      case _ => (e._1, e._2.mkString(" "))
    })

    keywords.appendAll(map)
  }

  println("\nKEYWORDS: " + keywords.toList.mkString(" | "))

  val out2 = classifier.classifyWithInlineXML(text)
  println(out2)

  val out3 = classifier.classifyToString(text)
  println(out3)

}
