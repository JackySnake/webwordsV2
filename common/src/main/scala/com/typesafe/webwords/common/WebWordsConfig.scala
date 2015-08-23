package com.typesafe.webwords.common

/**
 * This class represents our app configuration.
 */
case class WebWordsConfig(amqpURL: Option[String], mongoURL: Option[String], port: Option[Int])

object WebWordsConfig {
    def apply(): WebWordsConfig = {
        val amqpURL = Option("amqp://wolhehcu:pf057BFQwfxyuQwv7dxediac2FCPmkNF@owl.rmq.cloudamqp.com/wolhehcu") //Option(System.getenv("RABBITMQ_URL"))
        val mongoURL = Option("mongodb://admin:adminx@ds031613.mongolab.com:31613/sentiment") //Option(System.getenv("MONGOHQ_URL"))
        val port = Option(System.getenv("PORT")) map { s => Integer.parseInt(s) }
        val config = WebWordsConfig(amqpURL, mongoURL, port)
        println("Configuration is: " + config)
        config
    }
}
