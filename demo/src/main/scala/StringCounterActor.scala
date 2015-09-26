import akka.actor.Actor
import akka.actor.Actor.Receive

/**
 * Created by joseph on 9/14/15.
 */
case class StringProcessRequest(string: String)
case class StringProcessedResponse(words: Integer)

class StringCounterActor extends Actor {
  override def receive: Receive = {
    case StringProcessRequest(string) =>
      val wordsInLine = string.split(" ").length
      sender ! StringProcessedResponse(wordsInLine)

    case _ =>
  }
}


