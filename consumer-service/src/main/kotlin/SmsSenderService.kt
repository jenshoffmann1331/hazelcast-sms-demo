import jakarta.enterprise.context.ApplicationScoped
import org.jsmpp.bean.Alphabet
import org.jsmpp.bean.BindType
import org.jsmpp.bean.ESMClass
import org.jsmpp.bean.GeneralDataCoding
import org.jsmpp.bean.NumberingPlanIndicator
import org.jsmpp.bean.RegisteredDelivery
import org.jsmpp.bean.TypeOfNumber
import org.jsmpp.session.SMPPSession

@ApplicationScoped
class SmsSenderService {
    fun sendSms(sms: SmsMessage) {
        val session = SMPPSession()
        try {
            session.connectAndBind(
                "smppsim",
                2775,
                BindType.BIND_TX,
                "bitsense",
                "pass1234",
                "",
                TypeOfNumber.INTERNATIONAL,
                NumberingPlanIndicator.ISDN,
                ""
            )
            val messageId = session.submitShortMessage(
                "CMT",
                TypeOfNumber.INTERNATIONAL,
                NumberingPlanIndicator.ISDN,
                "SenderID",
                TypeOfNumber.INTERNATIONAL,
                NumberingPlanIndicator.ISDN,
                sms.recipient,
                ESMClass(),
                0x00,
                0x00,
                null,
                null,
                RegisteredDelivery(),
                0x00,
                GeneralDataCoding(Alphabet.ALPHA_DEFAULT),
                0x00,
                sms.text.toByteArray(Charsets.ISO_8859_1)
            )
            println("Message sent with ID: $messageId")
        } catch (e: Exception) {
            println("Failed to send message: ${e.message}")
        } finally {
            session.unbindAndClose()
        }
    }
}
