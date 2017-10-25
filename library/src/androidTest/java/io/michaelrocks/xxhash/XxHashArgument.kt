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

import java.nio.ByteBuffer
import java.util.Arrays
import java.util.Random

sealed class XxHashArgument {
  data class WholeArray(val array: ByteArray) : XxHashArgument() {
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      other as? WholeArray ?: return false
      return Arrays.equals(array, other.array)
    }

    override fun hashCode(): Int {
      var result = 17
      result = 31 * result + Arrays.hashCode(array)
      return result
    }

    object ArgumentFactory : Factory<WholeArray> {
      override fun create(random: Random): WholeArray {
        return WholeArray(newArray(random))
      }
    }
  }

  data class RangedArray(val array: ByteArray, val offset: Int, val size: Int) : XxHashArgument() {
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      other as? RangedArray ?: return false
      return Arrays.equals(array, other.array) && offset == other.offset && size == other.size
    }

    override fun hashCode(): Int {
      var result = 17
      result = 31 * result + Arrays.hashCode(array)
      result = 31 * result + offset
      result = 31 * result + size
      return result
    }

    object ArgumentFactory : Factory<RangedArray> {
      override fun create(random: Random): RangedArray {
        val array = newArray(random)
        val offset = random.nextIntOrZero(array.size)
        val size = random.nextIntOrZero(array.size - offset)
        return RangedArray(array, offset, size)
      }
    }
  }

  data class WholeHeapByteBuffer(val buffer: ByteBuffer) : XxHashArgument() {
    object ArgumentFactory : Factory<WholeHeapByteBuffer> {
      override fun create(random: Random): WholeHeapByteBuffer {
        return WholeHeapByteBuffer(newByteBuffer(random, false))
      }
    }
  }

  data class RangedHeapByteBuffer(val buffer: ByteBuffer, val offset: Int, val size: Int) : XxHashArgument() {
    object ArgumentFactory : Factory<RangedHeapByteBuffer> {
      override fun create(random: Random): RangedHeapByteBuffer {
        val buffer = newByteBuffer(random, false)
        val offset = random.nextIntOrZero(buffer.capacity())
        val size = random.nextIntOrZero(buffer.capacity() - offset)
        return RangedHeapByteBuffer(buffer, offset, size)
      }
    }
  }

  data class WholeDirectByteBuffer(val buffer: ByteBuffer) : XxHashArgument() {
    object ArgumentFactory : Factory<WholeDirectByteBuffer> {
      override fun create(random: Random): WholeDirectByteBuffer {
        return WholeDirectByteBuffer(newByteBuffer(random, true))
      }
    }
  }

  data class RangedDirectByteBuffer(val buffer: ByteBuffer, val offset: Int, val size: Int) : XxHashArgument() {
    object ArgumentFactory : Factory<RangedDirectByteBuffer> {
      override fun create(random: Random): RangedDirectByteBuffer {
        val buffer = newByteBuffer(random, true)
        val offset = random.nextIntOrZero(buffer.capacity())
        val size = random.nextIntOrZero(buffer.capacity() - offset)
        return RangedDirectByteBuffer(buffer, offset, size)
      }
    }
  }

  data class BooleanValue(val value: Boolean) : XxHashArgument() {
    object ArgumentFactory : Factory<BooleanValue> {
      override fun create(random: Random): BooleanValue {
        return BooleanValue(random.nextBoolean())
      }
    }
  }

  data class CharValue(val value: Char) : XxHashArgument() {
    object ArgumentFactory : Factory<CharValue> {
      override fun create(random: Random): CharValue {
        return CharValue(random.nextInt().toChar())
      }
    }
  }

  data class ByteValue(val value: Byte) : XxHashArgument() {
    object ArgumentFactory : Factory<ByteValue> {
      override fun create(random: Random): ByteValue {
        return ByteValue(random.nextInt().toByte())
      }
    }
  }

  data class ShortValue(val value: Short) : XxHashArgument() {
    object ArgumentFactory : Factory<ShortValue> {
      override fun create(random: Random): ShortValue {
        return ShortValue(random.nextInt().toShort())
      }
    }
  }

  data class IntValue(val value: Int) : XxHashArgument() {
    object ArgumentFactory : Factory<IntValue> {
      override fun create(random: Random): IntValue {
        return IntValue(random.nextInt())
      }
    }
  }

  data class LongValue(val value: Long) : XxHashArgument() {
    object ArgumentFactory : Factory<LongValue> {
      override fun create(random: Random): LongValue {
        return LongValue(random.nextLong())
      }
    }
  }

  data class FloatValue(val value: Float) : XxHashArgument() {
    object ArgumentFactory : Factory<FloatValue> {
      override fun create(random: Random): FloatValue {
        return FloatValue(random.nextFloat())
      }
    }
  }

  data class DoubleValue(val value: Double) : XxHashArgument() {
    object ArgumentFactory : Factory<DoubleValue> {
      override fun create(random: Random): DoubleValue {
        return DoubleValue(random.nextDouble())
      }
    }
  }

  interface Factory<out T : XxHashArgument> {
    fun create(random: Random): T

    fun createList(random: Random): List<T> {
      val size = random.nextInt(100)
      return ArrayList<T>(size).apply {
        repeat(size) {
          add(create(random))
        }
      }
    }
  }

  object ArgumentFactory : Factory<XxHashArgument> {
    private val factories = listOf(
        WholeArray.ArgumentFactory,
        RangedArray.ArgumentFactory,
        WholeHeapByteBuffer.ArgumentFactory,
        RangedHeapByteBuffer.ArgumentFactory,
        WholeDirectByteBuffer.ArgumentFactory,
        RangedDirectByteBuffer.ArgumentFactory,
        BooleanValue.ArgumentFactory,
        CharValue.ArgumentFactory,
        ByteValue.ArgumentFactory,
        ShortValue.ArgumentFactory,
        IntValue.ArgumentFactory,
        LongValue.ArgumentFactory,
        FloatValue.ArgumentFactory,
        DoubleValue.ArgumentFactory
    )

    override fun create(random: Random): XxHashArgument {
      return factories[random.nextInt(factories.size)].create(random)
    }
  }

  companion object {
    private fun Random.nextIntOrZero(bound: Int): Int {
      return if (bound <= 0) 0 else nextInt(bound)
    }

    private fun newArray(random: Random): ByteArray {
      val size = random.nextInt(100)
      val array = ByteArray(size)
      random.nextBytes(array)
      return array
    }

    private fun newByteBuffer(random: Random, direct: Boolean): ByteBuffer {
      val array = newArray(random)
      val buffer = if (direct) ByteBuffer.allocateDirect(array.size) else ByteBuffer.allocate(array.size)
      buffer.put(array)
      buffer.position(0)
      return buffer
    }
  }
}
