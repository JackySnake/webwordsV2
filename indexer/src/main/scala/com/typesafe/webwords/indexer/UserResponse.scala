package com.typesafe.webwords.indexer

/**
 * Created by joseph on 9/4/15.
 */
sealed trait UserResponse
final case class ArticleContent(message: String) extends UserResponse
