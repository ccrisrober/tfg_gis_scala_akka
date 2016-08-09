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

import java.util.concurrent.atomic.AtomicLong

class Counter(value: AtomicLong) {
  def this() = this(new AtomicLong())

  /**
   * Increment the counter by one.
   */
  def incr(): Long = value.incrementAndGet

  /**
   * Increment the counter by `n`, atomically.
   */
  def incr(n: Int): Long = value.addAndGet(n)

  /**
   * Get the current value.
   */
  def apply(): Long = value.get()

  /**
   * Set a new value, wiping the old one.
   */
  def update(n: Long) = value.set(n)

  /**
   * Clear the counter back to zero.
   */
  def reset() = update(0L)

  override def toString() = "Counter(%d)".format(value.get())
}
