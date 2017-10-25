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
#include <jni.h>
#include "oom_error.h"
#include "xxhash.h"

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunused-parameter"

JNIEXPORT jint JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_hashForArray(
    JNIEnv *env, jclass cls, jbyteArray buffer, jint offset, jint length, jint seed) {
  uint8_t *input = (*env)->GetPrimitiveArrayCritical(env, buffer, 0);
  if (input == NULL) {
    throwOutOfMemoryError(env);
    return 0;
  }

  jint h32 = XXH32(input + offset, (size_t) length, (uint32_t) seed);
  (*env)->ReleasePrimitiveArrayCritical(env, buffer, input, 0);
  return h32;
}

JNIEXPORT jint JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_hashForByteBuffer(
    JNIEnv *env, jclass cls, jobject buffer, jint offset, jint length, jint seed) {
  uint8_t *input = (*env)->GetDirectBufferAddress(env, buffer);
  if (input == NULL) {
    throwOutOfMemoryError(env);
    return 0;
  }

  return XXH32(input + offset, (size_t) length, (uint32_t) seed);
}

JNIEXPORT jlong JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_create(
    JNIEnv *env, jclass cls, jint seed) {
  XXH32_state_t *state = XXH32_createState();
  if (XXH32_reset(state, (uint32_t) seed) != XXH_OK) {
    XXH32_freeState(state);
    throwOutOfMemoryError(env);
    return 0;
  }

  return (jlong) state;
}

JNIEXPORT void JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_updateWithArray(
    JNIEnv *env, jclass cls, jlong state, jbyteArray buffer, jint offset, jint length) {
  uint8_t *input = (*env)->GetPrimitiveArrayCritical(env, buffer, 0);
  if (input == NULL) {
    throwOutOfMemoryError(env);
    return;
  }

  XXH32_update((XXH32_state_t *) state, input + offset, (size_t) length);
  (*env)->ReleasePrimitiveArrayCritical(env, buffer, input, 0);
}

JNIEXPORT void JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_updateWithByteBuffer(
    JNIEnv *env, jclass cls, jlong state, jobject buffer, jint offset, jint length) {
  uint8_t *input = (*env)->GetDirectBufferAddress(env, buffer);
  if (input == NULL) {
    throwOutOfMemoryError(env);
    return;
  }

  XXH32_update((XXH32_state_t *) state, input + offset, (size_t) length);
}

JNIEXPORT void JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_updateWithBoolean(
    JNIEnv *env, jclass cls, jlong state, jboolean value) {
  XXH32_update((XXH32_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_updateWithChar(
    JNIEnv *env, jclass cls, jlong state, jchar value) {
  XXH32_update((XXH32_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_updateWithByte(
    JNIEnv *env, jclass cls, jlong state, jbyte value) {
  XXH32_update((XXH32_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_updateWithShort(
    JNIEnv *env, jclass cls, jlong state, jshort value) {
  XXH32_update((XXH32_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_updateWithInt(
    JNIEnv *env, jclass cls, jlong state, jint value) {
  XXH32_update((XXH32_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_updateWithLong(
    JNIEnv *env, jclass cls, jlong state, jlong value) {
  XXH32_update((XXH32_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_updateWithFloat(
    JNIEnv *env, jclass cls, jlong state, jfloat value) {
  XXH32_update((XXH32_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_updateWithDouble(
    JNIEnv *env, jclass cls, jlong state, jdouble value) {
  XXH32_update((XXH32_state_t *) state, &value, sizeof(value));
}

JNIEXPORT jint JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_digest(
    JNIEnv *env, jclass cls, jlong state) {
  return XXH32_digest((XXH32_state_t *) state);
}

JNIEXPORT void JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_reset(
    JNIEnv *env, jclass cls, jlong state, jint seed) {
  XXH32_reset((XXH32_state_t *) state, (uint32_t) seed);
}

JNIEXPORT void JNICALL Java_io_michaelrocks_xxhash_XxHash32Native_destroy(
    JNIEnv *env, jclass cls, jlong state) {
  XXH32_freeState((XXH32_state_t *) state);
}

#pragma clang diagnostic pop
