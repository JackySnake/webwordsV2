package com.ssreader.service.model

/**
 * Created by joseph on 9/3/15.
 */
sealed trait Request
final case class ArticleLink(username: Option[String] = None, password: Option[String] = None, link: String) extends Request
