package com.typesafe.webwords.indexer

/**
 * Created by joseph on 9/3/15.
 */
sealed trait UserRequest
final case class ArticleLink(username: Option[String] = None,
                             password: Option[String] = None,
                             link: String) extends UserRequest
