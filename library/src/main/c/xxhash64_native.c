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

#include <stdlib.h>
#include <jni.h>
#include "oom_error.h"
#include "xxhash.h"

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunused-parameter"

JNIEXPORT jlong JNICALL Java_com_joom_xxhash_XxHash64Native_hashForArray(
    JNIEnv *env, jclass cls, jbyteArray buffer, jint offset, jint length, jlong seed) {
  uint8_t *input = (*env)->GetPrimitiveArrayCritical(env, buffer, 0);
  if (input == NULL) {
    throwOutOfMemoryError(env);
    return 0;
  }

  jlong h64 = (jlong) XXH64(input + offset, (size_t) length, (uint_least64_t) seed);
  (*env)->ReleasePrimitiveArrayCritical(env, buffer, input, 0);
  return h64;
}

JNIEXPORT jlong JNICALL Java_com_joom_xxhash_XxHash64Native_hashForByteBuffer(
    JNIEnv *env, jclass cls, jobject buffer, jint offset, jint length, jlong seed) {
  uint8_t *input = (*env)->GetDirectBufferAddress(env, buffer);
  if (input == NULL) {
    throwOutOfMemoryError(env);
    return 0;
  }

  return (jlong) XXH64(input + offset, (size_t) length, (uint64_t) seed);
}

JNIEXPORT jlong JNICALL Java_com_joom_xxhash_XxHash64Native_create(
    JNIEnv *env, jclass cls, jlong seed) {
  XXH64_state_t *state = XXH64_createState();
  if (XXH64_reset(state, (uint64_t) seed) != XXH_OK) {
    XXH64_freeState(state);
    throwOutOfMemoryError(env);
    return 0;
  }

  return (jlong) state;
}

JNIEXPORT void JNICALL Java_com_joom_xxhash_XxHash64Native_updateWithArray(
    JNIEnv *env, jclass cls, jlong state, jbyteArray buffer, jint offset, jint length) {
  uint8_t *input = (*env)->GetPrimitiveArrayCritical(env, buffer, 0);
  if (input == NULL) {
    throwOutOfMemoryError(env);
    return;
  }

  XXH64_update((XXH64_state_t *) state, input + offset, (size_t) length);
  (*env)->ReleasePrimitiveArrayCritical(env, buffer, input, 0);
}

JNIEXPORT void JNICALL Java_com_joom_xxhash_XxHash64Native_updateWithByteBuffer(
    JNIEnv *env, jclass cls, jlong state, jobject buffer, jint offset, jint length) {
  uint8_t *input = (*env)->GetDirectBufferAddress(env, buffer);
  if (input == NULL) {
    throwOutOfMemoryError(env);
    return;
  }

  XXH64_update((XXH64_state_t *) state, input + offset, (size_t) length);
}

JNIEXPORT void JNICALL Java_com_joom_xxhash_XxHash64Native_updateWithBoolean(
    JNIEnv *env, jclass cls, jlong state, jboolean value) {
  XXH64_update((XXH64_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_com_joom_xxhash_XxHash64Native_updateWithChar(
    JNIEnv *env, jclass cls, jlong state, jchar value) {
  XXH64_update((XXH64_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_com_joom_xxhash_XxHash64Native_updateWithByte(
    JNIEnv *env, jclass cls, jlong state, jbyte value) {
  XXH64_update((XXH64_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_com_joom_xxhash_XxHash64Native_updateWithShort(
    JNIEnv *env, jclass cls, jlong state, jshort value) {
  XXH64_update((XXH64_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_com_joom_xxhash_XxHash64Native_updateWithInt(
    JNIEnv *env, jclass cls, jlong state, jint value) {
  XXH64_update((XXH64_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_com_joom_xxhash_XxHash64Native_updateWithLong(
    JNIEnv *env, jclass cls, jlong state, jlong value) {
  XXH64_update((XXH64_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_com_joom_xxhash_XxHash64Native_updateWithFloat(
    JNIEnv *env, jclass cls, jlong state, jfloat value) {
  XXH64_update((XXH64_state_t *) state, &value, sizeof(value));
}

JNIEXPORT void JNICALL Java_com_joom_xxhash_XxHash64Native_updateWithDouble(
    JNIEnv *env, jclass cls, jlong state, jdouble value) {
  XXH64_update((XXH64_state_t *) state, &value, sizeof(value));
}

JNIEXPORT jlong JNICALL Java_com_joom_xxhash_XxHash64Native_digest(
    JNIEnv *env, jclass cls, jlong state) {
  return (jlong) XXH64_digest((XXH64_state_t *) state);
}

JNIEXPORT void JNICALL Java_com_joom_xxhash_XxHash64Native_reset(
    JNIEnv *env, jclass cls, jlong state, jlong seed) {
  XXH64_reset((XXH64_state_t *) state, (uint64_t) seed);
}

JNIEXPORT void JNICALL Java_com_joom_xxhash_XxHash64Native_destroy(
    JNIEnv *env, jclass cls, jlong state) {
  XXH64_freeState((XXH64_state_t *) state);
}

#pragma clang diagnostic pop
