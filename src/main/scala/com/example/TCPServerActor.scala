// Copyright (c) 2015, maldicion069 (Cristian Rodríguez) <ccrisrober@gmail.con>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
//
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
// WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
// ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
// WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
// ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
// OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.package com.example

package com.example

import akka.io.{ IO, Tcp }

import akka.actor.{Actor, ActorLogging, Props, ActorRef}
import java.net.InetSocketAddress

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._

class TCPServerActor(port: Int, isGame: Boolean = false) extends Actor with ActorLogging {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("0.0.0.0", port))

   def receive = {
    case b @ Bound(localAddress) =>
      // do some logging or setup ...

    case CommandFailed(a: Bind) => {
      println(a)
      context stop self
    }

    case c @ Connected(remote, local) =>
      println(remote.getPort())

      val connection = sender()
      val handler = context.actorOf(Props(new SimplisticHandler(remote.getPort(), connection, isGame)))
      SingletonGame.clients += SingletonGame.Client(connection, remote.getPort())
      connection ! Register(handler)
  }
}
class SimplisticHandler(id: Int, conn: ActorRef, isGame: Boolean = false) extends Actor {

  implicit val formats = DefaultFormats // Brings in default date formats etc.
  def getAction(v: JValue): String = {
    if (v == JNothing)
      ""
    else {
      v.extract[String]
    }
  }
  def getInt(v: JValue): Int = {
    if (v == JNothing)
      -1
    else {
      v.extract[Int]
    }
  }
  def getFloat(v: JValue): Float = {
    if (v == JNothing)
      -1
    else {
      v.extract[Float]
    }
  }

  import Tcp._
  def receive = {
    case Received(data) => {
        val input = data.decodeString("UTF-8")
        var msg = data
        try{
            val json = parse(input)
            val action = getAction((json \ "Action"))
            println(action)
            action match {
                case "initWName" => {
                    println("Añadiendo nombre")
                    val map = (
                        ("Action" -> "sendMap") ~
                        ("X" -> 5*64) ~
                        ("Y" -> 5*64) ~
                        ("Id" -> this.id) ~
                        ("Users" -> SingletonGame.positions.map {
                          u => (("Id" -> u._2.Id) ~
                              ("X"-> u._2.PosX) ~
                              ("Y"-> u._2.PosY))
                        }) ~
                        ("Map" ->
                          ("Id" -> SingletonGame.maps(0).Id) ~
                          ("MapFields" -> SingletonGame.maps(0).MapFields) ~
                          ("Width" -> SingletonGame.maps(0).Width) ~
                          ("Height" -> SingletonGame.maps(0).Height)
                        ) ~
                        ("KeyObjects" -> SingletonGame.maps(0).KeyObjects.map({
                          o => (
                              ("Id" -> o._2.Id) ~
                              ("PosX" -> o._2.PosX) ~
                              ("PosY" -> o._2.PosY) ~
                              ("Color" -> o._2.Color))
                        }))
                      )
                    msg = akka.util.ByteString.apply(compact(render(map))+"\n")
                    sender() ! Write(msg)
                    val user = new ObjectUser(this.id, 5*64, 5*64)
                    SingletonGame.positions += (this.id.toString() -> user)
                    if (isGame) {
                      val msgOthers = akka.util.ByteString.apply(compact(render(
                            ("Action" -> "new") ~
                            ("Id" -> this.id) ~
                            ("PosX" -> user.PosX) ~
                            ("PosY" -> user.PosY)
                          )
                        )
                      )
                      SingletonGame.clients.foreach(
                          x => {
                              println(x)
                              if (x != null && !x.actor.isTerminated && x.id != this.id) {
                                x.actor ! Write(msgOthers)
                              }
                          }
                      )
                    }
                }
                case "move" => {
                  println("Moveme")
                  val user = SingletonGame.positions(this.id.toString)
                  user.setPosition(getFloat((json \ "PosX")), getFloat((json \ "PosY")))
                  SingletonGame.positions.update(this.id.toString, user)
                  if(isGame) {
                    SingletonGame.clients.foreach(
                        x => {
                            println(x)
                            if (x != null && !x.actor.isTerminated && x.id != this.id) {
                              x.actor ! Write(data)
                            }
                        }
                    )
                  } else {
                    sender() ! Write(data)
                  }
                }
                case "exit" => {
                  SingletonGame.positions -= this.id.toString()
                  println(input)
                  if (isGame) {
                    val msgOthers = akka.util.ByteString.apply(compact(render(
                          ("Action" -> "exit") ~
                          ("Id" -> this.id)
                        )
                      )
                    )
                    SingletonGame.clients.foreach(
                        x => {
                            println(x)
                            if (x != null && !x.actor.isTerminated && x.id != this.id) {
                              x.actor ! Write(msgOthers)
                            }
                        }
                    )
                  } else {
                    sender() ! Write(akka.util.ByteString.apply(compact(render(
                          ("Action" -> "exit") ~
                          ("Id" -> "Me")
                        )
                      )+"\n"
                    ))
                  }
                  println("FIN DEL JUEGO")
                  Thread.sleep(1000)
                  context stop self
                }
                case _ => {}
            }
        } catch {
          case e: Exception => println("exception caught: " + e);
        }
    }
    case PeerClosed     => {
      SingletonGame.positions -= this.id.toString()
      context stop self
    }
  }
}

object TCPServerActor{
  def props(port: Int, isGame: Boolean): Props = Props(new TCPServerActor(port, isGame))
}
