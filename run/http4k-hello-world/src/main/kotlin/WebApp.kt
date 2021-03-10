import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer

val app = routes(
    "/" bind GET to {
        Response(OK).body("hello, world")
    }
)

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080

    val server = PrintRequest()
        .then(app)
        .asServer(Undertow(port)).start()

    println("Server started on " + server.port())
}
