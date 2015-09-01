package io.finch.petstore

import com.twitter.finagle.Service
import com.twitter.finagle.httpx.path.{/, Root, ->}
import com.twitter.finagle.httpx.{Method, Request, Response}
import com.twitter.util.{Future}
import io.finch._
import io.finch.argonaut._
import io.finch.request._
import io.finch.response._
import io.finch.route.Router

/**
 * Provides the paths and endpoints for all the API's public service methods.
 */
object endpoint extends ErrorHandling {

  /**
   * Private method that compiles all user service endpoints.
   * @return Bundled compilation of all user service endpoints.
   */
  private def users(db: PetstoreDb) =
    addUser(db) :+:
    addUsersViaList(db) :+:
    addUsersViaArray(db) :+:
    getUser(db) :+:
    deleteUser(db) :+:
    updateUser(db)

  /**
   * Compiles together all the endpoints relating to public service methods.
   * @return A service that contains all provided endpoints of the API.
   */
  def makeService(db: PetstoreDb): Service[Request, Response] = handleExceptions andThen (
    users(db)
  ).toService

  def route = {
    case Method.Get -> Root / "hello" / name =>
      //curl 'http://localhost:9090/hello/Andrea'
      Service.mk(req =>
        Ok(s"Hello ${name}").toFuture
      )
    /*case Method.Post -> Root / "user" =>
      //curl -X POST 'http://localhost:9090/user?name=Andrea&age=65'
      Service.mk(req => {
        for {
          name <- RequiredParam("name")(req)
          age <- RequiredParam("age")(req).map(_.toInt)
        } yield {
          val user = User(name, age)
          Ok(s"Hello ${user.greet}")
        }
      })*/
    case _ -> path =>
      Service.mk(req =>
        BadRequest(s"Service not found for path: ${path.toString.replace(Root.toString, "")}").toFuture
      )
  }


  /**
   * The information of the added User is passed in the body.
   * @return A Router that contains a RequestReader of the username of the added User.
   */
  /*def addUser(db: PetstoreDb): Router[String] =
    post("user" ? body.as[User]) { u: User =>
      db.addUser(u)
    }*/

  def addUser(db: PetstoreDb): Router[String] = {
    /*post("user" ? body.as[User]) { u: User =>
      db.addUser(u)*/

    case Method.Get -> Root / "hello" / name =>
      //curl 'http://localhost:9090/hello/Andrea'
      Service.mk(req =>
        Ok(s"Hello ${name}").toFuture
      )


    }

  /**
   * The list of Users is passed in the body.
   * @return A Router that contains a RequestReader of a sequence of the usernames of the Users added.
   */
  def addUsersViaList(db: PetstoreDb): Router[Seq[String]] =
    post("user" / "createWithList" ? body.as[Seq[User]]) { s: Seq[User] =>
      Future.collect(s.map(db.addUser))
    }

  /**
   * The array of users is passed in the body.
   * @return A Router that contains a RequestReader of a sequence of the usernames of the Users added.
   */
  def addUsersViaArray(db: PetstoreDb): Router[Seq[String]] =
    post("user" / "createWithArray" ? body.as[Seq[User]]) { s: Seq[User] =>
      Future.collect(s.map(db.addUser))
    }

  /**
   * The username of the User to be deleted is passed in the path.
   * @return A Router that contains essentially nothing unless an error is thrown.
   */
  def deleteUser(db: PetstoreDb): Router[Unit] =
    delete("user" / string) { n: String =>
      db.deleteUser(n)
    }

  /**
   * The username of the User to be found is passed in the path.
   * @return A Router that contains the User in question.
   */
  def getUser(db: PetstoreDb): Router[User] =
    get("user" / name) { n: String =>
      db.getUser(n)
    }

  /**
   * The username of the User to be updated is passed in the path.
   * @return A Router that contains a RequestReader of the User updated.
   */
  def updateUser(db: PetstoreDb): Router[User] =
    put("user" / string ? body.as[User]) { (n: String, u: User) =>
      db.updateUser(u)
    }
}

