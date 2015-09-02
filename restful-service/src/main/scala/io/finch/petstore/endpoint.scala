package io.finch.petstore

import com.twitter.finagle.Service
import com.twitter.finagle.httpx.{Request, Response}
import com.twitter.util.{Future}
import io.finch._
import io.finch.argonaut._
import io.finch.request._
import io.finch.response._
import io.finch.route._

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
      addUsersViaArray(db)
//      getUser(db) :+:
//      deleteUser(db)
//      updateUser(db)

  /**
   * Compiles together all the endpoints relating to public service methods.
   * @return A service that contains all provided endpoints of the API.
   */
  def makeService(db: PetstoreDb): Service[Request, Response] = handleExceptions andThen (
      users(db)
    ).toService

  /**
   * The information of the added User is passed in the body.
   * @return A Router that contains a RequestReader of the username of the added User.
   */
  def addUser(db: PetstoreDb): Router[String] =
    post("user" ? body.as[User]) { u: User =>
      db.addUser(u).asInstanceOf[String]
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
  /*def deleteUser(db: PetstoreDb): Router[Unit] =
    delete("user" / string) { n: String =>
      db.deleteUser(n)
    }*/

  /**
   * The username of the User to be found is passed in the path.
   * @return A Router that contains the User in question.
   */
  /*def getUser(db: PetstoreDb): Router[User] =
    get("user" / string) { n: String =>
      db.getUser(n)
    }*/

  /**
   * The username of the User to be updated is passed in the path.
   * @return A Router that contains a RequestReader of the User updated.
   */
  /*def updateUser(db: PetstoreDb): Router[User] =
    put("user" / string ? body.as[User]) { (n: String, u: User) =>
      db.updateUser(u)
    }*/
}