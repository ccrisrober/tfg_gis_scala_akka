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

import akka.actor.ActorSystem
import java.io._
import java.net._

import scala.collection.mutable.HashTable
import collection.mutable


object ApplicationMain extends App {
  val port = Option(System.getenv("PORT")).map(_.toInt).getOrElse(8090)
  println(port)

  val system = ActorSystem("MyActorSystem")

  var isGame = false
  println("[S/s] Game Mode / [_] Test Mode")
  val r = readChar()
  if (r == 'S' || r == 's') {
    isGame = true
  }
  println(isGame)

  val tcpServerActor = system.actorOf(TCPServerActor.props(port, isGame), "serverActor")

  println("Init server")
}
