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

import scala.collection.mutable

class ObjectUser(id: Int, x: Float, y: Float) {
  var Id: Int = id
  var PosX: Float = x
  var PosY: Float = y
  var Map: Int = 0
  var RollDice: Int = 0
  var Objects: Set[Int] = Set()
  
  def setPosition(x: Float, y: Float) {
    this.PosX = x
    this.PosY = y
  }
  
  def addObject(idx: Int) {
    this.Objects = this.Objects + idx
  }
  
  def removeObject(idx: Int) {
    this.Objects = this.Objects - idx
  }
  
}