package com.ssreader.message

import akka.actor.{ActorRef, ActorSystem}
import com.rabbitmq.client.BasicProperties
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import com.thenewmotion.akka.rabbitmq._


/**
 * Created by joseph on 26/08/2015.
 */
object PublishSubscribe extends App {
    implicit val system = ActorSystem()
    val factory = new ConnectionFactory()

//    val config = com.typesafe.config.ConfigFactory.load().getConfig("rabbitmq")
//    factory.setHost(config.getString("host"))
//    factory.setPort(config.getInt("port"))
//    factory.setUsername(config.getString("username"))
//    factory.setPassword(config.getString("password"))

    factory.setUri("amqp://wolhehcu:pf057BFQwfxyuQwv7dxediac2FCPmkNF@owl.rmq.cloudamqp.com/wolhehcu")

    val connection = system.actorOf(ConnectionActor.props(factory), "rabbitmq")
    val exchange = "amq.fanout"


    def setupPublisher(channel: Channel, self: ActorRef) {
        val queue = channel.queueDeclare().getQueue
        channel.queueBind(queue, exchange, "")
    }
    connection ! CreateChannel(ChannelActor.props(setupPublisher), Some("publisher"))

    def setupSubscriber(channel: Channel, self: ActorRef) {
        val queue = channel.queueDeclare().getQueue
        channel.queueBind(queue, exchange, "")
        val consumer = new DefaultConsumer(channel) {
             def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]) {
                println("received: " + fromBytes(body))
            }
        }
        channel.basicConsume(queue, true, consumer)
    }
    connection ! CreateChannel(ChannelActor.props(setupSubscriber), Some("subscriber"))


    /*Future {
        def loop(n: Long) {
            val publisher = system.actorSelection("/user/rabbitmq/publisher")

            def publish(channel: Channel) {
                channel.basicPublish(exchange, "", null, toBytes(n))
            }
            publisher ! ChannelMessage(publish, dropIfNoChannel = false)

            Thread.sleep(1000)
            loop(n + 1)
        }
        loop(0)
    }*/

    val publisher = system.actorSelection("/user/rabbitmq/publisher")

    def publish(channel: Channel, n: Long) {
        channel.basicPublish(exchange, "", null, toBytes(n))
    }

    val msgs = 0L to 33L
    msgs.foreach(x => publisher ! ChannelMessage(_.basicPublish(exchange, "", null, toBytes(x)), dropIfNoChannel = false))

//    expectMsgAllOf(FiniteDuration(33, TimeUnit.SECONDS), msgs: _*)

    def fromBytes(x: Array[Byte]) = new String(x, "UTF-8")
    def toBytes(x: Long) = x.toString.getBytes("UTF-8")

}
