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

#include <stddef.h>
#include <assert.h>
#include "oom_error.h"

static volatile jclass g_outOfMemoryError;

static jclass getOutOfMemoryErrorClass(JNIEnv *env) {
  if (g_outOfMemoryError == NULL) {
    g_outOfMemoryError = (*env)->FindClass(env, "java/lang/OutOfMemoryError");
  }
  return g_outOfMemoryError;
}

void throwOutOfMemoryError(JNIEnv *env) {
  jclass outOfMemoryError = getOutOfMemoryErrorClass(env);
  if (outOfMemoryError == NULL) {
    assert("OutOfMemoryError not found");
  } else {
    (*env)->ThrowNew(env, outOfMemoryError, "Out of memory");
  }
}
