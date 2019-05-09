import javax.servlet.http.*

class HttpExample {
    fun helloWorld(req: HttpServletRequest, resp: HttpServletResponse) {
        with(resp.writer) {
            println("Hello World!")
        }
    }
}
