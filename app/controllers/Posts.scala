package controllers

import backend.{PostMongoRepo, PostRepo}
import controllers.PostFields._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc._
import reactivemongo.bson.{BSONObjectID, BSONDocument}
import reactivemongo.core.actors.Exceptions.PrimaryUnavailableException
import reactivemongo.core.commands.LastError

trait Posts {
  this: Controller =>

  def postRepo: PostRepo = PostMongoRepo

  def list = Action.async {implicit request =>
    postRepo.find()
      .map(posts => Ok(Json.toJson(posts.reverse)))
      .recover {case PrimaryUnavailableException => InternalServerError("Please install MongoDB")}
  }

  def like(id: String) = Action.async(BodyParsers.parse.json) { implicit request =>
    val value = (request.body \ Favorite).as[Boolean]
    postRepo.update(BSONDocument(Id -> BSONObjectID(id)), BSONDocument("$set" -> BSONDocument(Favorite -> value)))
      .map(le => Ok(Json.obj("success" -> le.ok)))
  }

  def update(id: String) = Action.async(BodyParsers.parse.json) { implicit request =>
    val value = (request.body \ Text).as[String]
    postRepo.update(BSONDocument(Id -> BSONObjectID(id)), BSONDocument("$set" -> BSONDocument(Text -> value)))
      .map(le => Ok(Json.obj("success" -> le.ok)))
  }

  def delete(id: String) = Action.async {
    postRepo.remove(BSONDocument(Id -> BSONObjectID(id)))
      .map(le => RedirectAfterPost(le, routes.Posts.list()))
  }

  private def RedirectAfterPost(lastError: LastError, call: Call): Result =
    if (lastError.inError) InternalServerError("%s".format(lastError))
    else Redirect(call)

  def add = Action.async(BodyParsers.parse.json) { implicit request =>
    val username = (request.body \ Username).as[String]
    val text = (request.body \ Text).as[String]
    val avatar = (request.body \ Avatar).as[String]
    postRepo.save(BSONDocument(
      Text -> text,
      Username -> username,
      Avatar -> avatar,
      Favorite -> false
    )).map(le => Redirect(routes.Posts.list()))
  }
}

object Posts extends Controller with Posts

object PostFields {
  val Id = "_id"
  val Text = "text"
  val Username = "username"
  val Avatar = "avatar"
  val Favorite = "favorite"
}
