// [START functions_helloworld_get]
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HttpExample {
    fun helloWorld(req: HttpServletRequest, resp: HttpServletResponse) {
        with(resp.writer) {
            println("Hello World!")
        }
    }
}
// [END functions_helloworld_get]
