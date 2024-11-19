package com.appbopiotback.controllers

import com.appbopiotback.models.Action
import com.appbopiotback.models.ActionResponse
import com.appbopiotback.models.JsonMessage
import io.smallrye.reactive.messaging.keyed.Keyed
import jakarta.inject.Singleton
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

@kotlinx.serialization.Serializable
data class MqttMessageFormat(
    val type: Int,
    val message: String
)

@kotlinx.serialization.Serializable
data class MqttMessageResponseFormat(
    val type: Int,
    val action : ActionResponse
)


@Singleton
class MessageController {

    private val mqttClient: MqttClient by lazy {
        val brokerUrl = "mqtt://broker.emqx.io:1883" // Remplacez par l'URL de votre broker MQTT
        val clientId = "mqttx_" + (System.currentTimeMillis() / 1000).toString()
        MqttClient(brokerUrl, clientId).apply {
            connect(MqttConnectOptions().apply {
                isCleanSession = true
            })
        }
    }

    fun sendMessage(request: JsonMessage, topic : String): Response {
        val messageToSend = MqttMessageFormat(type = request.type.id, message = request.message)
        val jsonMessage = Json.encodeToString(messageToSend)

        mqttClient.publish(
            topic,
            MqttMessage(jsonMessage.toByteArray())
        )
        return Response.status(200).entity("Message envoyé : ${request.message}").build()
    }

    fun waitForResponse(topic: String): MqttMessageResponseFormat? {
        val latch = java.util.concurrent.CountDownLatch(1)
        var receivedMessage: MqttMessageResponseFormat? = null

        mqttClient.subscribe(topic) { _, mqttMessage ->
            val json = mqttMessage.payload.toString(Charsets.UTF_8)
            val message = Json.decodeFromString<MqttMessageResponseFormat>(json)
            if (message.type == 2) { // Vérifie le type attendu
                receivedMessage = message
                latch.countDown()
            }
        }

        // Attendre un maximum de 5 secondes pour une réponse
        if (latch.await(5, java.util.concurrent.TimeUnit.SECONDS)) {
            return receivedMessage
        }
        return null
    }

}
