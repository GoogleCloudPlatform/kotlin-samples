package com.google

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType


@Path("/")
class App {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun index(): String {
        return "hello, world"
    }

}
