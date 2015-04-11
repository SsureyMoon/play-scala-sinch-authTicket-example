package controllers

import controllers.sinchServer.callData.singedAuthData
import controllers.sinchServer.{MainActors, BootedActor, callData, sinchActor}
import controllers.authKeys.sinch_api_keys

import play.api._
import play.api.libs.json._

import play.api.mvc._

import scala.concurrent.{Await, Future}


import scala.concurrent.duration._

import akka.pattern.ask

import akka.util.Timeout





object Application extends Controller with BootedActor with MainActors {

  case class singedAuthFailException(msg: String) extends Exception(msg)

  def webchat = Action { request =>

    //change this to your key
    val content = views.html.webchat(sinch_api_keys.APPLICATION_KEY)
    Ok(content).withSession(request.session)
  }




  def makecall = Action { request =>
    val body = request.body.asJson
    body.map{value=>
      val username = (value \ ("username")).toString()
      implicit val timeout = Timeout(1.seconds)
      val future: Future[singedAuthData] = (sinchActorSys ? username).mapTo[singedAuthData]
      val result = Await.result(future, timeout.duration)
      Ok(Json.stringify(Json.obj("userTicket" -> result.signedTicket.replaceAll("[\\r\\n]", "")))).withHeaders("Access-Control-Allow-Origin" -> "*").withHeaders("Content-Type" -> "application/json; charset=UTF-8")
    }.getOrElse(BadRequest)

  }

}


