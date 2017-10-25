cmake_minimum_required(VERSION 3.4.1)

add_library( xxhash SHARED
  src/main/c/xxhash32_native.c
  src/main/c/xxhash64_native.c
  src/main/c/oom_error.c
  external/xxHash/xxhash.c
)
