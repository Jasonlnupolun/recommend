package com.scala.lsy.rec

/**
  * Created by Administrator on 2016/12/6.
  */

import java.io.File

import akka.actor._
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.scala.lsy.rec.service.RestInterface
import com.typesafe.config.ConfigFactory
import com.vstartek.java.utils.PropertyUtil
import spray.can.Http

import scala.concurrent.duration._
object Boot extends App {
  val parsedConfig = ConfigFactory.parseFile(new File(PropertyUtil.path))
  val config = ConfigFactory.load(parsedConfig)
  val host = config.getString("http.host")
  val port = config.getInt("http.port")
  implicit val system = ActorSystem("rec-service")
  val api = system.actorOf(Props(new RestInterface()), "httpInterface")

  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)
  IO(Http).ask(Http.Bind(listener = api, interface = host, port = port))
    .mapTo[Http.Event]
    .map {
      case Http.Bound(address) =>
        println(s"REST interface bound to $address")
      case Http.CommandFailed(cmd) =>
        println("REST interface could not bind to " +
          s"$host:$port, ${cmd.failureMessage}")
        system.shutdown()
    }
}

