package com.scala.lsy.rec.utils

import com.vstartek.java.utils.PropertyUtil

/**
  * Created by Administrator on 2016/12/14.
  */
object SqlParams {

  val historyupdate= PropertyUtil.get("history.update")
  val userhistory = PropertyUtil.get("user.history")
  val videoid =  PropertyUtil.get("video.id")
  val userhistoryid=  PropertyUtil.get("user.history.id")
}
