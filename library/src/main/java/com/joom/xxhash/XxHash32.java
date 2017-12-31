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

import android.support.annotation.NonNull;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

public class XxHash32 implements Closeable {
  private static final int DEFAULT_SEED = 0;

  private long state;

  public XxHash32() {
    this(DEFAULT_SEED);
  }

  public XxHash32(final int seed) {
    state = XxHash32Native.create(seed);
  }

  public static int hashForArray(@NonNull final byte[] buffer) {
    checkBufferNotNull(buffer);
    return XxHash32Native.hashForArray(buffer, 0, buffer.length, DEFAULT_SEED);
  }

  public static int hashForArray(@NonNull final byte[] buffer, final int seed) {
    checkBufferNotNull(buffer);
    return XxHash32Native.hashForArray(buffer, 0, buffer.length, seed);
  }

  public static int hashForArray(@NonNull final byte[] buffer, final int offset, final int size) {
    checkBufferNotNull(buffer);
    checkValidRange(buffer.length, offset, size);
    return XxHash32Native.hashForArray(buffer, offset, size, DEFAULT_SEED);
  }

  public static int hashForArray(@NonNull final byte[] buffer, final int offset, final int size, final int seed) {
    checkBufferNotNull(buffer);
    checkValidRange(buffer.length, offset, size);
    return XxHash32Native.hashForArray(buffer, offset, size, seed);
  }

  public static int hashForByteBuffer(@NonNull final ByteBuffer buffer) {
    checkBufferNotNull(buffer);
    return XxHash32Native.hashForByteBuffer(buffer, buffer.position(), buffer.limit(), DEFAULT_SEED);
  }

  public static int hashForArray(@NonNull final ByteBuffer buffer, final int seed) {
    checkBufferNotNull(buffer);
    return XxHash32Native.hashForByteBuffer(buffer, buffer.position(), buffer.limit(), seed);
  }

  public static int hashForArray(@NonNull final ByteBuffer buffer, final int offset, final int size) {
    checkBufferNotNull(buffer);
    checkValidRange(buffer.limit(), offset, size);
    return XxHash32Native.hashForByteBuffer(buffer, offset, size, DEFAULT_SEED);
  }

  public static int hashForArray(@NonNull final ByteBuffer buffer, final int offset, final int size, final int seed) {
    checkBufferNotNull(buffer);
    checkValidRange(buffer.limit(), offset, size);
    return XxHash32Native.hashForByteBuffer(buffer, offset, size, seed);
  }

  public void update(@NonNull final byte[] buffer) {
    checkBufferNotNull(buffer);
    XxHash32Native.updateWithArray(checkState(), buffer, 0, buffer.length);
  }

  public void update(@NonNull final byte[] buffer, final int offset, final int size) {
    checkBufferNotNull(buffer);
    checkValidRange(buffer.length, offset, size);
    XxHash32Native.updateWithArray(checkState(), buffer, offset, size);
  }

  public void update(@NonNull final ByteBuffer buffer) {
    checkBufferNotNull(buffer);
    updateWithByteBufferInternal(buffer, buffer.position(), buffer.remaining());
  }

  public void update(@NonNull final ByteBuffer buffer, final int offset, final int size) {
    checkBufferNotNull(buffer);
    checkValidRange(buffer.limit(), offset, size);
    updateWithByteBufferInternal(buffer, offset, size);
  }

  public void update(final boolean value) {
    XxHash32Native.updateWithBoolean(checkState(), value);
  }

  public void update(final char value) {
    XxHash32Native.updateWithChar(checkState(), value);
  }

  public void update(final byte value) {
    XxHash32Native.updateWithByte(checkState(), value);
  }

  public void update(final short value) {
    XxHash32Native.updateWithShort(checkState(), value);
  }

  public void update(final int value) {
    XxHash32Native.updateWithInt(checkState(), value);
  }

  public void update(final long value) {
    XxHash32Native.updateWithLong(checkState(), value);
  }

  public void update(final float value) {
    XxHash32Native.updateWithFloat(checkState(), value);
  }

  public void update(final double value) {
    XxHash32Native.updateWithDouble(checkState(), value);
  }

  public int digest() {
    return XxHash32Native.digest(checkState());
  }

  public void reset() {
    XxHash32Native.reset(checkState(), DEFAULT_SEED);
  }

  public void reset(final int seed) {
    XxHash32Native.reset(checkState(), seed);
  }

  @Override
  public void close() throws IOException {
    if (state != 0L) {
      XxHash32Native.destroy(state);
      state = 0L;
    }
  }

  @Override
  protected void finalize() throws Throwable {
    try {
      close();
    } finally {
      super.finalize();
    }
  }

  private static void checkBufferNotNull(final Object buffer) {
    if (buffer == null) {
      throw new NullPointerException("Buffer cannot be null");
    }
  }

  private static void checkValidRange(final int bufferSize, final int rangeStart, final int rangeSize) {
    if (rangeSize < 0) {
      throw new IllegalArgumentException("Size cannot be negative");
    }

    final int rangeEnd = rangeStart + rangeSize;
    if (rangeStart < 0 || rangeEnd > bufferSize) {
      throw new IndexOutOfBoundsException(
          "Range [" + rangeStart + ", " + rangeEnd + ") doesn't fit the buffer [0, " + bufferSize + ")");
    }
  }

  private long checkState() {
    if (state == 0L) {
      throw new IllegalStateException("XxHash32 instance is destroyed");
    }

    return state;
  }

  private void updateWithByteBufferInternal(@NonNull final ByteBuffer buffer, final int offset, final int size) {
    if (buffer.isDirect()) {
      XxHash32Native.updateWithByteBuffer(checkState(), buffer, offset, size);
    } else if (buffer.hasArray()) {
      XxHash32Native.updateWithArray(checkState(), buffer.array(), buffer.arrayOffset() + offset, size);
    } else {
      final int oldPosition = buffer.position();
      try {
        final byte[] array = new byte[size];
        buffer.position(offset);
        buffer.get(array);
        XxHash32Native.updateWithArray(checkState(), array, 0, size);
      } finally {
        buffer.position(oldPosition);
      }
    }
  }
}
