package com.scala.lsy.rec.utils

import com.vstartek.java.utils.redis.JedisUtil
import redis.clients.jedis.Jedis

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Administrator on 2016/12/12.
  */
object Jedis {
  def putJedis(recType:String,userid:String,gson:String): Unit ={
    if(recType!=null&&userid.nonEmpty){
      val jedis: Jedis = JedisUtil.getInstance.getJedis
      jedis.hset(recType,userid,gson)
      JedisUtil.getInstance.returnJedis(jedis)
    }
  }

  def getMixJedis(userid:String): ArrayBuffer[String] ={
    val jedis: Jedis = JedisUtil.getInstance.getJedis
    val result = jedis.hget("mix.lsy",userid)
    var resultArray= ArrayBuffer[String]()
    if(result!=null&&result.nonEmpty){
      resultArray ++= result.split(";")
    }
    JedisUtil.getInstance.returnJedis(jedis)
    println("得到的redis中数据："+resultArray)
    resultArray
  }
  def getMixJedis(videotype:String,userid:String): String ={
    val jedis: Jedis = JedisUtil.getInstance.getJedis
    val result = jedis.hget(videotype,userid)
    JedisUtil.getInstance.returnJedis(jedis)
    println("得到的redis中数据："+result)
    result
  }

  //根据用户获取观看历史
  def getHistoryVideoIds(key:String,videoid:String): Set[String] ={
    val jedis: Jedis = JedisUtil.getInstance.getJedis
    import collection.JavaConversions._
    val set = jedis.smembers(key)
    jedis.sadd(key,videoid)
    set.toSet
  }
}
