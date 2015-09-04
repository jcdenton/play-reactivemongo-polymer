package repositories

import javax.inject.{Inject, Singleton}

import play.api.Logger
import play.api.inject.ApplicationLifecycle
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.extensions.json.fixtures.JsonFixtures

import scala.concurrent.duration._
import scala.util.{Failure, Success}

@Singleton
class PostFixtures @Inject()(val reactiveMongoApi: ReactiveMongoApi,
                                        val lifecycle: ApplicationLifecycle,
                                        val postRepo: PostRepo) {

  Logger.info("Loading fixtures")

  reactiveMongoApi.db.connection.waitForPrimary(5.seconds).onSuccess { case _ =>
    JsonFixtures(reactiveMongoApi.db).load("posts.conf").onComplete {
      case Success(ids) => Logger.info("Successfully loaded fixtures")
      case Failure(e) => Logger.error("Failed loading fixtures", e)
    }
  }

  lifecycle.addStopHook(() => {
    Logger.info("Dropping fixtures")
    postRepo.drop().andThen {
      case Success(_) => Logger.info("Successfully dropped fixtures")
      case Failure(e) => Logger.error("Failed dropping fixtures", e)
    }
  })
}
