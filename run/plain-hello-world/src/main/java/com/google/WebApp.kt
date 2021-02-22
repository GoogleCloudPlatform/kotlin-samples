package com.google

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

fun main() {
    val port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"))
    val server = HttpServer.create(InetSocketAddress(port), 0)

    server.createContext("/") { handler ->
        val response = "hello, world".toByteArray()
        handler.sendResponseHeaders(200, response.size.toLong())
        handler.responseBody.write(response)
    }

    println("Listening at http://localhost:$port")

    server.start()
}
