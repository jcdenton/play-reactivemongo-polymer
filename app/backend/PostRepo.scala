package backend

import play.api.Play.current
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.bson.BSONDocument
import reactivemongo.core.commands.LastError
import play.modules.reactivemongo.json.BSONFormats._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait PostRepo {
  def db = ReactiveMongoPlugin.db

  protected def collection = db.collection[JSONCollection]("posts")

  def find(): Future[List[JsObject]]
  def update(selector: BSONDocument, update: BSONDocument): Future[LastError]
  def remove(document: BSONDocument): Future[LastError]
  def save(document: BSONDocument): Future[LastError]
}

object PostMongoRepo extends PostRepo {
  override def find(): Future[List[JsObject]] = {
    collection.find(Json.obj())
      .cursor[JsObject]
      .collect[List]()
  }

  override def update(selector: BSONDocument, update: BSONDocument): Future[LastError] = {
    collection.update(selector, update)
  }

  override def remove(document: BSONDocument): Future[LastError] = collection.remove(document)

  override def save(document: BSONDocument): Future[LastError] = collection.save(document)
}
