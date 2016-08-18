package sample.stream.demo

import scala.concurrent.Future

object Main {

  def main(args: Array[String]) {


    Future {
      RESTFulServer.run()
    }

    println("Trying to see how the client work...")


    for (a <- 1 to 20) {
      println("The number " + a + " request now")
      RESTFulClient.get
    }

  }

}
