package io.finch.petstore

import com.twitter.util.{Await, Future}

import scala.collection.mutable

/**
 * Provides a great majority of the service methods that allow Users to interact with the Pets in the
 * store and to get information about them.
 */
class PetstoreDb {
  private[this] val users = mutable.Map.empty[Long, User]


  /**
   * POST: Create a [[User]].
   * @param newGuy The User we want to add to the database.
   * @return The user name of the added User.
   */
  def addUser(newGuy: User): Future[String] =
    users.synchronized {
      val inputName: String = newGuy.username
      if (users.values.exists(_.username == inputName))
        throw RedundantUsername(s"Username $inputName is already taken.")
      else {
        newGuy.id match {
          case Some(_) => Future.exception(InvalidInput("New user should not contain an id"))
          case None => users.synchronized {
            val genId = if (users.isEmpty) 0 else users.keys.max + 1
            users(genId) = newGuy.copy(id = Some(genId))
            Future(newGuy.username)
          }
        }
      }
    }

  /**
   * GET: Get [[User]] by username, assume all usernames are unique.
   * @param name The username of the User we want to find.
   * @return The User in question.
   */
  def getUser(name: String): Future[User] =
    users.synchronized {
      users.values.find(_.username == name) match {
        case Some(user) => Future.value(user)
        case None => Future.exception(MissingUser("This user doesn't exist!"))
      }
    }

  /**
   * DELETE: Delete a [[User]] by their username.
   * @param name The username of the User to be deleted.
   */
  def deleteUser(name: String): Future[Unit] =
    users.synchronized {
      getUser(name).flatMap {u:User =>
        u.id.foreach{ num =>
          users.remove(num)
        }
        Future.Unit
      }
    }

  /**
   * PUT: Update [[User]]. Note that usernames cannot be changed because they are the unique identifiers by which the
   * system finds existing users. Although Swagger doesn't specify this, if the username of an existing user is
   * changed, the API will no longer be able to find the user or the user's unique id.
   * @param betterUser The better, updated version of the old User.
   * @return The betterUser.
   */
  def updateUser(betterUser: User): Future[User] =
    users.synchronized {
      for {
        user <- getUser(betterUser.username)
        u = betterUser.copy(id = user.id)
      } yield {
        u.id.foreach { id =>
          users(id) = u
        }
        u
      }
    }
}

