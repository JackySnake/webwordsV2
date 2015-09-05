package com.ssreader.service.model

/**
 * Created by joseph on 9/4/15.
 */
sealed trait Response
final case class ArticleContent(message: String) extends Response
