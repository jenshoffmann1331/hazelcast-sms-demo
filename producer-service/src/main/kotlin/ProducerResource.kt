import com.hazelcast.core.HazelcastInstance
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path

data class SmsMessage(
    val id: String,
    val recipient: String,
    val text: String,
)

@Path("/producer")
class ProducerResource(
    val hazelcastInstance: HazelcastInstance,
) {

    @POST
    fun post(): String {
        val queue = hazelcastInstance.getQueue<SmsMessage>("sms-queue")
        val counter = hazelcastInstance.cpSubsystem.getAtomicLong("sms-counter")
        val id = counter.incrementAndGet().toString()
        val sms = SmsMessage(
            id = id,
            recipient = "1234567890",
            text = "Hello, this is a test message!",
        )
        queue.offer(sms)
        return "Message queued with ID ${sms.id}"
    }
}
