/*
 * Copyright 2019 SIA Joom
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

public class XxHash {
  private static final String NATIVE_LIBRARY_NAME = "xxhash";

  private static final Object lock = new Object();

  @GuardedBy("lock")
  private static NativeLibraryLoader nativeLibraryLoader;
  @GuardedBy("lock")
  private static boolean isNativeLibraryLoaded = false;

  public static void setNativeLibraryLoader(@Nullable final NativeLibraryLoader loader) {
    synchronized (lock) {
      if (isNativeLibraryLoaded) {
        throw new IllegalStateException("Native library has already been loaded");
      }

      nativeLibraryLoader = loader;
    }
  }

  static void loadNativeLibrary() {
    synchronized (lock) {
      if (isNativeLibraryLoaded) {
        return;
      }

      if (nativeLibraryLoader != null) {
        nativeLibraryLoader.loadLibrary(NATIVE_LIBRARY_NAME);
      } else {
        System.loadLibrary(NATIVE_LIBRARY_NAME);
      }
      isNativeLibraryLoaded = true;
    }
  }

  public interface NativeLibraryLoader {
    void loadLibrary(@Nonnull String libraryName);
  }
}
