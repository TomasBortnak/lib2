package sample.stream.demo

import sample.stream.lib.http.ReactiveStream

import scala.util.{Failure, Success}

object RESTFulClient {

  def get {

    val rs = new ReactiveStream

    val response = rs.get("localhost", 8080, "/user/2", "", "test")

    response.map {
      rp => println(rp.entity)
    }.andThen {
      case Success(_) =>
        println("request succeded")
      case Failure(_) => println("request failed")
      case _ => println("request ???")
    }
  }
}
