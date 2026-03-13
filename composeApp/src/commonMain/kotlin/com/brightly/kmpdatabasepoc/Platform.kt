package com.brightly.kmpdatabasepoc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform