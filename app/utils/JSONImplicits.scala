package utils

import play.api.libs.json.Json
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONObjectID

import scala.language.implicitConversions

object JSONImplicits {

  implicit def stringToBSONObjectID(id: String) = BSONObjectID(id)

  implicit def writeResultToJsObject(result: WriteResult): (String, Json.JsValueWrapper) = "success" -> result.ok
}
