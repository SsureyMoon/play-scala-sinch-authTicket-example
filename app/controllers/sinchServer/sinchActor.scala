package controllers.sinchServer

import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}


import akka.actor.{ActorLogging, Actor}
import controllers.authKeys.sinch_api_keys

import play.api._
import play.api.libs.json.Json
import callData.singedAuthData
import sun.misc.{BASE64Decoder, BASE64Encoder}

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec



/**
 * Created by ssureymoon on 4/5/15.
 */
object sinchActor{
  case class singedAuthData(username: String, signedTicket: String)
}

class sinchActor extends Actor with ActorLogging{
  def receive = {
    case input: String => getAuthTicket(input)
  }
  val tz = TimeZone.getTimeZone("UTC")
  val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
  df.setTimeZone(tz)
  val nowAsISO = df.format(new Date())

  def getAuthTicket(username: String) {
    val sinchName = username.replace("@", "sinchAgo").replace(".", "Dot")
    val userTicket = Json.obj(
      "expiresIn" -> 3600,
      "applicationKey" -> sinch_api_keys.APPLICATION_KEY,
      "identity" -> Json.obj(
        "endpoint" ->  username,
        "type" -> "username"
      ),
      "created" -> nowAsISO
    )
    //Logger.info(Json.stringify(userTicket))
    val ticketString = Json.stringify(userTicket).replaceAll(" " , "")
    val ha = new BASE64Encoder()
    val ticketEncoded64 = ha.encode(ticketString.getBytes("UTF-8")).replaceAll(" " , "")
    val de = new BASE64Decoder()
    val sha256_HMAC = Mac.getInstance("HmacSHA256")
    val secret_key = new SecretKeySpec(de.decodeBuffer(sinch_api_keys.APPLICATION_SECRET) , "HmacSHA256")
    sha256_HMAC.init(secret_key)
    val signature = ha.encode(sha256_HMAC.doFinal(ticketString.getBytes()))


    val signedUserTicket = ticketEncoded64 + ":" + signature

    sender ! singedAuthData(username, signedUserTicket)

  }

}