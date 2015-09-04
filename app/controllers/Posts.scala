package controllers

import javax.inject.Inject

import models.Post
import models.Post.Fields._
import models.Post.Formats._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, BodyParsers, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import repositories.PostRepo
import utils.JSONImplicits._

import scala.concurrent.Future

class Posts @Inject()(val reactiveMongoApi: ReactiveMongoApi,
                      val postRepo: PostRepo)
  extends Controller with MongoController with ReactiveMongoComponents {

  def list = Action.async { implicit request =>
    postRepo.findAll()
      .map(posts => Ok(Json.toJson(posts)))
  }

  def like(id: String) = Action.async(BodyParsers.parse.json) { implicit request =>
    postRepo.updateLikeById(id, (request.body \ Favorite).as[Boolean])
      .map(result => Ok(Json.obj(result)))
  }

  def update(id: String) = Action.async(BodyParsers.parse.json) { implicit request =>
    postRepo.updateTextById(id, (request.body \ Text).as[String])
      .map(result => Ok(Json.obj(result)))
  }

  def delete(id: String) = Action.async {
    postRepo.removeById(id)
      .map(result => Redirect(routes.Posts.list()))
  }

  def create = Action.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[Post].fold(
      errors => Future.successful(BadRequest(JsError.toJson(errors))),
      post => postRepo.save(post).map(result => Redirect(routes.Posts.list()))
    )
  }
}
