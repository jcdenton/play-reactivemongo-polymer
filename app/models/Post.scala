package models

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import reactivemongo.bson.BSONObjectID

case class Post(_id: BSONObjectID = BSONObjectID.generate,
                text: String,
                username: String,
                avatar: String,
                favorite: Boolean)

object Post {
  implicit val postHandler = Json.format[Post]

  object Fields {
    val Id = "_id"
    val Text = "text"
    val Username = "username"
    val Avatar = "avatar"
    val Favorite = "favorite"
  }

  object Formats {
    import Fields._

    implicit def postWrites: Writes[Post] = (
      (JsPath \ Id).write[BSONObjectID] and
        (JsPath \ Text).write[String] and
        (JsPath \ Username).write[String] and
        (JsPath \ Avatar).write[String] and
        (JsPath \ Favorite).write[Boolean]
      )(unlift(Post.unapply))

    implicit def postListWrites: Writes[List[Post]] = Writes.list(postWrites)

    implicit def postReads: Reads[Post] = (
      (JsPath \ Text).read[String] and
        (JsPath \ Username).read[String] and
        (JsPath \ Avatar).read[String] and
        (JsPath \ Favorite).read[Boolean]
      )((text, username, avatar, favorite) =>
      Post(text = text, username = username, avatar = avatar, favorite = favorite))
  }
}
