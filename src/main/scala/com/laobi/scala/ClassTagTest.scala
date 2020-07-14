package com.laobi.scala

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

object ClassTagTest {

  def getValueFromMap[T : ClassTag](key: String, dataMap: Map[String, Any]): Option[T] = {

    dataMap.get(key) match {
      case Some(value: T) => Some(value)
      case _ => None
    }

  }

  def getValueFromMap2[T : TypeTag](x: T) = {

    val targs = typeOf[T] match { case TypeRef(_, _, args) => args }
    println(s"type of $x has type arguments $targs")

    x
  }

  def main(args: Array[String]): Unit = {

    class Animal {
      override def toString = "I am Animal"
    }

    val myMap: Map[String, Any] = Map("Number" -> 1, "Greeting" -> "Hello World",
      "Animal" -> new Animal)
    // returns Some(1)
    val number1: Option[Int] = getValueFromMap[Int]("Number", myMap)
    println("number is " + number1)
    // returns None
    val numberNotExists: Option[Int] = getValueFromMap[Int]("Number2", myMap)
    println("number is " + numberNotExists)
    println
    // returns Some(Hello World)
    val greeting: Option[String] = getValueFromMap[String]("Greeting", myMap)
    println("greeting is " + greeting)
    // returns None
    val greetingDoesNotExists: Option[String] = getValueFromMap[String]("Greeting1", myMap)
    println("greeting is " + greetingDoesNotExists)
    println()
    // returns Some[Animal]
    val animal: Option[Animal] = getValueFromMap[Animal]("Animal", myMap)
    println("Animal is " + animal)
    // returns None
    val animalDoesNotExist: Option[Animal] = getValueFromMap[Animal]("Animal1", myMap)
    println("Animal is " + animalDoesNotExist)
    println
    // 注意，这里开始出现问题了
    // 现在编译器不会报错，因为所有的都发生在运行时
    // 即使getValueFromMap 返回的是 Option[String]
    val greetingInt: Option[Int] = getValueFromMap[Int]("Greeting", myMap)
    // 输出 Some(Hello World)
    println("greetingInt is " + greetingInt)
    // 这里会抛出 ClassCastException
    val somevalue = greetingInt.map((x) => x + 5)
    // 下面的不会打印
    println(somevalue)

    println()

    getValueFromMap2("adsc")
    println(getValueFromMap2(List(1,2,4)).head + 1)

  }

}
