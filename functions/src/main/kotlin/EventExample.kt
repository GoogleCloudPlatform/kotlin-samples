// [START functions_helloworld_pubsub]
import java.util.Base64
import java.util.logging.Logger

class EventExample {
    companion object {
        var log: Logger = Logger.getLogger(EventExample::class.java.name)
    }

    fun helloPubSub(message: PubSubMessage) {
        val data = String(Base64.getDecoder().decode(message.data))
        log.info(data)
    }
}

data class PubSubMessage(
    val data: String,
    val messageId: String,
    val publishTime: String,
    val attributes: Map<String, String>,
)
// [END functions_helloworld_pubsub]
