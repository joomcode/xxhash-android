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

class XxHash64Hasher(seed: Long?) : Hasher {
  val hash = if (seed == null) XxHash64() else XxHash64(seed)

  override fun update(argument: XxHashArgument) {
    return when (argument) {
      is XxHashArgument.WholeArray ->
        hash.update(argument.array)
      is XxHashArgument.RangedArray ->
        hash.update(argument.array, argument.offset, argument.size)
      is XxHashArgument.WholeHeapByteBuffer ->
        hash.update(argument.buffer)
      is XxHashArgument.RangedHeapByteBuffer ->
        hash.update(argument.buffer, argument.offset, argument.size)
      is XxHashArgument.WholeDirectByteBuffer ->
        hash.update(argument.buffer)
      is XxHashArgument.RangedDirectByteBuffer ->
        hash.update(argument.buffer, argument.offset, argument.size)
      is XxHashArgument.BooleanValue ->
        hash.update(argument.value)
      is XxHashArgument.CharValue ->
        hash.update(argument.value)
      is XxHashArgument.ByteValue ->
        hash.update(argument.value)
      is XxHashArgument.ShortValue ->
        hash.update(argument.value)
      is XxHashArgument.IntValue ->
        hash.update(argument.value)
      is XxHashArgument.LongValue ->
        hash.update(argument.value)
      is XxHashArgument.FloatValue ->
        hash.update(argument.value)
      is XxHashArgument.DoubleValue ->
        hash.update(argument.value)
    }
  }
}
