/*
 * Copyright 2017 SIA Joom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.michaelrocks.xxhash

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer

class ByteArrayHasher : Hasher {
  private val bytes = ByteArrayOutputStream()
  private val data = DataOutputStream(bytes)

  fun toByteArray(): ByteArray {
    return bytes.toByteArray()
  }

  override fun update(argument: XxHashArgument) {
    return when (argument) {
      is XxHashArgument.WholeArray ->
        data.write(argument.array)
      is XxHashArgument.RangedArray ->
        data.write(argument.array, argument.offset, argument.size)
      is XxHashArgument.WholeHeapByteBuffer ->
        update(argument.buffer.duplicate())
      is XxHashArgument.RangedHeapByteBuffer ->
        update(argument.buffer.duplicate(), argument.offset, argument.size)
      is XxHashArgument.WholeDirectByteBuffer ->
        update(argument.buffer.duplicate())
      is XxHashArgument.RangedDirectByteBuffer ->
        update(argument.buffer.duplicate(), argument.offset, argument.size)
      is XxHashArgument.BooleanValue ->
        data.writeBoolean(argument.value)
      is XxHashArgument.CharValue ->
        data.writeChar(argument.value.reverseBytes().toInt())
      is XxHashArgument.ByteValue ->
        data.writeByte(argument.value.toInt())
      is XxHashArgument.ShortValue ->
        data.writeShort(argument.value.reverseBytes().toInt())
      is XxHashArgument.IntValue ->
        data.writeInt(argument.value.reverseBytes())
      is XxHashArgument.LongValue ->
        data.writeLong(argument.value.reverseBytes())
      is XxHashArgument.FloatValue ->
        data.writeInt(argument.value.toRawIntBits().reverseBytes())
      is XxHashArgument.DoubleValue ->
        data.writeLong(argument.value.toRawLongBits().reverseBytes())
    }
  }

  private fun update(buffer: ByteBuffer) {
    val array = ByteArray(buffer.remaining())
    buffer.get(array)
    data.write(array)
  }

  private fun update(buffer: ByteBuffer, offset: Int, size: Int) {
    buffer.position(offset)
    val array = ByteArray(size)
    buffer.get(array)
    data.write(array)
  }
}
