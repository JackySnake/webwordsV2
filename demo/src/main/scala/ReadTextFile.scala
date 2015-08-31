import scala.io.Source

/**
 * Created by joseph on 8/28/15.
 */
object ReadTextFile extends App {

//    val textFile = Source.fromFile("classpath:src/main/resources/water.txt").getLines().toList
    val stream = getClass.getResourceAsStream("/water.txt")
    val lines = Source.fromInputStream(stream).getLines().toList
    lines.foreach(line => printf(line))


}
