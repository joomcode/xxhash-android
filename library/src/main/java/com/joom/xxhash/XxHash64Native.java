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

package com.joom.xxhash;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

class XxHash64Native extends XxHashNative {
  static native long hashForArray(@Nonnull byte[] input, int offset, int length, long seed);
  static native long hashForByteBuffer(@Nonnull ByteBuffer input, int offset, int length, long seed);
  static native long create(long seed);
  static native void updateWithArray(long state, @Nonnull byte[] input, int offset, int length);
  static native void updateWithByteBuffer(long state, @Nonnull ByteBuffer input, int offset, int length);
  static native void updateWithBoolean(long state, boolean input);
  static native void updateWithChar(long state, char input);
  static native void updateWithByte(long state, byte input);
  static native void updateWithShort(long state, short input);
  static native void updateWithInt(long state, int input);
  static native void updateWithLong(long state, long input);
  static native void updateWithFloat(long state, float input);
  static native void updateWithDouble(long state, double input);
  static native long digest(long state);
  static native void reset(long state, long seed);
  static native void destroy(long state);
}
