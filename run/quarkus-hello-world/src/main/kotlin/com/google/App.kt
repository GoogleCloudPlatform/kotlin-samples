package com.google

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/")
class App {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun index(): String {
        return "hello, world"
    }
}
