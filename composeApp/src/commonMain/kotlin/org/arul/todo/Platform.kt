package org.arul.todo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform