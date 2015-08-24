package com.typesafe.webwords.web

import com.typesafe.webwords.common._

/**
 * This is the main() object for the web process. It starts up an embedded
 * Jetty web server which delegates all the work to Akka HTTP.
 */
object WebMain extends App {
    val config = WebWordsConfig()

    val server = new WebServer(config)
    server.start()
    server.run()
    server.stop()
}
