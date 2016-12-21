package com.scala.lsy.rec.service
import akka.actor._
import akka.util.Timeout
import com.scala.lsy.rec.bean._
import com.scala.lsy.rec.dao.ApiActor
import com.scala.lsy.rec.view.{RestApiRequestCreator}
import org.json4s.DefaultFormats
import spray.httpx.Json4sSupport
import spray.routing._
import scala.concurrent.duration._
class RestInterface extends HttpServiceActor with RestApiService with RestApiRequestCreator {
  def receive = runRoute(routes)
  def handleRequest(message: RequestMessage): Route =
    ctx => customerRequest(ctx, Props[ApiActor], message)
}
trait RestApiService extends HttpService  with Json4sSupport with ActorLogging { actor: Actor =>
  val json4sFormats = DefaultFormats
  implicit val timeout = Timeout(10 seconds)
  def routes: Route =
    pathPrefix("api") {
      pathPrefix("shenzhen") {
        path("user" / LongNumber) {
          id: Long =>
            get {
              rejectEmptyResponse {
                handleRequest {
                  GetApiRequst(id)
                }
              }
            }
        } ~
          path("rec") {
            get {
              handleRequest {
                GetUserListRequst
              }
            }
          } ~
          path("userlist") {
            get {
              handleRequest {
                GetUserListRequst
              }
            }
          } ~
          path("history" / LongNumber) { id =>
            get {
              rejectEmptyResponse {
                handleRequest {
                  GetHistoryReques(id)
                }
              }
            }
          } ~
          path("historyupdate") {
            parameters('userid.as[Long], 'videoid.as[Long]) { (userid, videoid) =>
              get {
                rejectEmptyResponse {
                  handleRequest {
                    UpdateHistory(userid, videoid)
                  }
                }
              }
            }
          }
      }~ pathPrefix("youpeng") {
        path("user" / LongNumber) {
          id: Long =>
            get {
              rejectEmptyResponse {
                handleRequest {
                  GetApiRequst(id)
                }
              }
            }
        } ~
          path("rec") {
            get {
              handleRequest {
                GetUserListRequst
              }
            }
          } ~
          path("userlist") {
            get {
              handleRequest {
                GetUserListRequst
              }
            }
          } ~
          path("history" / LongNumber) { id =>
            get {
              rejectEmptyResponse {
                handleRequest {
                  GetHistoryReques(id)
                }
              }
            }
          } ~
          path("historyupdate") {
            parameters('userid.as[Long], 'videoid.as[Long]) { (userid, videoid) =>
              get {
                rejectEmptyResponse {
                  handleRequest {
                    UpdateHistory(userid, videoid)
                  }
                }
              }
            }
          }
      }
    }



  def handleRequest(message: RequestMessage): Route
}

