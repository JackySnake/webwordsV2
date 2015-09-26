import akka.actor.{ActorSystem, Props, Actor}
import akka.event.Logging

/**
 * Created by joseph on 9/14/15.
 */

case object Ping

class HelloActor extends Actor {
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case Ping => log.info(s" hello Ping ")
  }
}

object ActorUtils {
  def props(name: String) = Props(classOf[HelloActor], name)
}

object ActorStorm extends App {
  implicit val system = ActorSystem("akka-storm")

  val hello = system.actorOf(Props(new HelloActor), name = "test")

  hello ! Ping

  system.shutdown()

}
