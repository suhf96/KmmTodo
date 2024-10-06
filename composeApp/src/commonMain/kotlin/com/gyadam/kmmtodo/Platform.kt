package com.gyadam.kmmtodo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform