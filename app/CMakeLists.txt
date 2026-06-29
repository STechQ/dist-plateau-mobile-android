cmake_minimum_required(VERSION 3.17)
project(plateau_native)

add_library(
        plateau_native
        SHARED
        src/main/cpp/plateau_native.cpp)

target_link_options(plateau_native PRIVATE "-Wl,-z,max-page-size=16384")
