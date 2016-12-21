package com.scala.lsy.rec.view

import akka.actor.SupervisorStrategy.Stop
import akka.actor._
import com.scala.lsy.rec.bean.Created
import com.scala.lsy.rec.bean.Success
import com.scala.lsy.rec.bean._
import com.scala.lsy.rec.view.RestApiRequest.WithProps
import org.json4s.DefaultFormats
import spray.http.StatusCode
import spray.http.StatusCodes._
import spray.httpx.Json4sSupport
import spray.routing.RequestContext
import akka.actor.OneForOneStrategy
import scala.concurrent.duration._


/**
  * Created by Administrator on 2016/12/6.
  */
trait RestApiRequest  extends Actor with Json4sSupport{
  def requestContext: RequestContext
  def target: ActorRef
  def message: RequestMessage
  import context._
  setReceiveTimeout(2.seconds)
  target ! message

  def receive = {
    case OneApi(message)=>complete(OK, message)
    case HistoryList(message)=>complete(OK, message)
    case UserList(users) => complete(OK,users)
    case RecVideo(videos) => complete(OK,videos)
//    case Created(location) => complete(spray.htt
    // p.StatusCodes.Created, location)
//    case Success(message) => complete(OK, message)
    case UpdateSucess(message)=> complete(OK,message)
    case Error(message) => complete(BadRequest, message)
    case ReceiveTimeout => complete(GatewayTimeout, "Request Timeout")
  }

  def complete[T <: AnyRef](status: StatusCode, obj: T) = {
    requestContext.complete(status, obj)
    stop(self)
  }

  override val supervisorStrategy =
    OneForOneStrategy() {
      case e => {
        complete(InternalServerError, Error(e.getMessage))
        Stop
      }
    }
}


object RestApiRequest {

  case class WithProps(requestContext: RequestContext, props: Props, message: RequestMessage) extends RestApiRequest {
    lazy val target = context.actorOf(props)

    implicit def json4sFormats = DefaultFormats
  }

}

trait RestApiRequestCreator {
  this: Actor =>
  def customerRequest(requestContext: RequestContext, props: Props, message: RequestMessage) =
    context.actorOf(Props(new WithProps(requestContext, props, message)))
}
