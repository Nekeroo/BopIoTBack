package com.appbopiotback.mqtt.sender

import com.appbopiotback.models.JsonMessage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter


@ApplicationScoped
class MqttSender(@Channel("BopIoT-out") private val emitter : Emitter<String>) {

    private val objectMapper = jacksonObjectMapper()

    fun sendSomeMessage(request : JsonMessage) {
        val commandToSend = objectMapper.writeValueAsString(request)
        emitter.send(commandToSend)
    }

}