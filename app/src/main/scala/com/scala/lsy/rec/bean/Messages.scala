package com.scala.lsy.rec.bean

import java.util.Date

import scala.collection.mutable.ListBuffer

/**
  * Created by Administrator on 2016/12/6.
  */

sealed trait ResultMessage
case class Api(id: Long, birthDate: Date, name: String)
case class User(id: Long, name: String)
case class History( name: String,image:String,tags:String,region:String,year:String,typeString:String,addDate: String,longtime:String,source:String)
case class Video(id: String, name: String,image:String,tags:String,region:String,year:String,typeString:String)
case class OneApi(item: Api) extends ResultMessage
case class UpdateSucess(item: String) extends ResultMessage
case class RecVideo(item: ListBuffer[Video]) extends ResultMessage
case class UserList(items: List[User]) extends ResultMessage
case class HistoryList(items: ListBuffer[History]) extends ResultMessage

case class Created(location: String) extends ResultMessage
case class Success(message: String) extends ResultMessage
case class Error(message: String) extends ResultMessage

sealed trait RequestMessage
case class GetApiRequst(id: Long) extends RequestMessage
case object GetUserListRequst extends RequestMessage
case class GetHistoryReques(id:Long) extends  RequestMessage
case class GetVideoReques(id:Long) extends  RequestMessage
case class UpdateHistory(userid:Long,videoid:Long) extends  RequestMessage
