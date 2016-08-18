package sample.stream.lib.http

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import akka.util.ByteString
import com.redis._
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import akka.stream.scaladsl._

/**
 * - should have some custom exception class
 *
 * http://doc.akka.io/docs/akka/2.4/scala/http/client-side/connection-level.html
 *
 */
class ReactiveStream {

  val host = ConfigFactory.load().getString("redis.host")
  val port = ConfigFactory.load().getString("redis.port")

  var redis = null
  //new RedisClientPool(host, port.toInt)
  var enableRecovery = false


  /**
   * By using HTTP get to send the message by reactive stream
   *
   * @param endpoint the address of the HTTP endpoint
   * @param port the port of the endpoint
   * @param queryString the query string
   * @param message the message of the HTTP request data
   * @param identity the identity of the copy of data at redis which is key in redis
   * @return Future[HTTPResponse]
   */
  def get(endpoint: String, port: Int, queryString: String, message: String, identity: String) = {

    // what good delimer of it?
    //save(identity, "GET" + ";" + endpoint + ";" + queryString + ";" + message)

    val connectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] =
      Http().outgoingConnection(endpoint, port = port)

    val data = ByteString(message)

    val responseFuture: Future[HttpResponse] =
      Source.single(HttpRequest(GET, uri = queryString, entity = data))
        .via(connectionFlow)
        .runWith(Sink.head)

    responseFuture


  }

  /**
   * By using HTTP post to send the message by reactive stream
   *
   * @param endpoint the address of the HTTP endpoint
   * @param port the port of the endpoint
   * @param queryString the query string
   * @param message the message of the HTTP request data
   * @param identity the identity of the copy of data at redis which is key in redis
   * @return Future[HTTPResponse]
   */
  def post(endpoint: String, port: Int, queryString: String, message: String, identity: String) = {


    // what good delimer of it?
    //save(identity, "POST" + ";" + endpoint + ";" + queryString + ";" + message)

    val connectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] =
      Http().outgoingConnection(endpoint, port = port)

    val data = ByteString(message)

    val responseFuture: Future[HttpResponse] =
      Source.single(HttpRequest(POST, uri = queryString, entity = data))
        .via(connectionFlow)
        .runWith(Sink.head)

    responseFuture

  }

  /*
    def retry(identity: String) = {

      if (enableRecovery) {

        redis.withClient(
          client => {

            val content = client.get(identity).getOrElse(null)

            if (content != null) {

              val all = content.split(";")

              val method = all(0)
              val endpoint = all(1)
              val queryString = all(2)
              val message = all(3)

              method match {
                case "GET" => get(endpoint, queryString, message, identity)
                case "POST" => post(endpoint, queryString, message, identity)
              }

            }

          }
        )

      }

    }

    def save(identity: String, content: String) = {

      redis.withClient {
        client => {
          client.set(identity, content)
        }
      }

    } */

}
