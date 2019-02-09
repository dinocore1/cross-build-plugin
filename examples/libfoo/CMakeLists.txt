project(libFoo)

add_library(foo
    src/foo.cpp
    )

target_include_directories(foo PUBLIC include)
