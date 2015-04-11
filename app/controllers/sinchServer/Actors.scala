package controllers.sinchServer

import akka.actor.{Props, ActorSystem}

/**
 * Created by ssureymoon on 4/6/15.
 */
trait Actors {
  implicit def system: ActorSystem
}

trait BootedActor extends Actors {
  implicit lazy val system = ActorSystem("akka-Sinch")
  sys.addShutdownHook(system.shutdown())
}

trait MainActors {
  this: Actors =>

  val sinchActorSys = system.actorOf(Props[sinchActor], name="testActor")
}