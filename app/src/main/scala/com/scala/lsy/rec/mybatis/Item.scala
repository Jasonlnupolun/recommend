package com.scala.lsy.rec.mybatis

/**
  * Created by Administrator on 2016/12/12.
  */
class Item {
  var id : Int = _
  var description : String = _
  var info : Option[String] = None
  var year : Option[Int] = None
}

object Item {

  def apply(description : String, info : Option[String] = None, year : Option[Int] = None) = {
    val i = new Item
    i.description = description
    i.info = info
    i.year = year
    i
  }

}
