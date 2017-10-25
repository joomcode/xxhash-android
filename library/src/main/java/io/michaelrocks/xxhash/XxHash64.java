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

package io.michaelrocks.xxhash;

import android.support.annotation.NonNull;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

public class XxHash64 implements Closeable {
  private static final long DEFAULT_SEED = 0L;

  private long state;

  public XxHash64() {
    this(DEFAULT_SEED);
  }

  public XxHash64(final long seed) {
    state = XxHash64Native.create(seed);
  }

  public static long hashForArray(@NonNull final byte[] buffer) {
    checkBufferNotNull(buffer);
    return XxHash64Native.hashForArray(buffer, 0, buffer.length, DEFAULT_SEED);
  }

  public static long hashForArray(@NonNull final byte[] buffer, final long seed) {
    checkBufferNotNull(buffer);
    return XxHash64Native.hashForArray(buffer, 0, buffer.length, seed);
  }

  public static long hashForArray(@NonNull final byte[] buffer, final int offset, final int size) {
    checkBufferNotNull(buffer);
    checkValidRange(buffer.length, offset, size);
    return XxHash64Native.hashForArray(buffer, offset, size, DEFAULT_SEED);
  }

  public static long hashForArray(@NonNull final byte[] buffer, final int offset, final int size, final long seed) {
    checkBufferNotNull(buffer);
    checkValidRange(buffer.length, offset, size);
    return XxHash64Native.hashForArray(buffer, offset, size, seed);
  }

  public static long hashForByteBuffer(@NonNull final ByteBuffer buffer) {
    checkBufferNotNull(buffer);
    return XxHash64Native.hashForByteBuffer(buffer, buffer.position(), buffer.limit(), DEFAULT_SEED);
  }

  public static long hashForArray(@NonNull final ByteBuffer buffer, final long seed) {
    checkBufferNotNull(buffer);
    return XxHash64Native.hashForByteBuffer(buffer, buffer.position(), buffer.limit(), seed);
  }

  public static long hashForArray(@NonNull final ByteBuffer buffer, final int offset, final int size) {
    checkBufferNotNull(buffer);
    checkValidRange(buffer.limit(), offset, size);
    return XxHash64Native.hashForByteBuffer(buffer, offset, size, DEFAULT_SEED);
  }

  public static long hashForArray(@NonNull final ByteBuffer buffer, final int offset, final int size, final long seed) {
    checkBufferNotNull(buffer);
    checkValidRange(buffer.limit(), offset, size);
    return XxHash64Native.hashForByteBuffer(buffer, offset, size, seed);
  }

  public void update(@NonNull final byte[] buffer) {
    checkBufferNotNull(buffer);
    XxHash64Native.updateWithArray(checkState(), buffer, 0, buffer.length);
  }

  public void update(@NonNull final byte[] buffer, final int offset, final int size) {
    checkBufferNotNull(buffer);
    checkValidRange(buffer.length, offset, size);
    XxHash64Native.updateWithArray(checkState(), buffer, offset, size);
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
    XxHash64Native.updateWithBoolean(checkState(), value);
  }

  public void update(final char value) {
    XxHash64Native.updateWithChar(checkState(), value);
  }

  public void update(final byte value) {
    XxHash64Native.updateWithByte(checkState(), value);
  }

  public void update(final short value) {
    XxHash64Native.updateWithShort(checkState(), value);
  }

  public void update(final int value) {
    XxHash64Native.updateWithInt(checkState(), value);
  }

  public void update(final long value) {
    XxHash64Native.updateWithLong(checkState(), value);
  }

  public void update(final float value) {
    XxHash64Native.updateWithFloat(checkState(), value);
  }

  public void update(final double value) {
    XxHash64Native.updateWithDouble(checkState(), value);
  }

  public long digest() {
    return XxHash64Native.digest(checkState());
  }

  public void reset() {
    XxHash64Native.reset(checkState(), DEFAULT_SEED);
  }

  public void reset(final long seed) {
    XxHash64Native.reset(checkState(), seed);
  }

  @Override
  public void close() throws IOException {
    if (state != 0L) {
      XxHash64Native.destroy(state);
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
      throw new IllegalStateException("XxHash64 instance is destroyed");
    }

    return state;
  }

  private void updateWithByteBufferInternal(@NonNull final ByteBuffer buffer, final int offset, final int size) {
    if (buffer.isDirect()) {
      XxHash64Native.updateWithByteBuffer(checkState(), buffer, offset, size);
    } else if (buffer.hasArray()) {
      XxHash64Native.updateWithArray(checkState(), buffer.array(), buffer.arrayOffset() + offset, size);
    } else {
      final int oldPosition = buffer.position();
      try {
        final byte[] array = new byte[size];
        buffer.position(offset);
        buffer.get(array);
        XxHash64Native.updateWithArray(checkState(), array, 0, size);
      } finally {
        buffer.position(oldPosition);
      }
    }
  }
}
