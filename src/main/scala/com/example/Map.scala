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

import java.util.concurrent.ConcurrentHashMap
import collection.mutable

class Map(id: Int, mapFields: String, w: Int, h: Int, ks: mutable.ArrayBuffer[KeyObject]) {  // falta KeyObjects y RealObjects
  var Id: Int = id
  var MapFields: String = mapFields
  var Width: Int = w
  var Height: Int = h
  var KeyObjects = scala.collection.mutable.HashMap.empty[Int,KeyObject] //concurrent.Map[Int, KeyObject] = new ConcurrentHashMap().asScala
  /*
   * KeyObject removeKeyObject(int idx) {
    this.KeyObjects.remove(idx);
    return RealObjects[idx];
  }
  KeyObject addKeyObject(int idx, float x, float y) {
    RealObjects[idx].setPosition(x, y);
    this.KeyObjects[idx] = RealObjects[idx];
    return RealObjects[idx];
  }
  */
  def addKeyObject(idx: Int, x: Float, y: Float) : KeyObject = {
    var obj: KeyObject = (RealObject.Objects get idx).get
    obj.setPosition(x, y)
    RealObject.Objects.put(idx, obj)
    KeyObjects.put(idx, obj)
    obj
  }
  
  def removeKey(idx: Int) : KeyObject = {
    KeyObjects = KeyObjects - idx
    (RealObject.Objects get idx).get
  }
  
  override def toString(): String = "(" + MapFields + ")";
}

//http://docs.scala-lang.org/overviews/collections/maps.html sunchronized!!