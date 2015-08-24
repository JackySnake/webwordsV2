package com.typesafe.webwords.indexer

import akka.actor._
import akka.actor.Actor.actorOf
import com.typesafe.webwords.common._
import java.util.concurrent.CountDownLatch
import com.typesafe.webwords.common.AMQPCheck

/**
 * This is the main() method for the indexer (worker) process.
 * The indexer gets requests to "index" a URL from a work queue,
 * storing results in a persistent cache (kept in MongoDB).
 */
object Main extends App {
    val config = WebWordsConfig()
}
