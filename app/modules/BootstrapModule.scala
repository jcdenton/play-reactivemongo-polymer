package modules

import play.api.Mode.Prod
import play.api.inject.Module
import play.api.{Configuration, Environment}
import repositories.PostFixtures

class BootstrapModule extends Module {

  override def bindings(environment: Environment, configuration: Configuration) = {
    environment.mode match {
      case Prod => Seq.empty
      case _ => Seq(bind(classOf[PostFixtures]).toSelf.eagerly())
    }
  }
}
