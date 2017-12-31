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

package com.joom.xxhash

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Random

@RunWith(AndroidJUnit4::class)
class XxHash64Test {
  @Test
  fun testWholeArray() {
    repeat(100) { checkFactory(XxHashArgument.WholeArray.ArgumentFactory) }
  }

  @Test
  fun testRangedArray() {
    repeat(100) { checkFactory(XxHashArgument.RangedArray.ArgumentFactory) }
  }

  @Test
  fun testWholeHeapByteBuffer() {
    repeat(100) { checkFactory(XxHashArgument.WholeHeapByteBuffer.ArgumentFactory) }
  }

  @Test
  fun testRangedHeapByteBuffer() {
    repeat(100) { checkFactory(XxHashArgument.RangedHeapByteBuffer.ArgumentFactory) }
  }

  @Test
  fun testWholeDirectByteBuffer() {
    repeat(100) { checkFactory(XxHashArgument.WholeDirectByteBuffer.ArgumentFactory) }
  }

  @Test
  fun testRangedDirectByteBuffer() {
    repeat(100) { checkFactory(XxHashArgument.RangedDirectByteBuffer.ArgumentFactory) }
  }

  @Test
  fun testBooleanValue() {
    repeat(100) { checkFactory(XxHashArgument.BooleanValue.ArgumentFactory) }
  }

  @Test
  fun testCharValue() {
    repeat(100) { checkFactory(XxHashArgument.CharValue.ArgumentFactory) }
  }

  @Test
  fun testByteValue() {
    repeat(100) { checkFactory(XxHashArgument.ByteValue.ArgumentFactory) }
  }

  @Test
  fun testShortValue() {
    repeat(100) { checkFactory(XxHashArgument.ShortValue.ArgumentFactory) }
  }

  @Test
  fun testIntValue() {
    repeat(100) { checkFactory(XxHashArgument.IntValue.ArgumentFactory) }
  }

  @Test
  fun testLongValue() {
    repeat(100) { checkFactory(XxHashArgument.LongValue.ArgumentFactory) }
  }

  @Test
  fun testFloatValue() {
    repeat(100) { checkFactory(XxHashArgument.FloatValue.ArgumentFactory) }
  }

  @Test
  fun testDoubleValue() {
    repeat(100) { checkFactory(XxHashArgument.DoubleValue.ArgumentFactory) }
  }

  @Test
  fun testRandom() {
    repeat(100) { checkFactory(XxHashArgument.ArgumentFactory) }
  }

  private fun checkFactory(factory: XxHashArgument.Factory<*>) {
    val random = Random()
    val arguments = factory.createList(random)
    val seed = if (random.nextBoolean()) random.nextLong() else null
    checkArguments(arguments, seed)
  }

  private fun checkArguments(arguments: List<XxHashArgument>, seed: Long?) {
    val actualHash = XxHash64Hasher(seed).run {
      update(arguments)
      hash.digest()
    }

    val expectedHash = ByteArrayHasher().run {
      update(arguments)
      if (seed == null) XxHash64.hashForArray(toByteArray()) else XxHash64.hashForArray(toByteArray(), seed)
    }

    if (expectedHash != actualHash) {
      Assert.fail("Expected $expectedHash, actual $actualHash, seed = $seed, arguments = $arguments")
    }
  }
}
