package repositories

import javax.inject.Inject

import models.Post
import models.Post.Fields._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dao.JsonDao
import reactivemongo.extensions.json.dsl.JsonDsl._

class PostRepo @Inject() (val reactiveMongoApi: ReactiveMongoApi)
  extends JsonDao[Post, BSONObjectID](reactiveMongoApi.db, "posts") {

  def updateLikeById(id: BSONObjectID, liked: Boolean) =
    updateById(id, $set(Favorite -> liked))

  def updateTextById(id: BSONObjectID, text: String) =
    updateById(id, $set(Text -> text))
}
