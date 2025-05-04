import com.hazelcast.core.HazelcastInstance
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped

data class SmsMessage(
    val id: String,
    val recipient: String,
    val text: String,
)

@ApplicationScoped
class SmsConsumer(
    private val hazelcastInstance: HazelcastInstance,
    private val smsSenderService: SmsSenderService,
) {

    private val queue by lazy {
        hazelcastInstance.getQueue<SmsMessage>("sms-queue")
    }

    @Scheduled(every = "5s")
    fun consume() {
        val sms = queue.poll()
        if (sms != null) {
            println("Consumed message: $sms")
            smsSenderService.sendSms(sms)
        } else {
            println("No messages to consume")
        }
    }
}
