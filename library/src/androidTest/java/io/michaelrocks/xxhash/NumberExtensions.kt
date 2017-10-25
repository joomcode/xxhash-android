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

@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package io.michaelrocks.xxhash

import java.lang.Character as JavaChar
import java.lang.Double as JavaDouble
import java.lang.Float as JavaFloat
import java.lang.Integer as JavaInt
import java.lang.Long as JavaLong
import java.lang.Short as JavaShort

fun Char.reverseBytes(): Char {
  return JavaChar.reverseBytes(this)
}

fun Short.reverseBytes(): Short {
  return JavaShort.reverseBytes(this)
}

fun Int.reverseBytes(): Int {
  return JavaInt.reverseBytes(this)
}

fun Long.reverseBytes(): Long {
  return JavaLong.reverseBytes(this)
}

fun Float.toRawIntBits(): Int {
  return JavaFloat.floatToRawIntBits(this)
}

fun Double.toRawLongBits(): Long {
  return JavaDouble.doubleToRawLongBits(this)
}
