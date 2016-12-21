package com.scala.lsy.rec.dao

import java.util.Date
import akka.actor.Actor
import akka.event.Logging
import com.scala.lsy.rec.bean._
import com.scala.lsy.rec.utils.{SqlParams, Jedis}
import com.vstartek.java.bean.{SqlUser, SqlHistory, SqlBean}
import com.vstartek.java.utils.PropertyUtil
import com.vstartek.java.utils.mysql.DBCommon

import scala.collection.mutable.{ListBuffer, ArrayBuffer}

/**
  * Created by Administrator on 2016/12/6.
  */
trait ApiOperations {

  //根据userid 生成推荐结果
  def getById(id: Long) = {
    val buffer = Jedis.getMixJedis(id.toString)
    var list = ListBuffer[Video]()
    for(s<-buffer){
      val array = new Array[Object](1)
      array(0)=s
      val sql = PropertyUtil.get("user.rec.id")

      val beans = DBCommon.queryForBean(sql,array,classOf[SqlBean]);
      if(beans!=null&&beans.size()>0){
        val bean = beans.get(0).asInstanceOf[SqlBean]
        list += new Video(bean.getId,bean.getName,bean.getImage,bean.getTags,bean.getRegion,bean.getYear,bean.getTypeString)
      }
    }
    RecVideo(list)
  }

  def getHistoryById(id: Long) = {
    val array = new Array[Object](1)
    array(0)=id.toString
    val sql =SqlParams.userhistoryid
    import collection.JavaConversions._
    val bean:java.util.ArrayList[SqlHistory] = DBCommon.queryForBean(sql,array,classOf[SqlHistory]).asInstanceOf[java.util.ArrayList[SqlHistory]]
    var list = ListBuffer[History]()
    for(b <- bean){
      list +=new History(b.getName,b.getImage,b.getTags,b.getRegion,b.getYear,"电影",b.getAddtime,b.getLongtime,b.getSource)
    }
    HistoryList(list)
  }

  def getVideoById(id: Long) = {
    val array = new Array[Object](1)
    array(0)=id.toString
    val sql = SqlParams.videoid
    import collection.JavaConversions._
    val bean:java.util.ArrayList[SqlHistory] = DBCommon.queryForBean(sql,array,classOf[SqlHistory]).asInstanceOf[java.util.ArrayList[SqlHistory]]
    var list = ListBuffer[History]()
    for(b <- bean){
      list +=new History(b.getName,b.getImage,b.getTags,b.getRegion,b.getYear,"电影",b.getAddtime,b.getLongtime,b.getSource)
    }
    HistoryList(list)
  }

  def all() =  {
    val list = ListBuffer[User]()
    try{
      val sql = SqlParams.userhistory
      import collection.JavaConversions._
      val beans = DBCommon.queryForBean(sql,null,classOf[SqlUser]).asInstanceOf[java.util.ArrayList[SqlUser]]
        .map(x => new User(x.getId,x.getName)).toList
      UserList(beans)
    } catch{
      case e:Exception => {
        println(e.getMessage())
        List()
      }
    }
  }

  def delete(id: Long) = {
    Success("deleted successfully")
  }

  def create (dueDate: Date, text: String) =  {
    Created("")
  }

  def updateHistory(userid:Long,videoid:Long) ={
    val array = new Array[Object](3)
    array(0)=userid.toString
    array(1)= System.currentTimeMillis().toString
    array(2)=videoid.toString
    val sql = SqlParams.historyupdate
    import collection.JavaConversions._
    DBCommon.saveBean(sql,array)
    new UpdateSucess("ok")
  }
}



class ApiActor extends Actor with ApiOperations{
  val log = Logging(context.system, this)
  def receive = {
    case GetApiRequst(id) => sender ! getById(id)
    case UpdateHistory(userid,videoid)=>sender ! updateHistory(userid,videoid)
//    case DeleteCustomer(id) => sender ! delete(id)
//    case CreateCustomer(dueDate, text) => sender ! create(dueDate, text)
    case GetVideoReques(id)=>sender ! getVideoById(id)
    case GetHistoryReques(id)=>sender ! getHistoryById(id)
    case GetUserListRequst => sender ! all()
  }
}
