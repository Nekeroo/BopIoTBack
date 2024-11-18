package mqtt.sender

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import java.util.*

data class JsonMessage(val message: String)

@ApplicationScoped
class MqttSender(@Channel("BopIoT-out") private val emitter : Emitter<String>) {

    private val objectMapper = jacksonObjectMapper()

    fun sendSomeMessage(value: String) {
        val jsonMessage = JsonMessage(value)
        val commandToSend = objectMapper.writeValueAsString(jsonMessage)
        emitter.send(commandToSend)
    }

}