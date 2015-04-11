package controllers.sinchServer

import play.api.data.Form

/**
 * Created by ssureymoon on 4/5/15.
 */
object callData {
  case class singedAuthData(username: String, signedTicket: String)
}
