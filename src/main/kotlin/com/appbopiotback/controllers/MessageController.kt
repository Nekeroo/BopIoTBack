package com.appbopiotback.controllers

import com.appbopiotback.models.input.*
import com.appbopiotback.services.MqttConnectionCallback
import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

@Singleton
class MessageController {

    private var mqttClient: MqttClient

    var callback = object : MqttConnectionCallback {
        override fun onSuccess() {
            println("Connexion réussie au broker MQTT !")
        }

        override fun onFailure(exception: Exception) {
            println("Échec de la connexion au broker MQTT : ${exception.message}")
        }

        override fun connectionLost(cause: Throwable) {
            tryReconnect()
        }
    }

    init {

        val brokerUrl = "tcp://broker.emqx.io:1883" // Remplacez par l'URL de votre broker MQTT
        val clientId = "mqttx_" + (System.currentTimeMillis() / 1000).toString()
        println(clientId)

        mqttClient = MqttClient(brokerUrl, clientId).apply {
            try {
                connect(MqttConnectOptions().apply {
                    isCleanSession = true
                })
                callback.onSuccess()
            }catch (e : Exception){
                callback.onFailure(e)
            }
        }

    }

    fun tryReconnect() {
        try {
            mqttClient.connect(MqttConnectOptions().apply {
                isCleanSession = true
            })
            println("Reconnecté au broker MQTT.")
        } catch (e: Exception) {
            println("Reconnexion échouée : ${e.message}")
        }
    }

    @PostConstruct
    fun init() {
        mqttClient.connect()
    }

    fun sendMessageForInformation(request : MqttMessageLibelle, topic : String) {
        val json = Json { encodeDefaults = true }
        val jsonMessage = json.encodeToString(MqttMessageLibelle.serializer(), request)

        println(jsonMessage)
        mqttClient.publish(
            topic,
            MqttMessage(jsonMessage.toByteArray())
        )
    }

//    fun sendMessageForAction(request: MqttMessageAction, topic : String) {
//        println(request)
//        val json = Json { encodeDefaults = true }
//        val jsonMessage = json.encodeToString(MqttMessageAction.serializer(), request)
//
//        println(jsonMessage)
//        mqttClient.publish(
//            topic,
//            MqttMessage(jsonMessage.toByteArray())
//        )
//
//    }

    fun <T> sendMessage(request: T, topic: String) {
        val json = Json { encodeDefaults = true }
        val jsonMessage = json.encodeToString(request)

        println(jsonMessage)
        mqttClient.publish(
            topic,
            MqttMessage(jsonMessage.toByteArray())
        )
    }

    fun sendMessageForEndGame(request: MqttMessageEndGame, topic : String) {
        val json = Json { encodeDefaults = true }
        val jsonMessage = json.encodeToString(MqttMessageEndGame.serializer(), request)

        println(jsonMessage)
        mqttClient.publish(
            topic,
            MqttMessage(jsonMessage.toByteArray())
        )

    }

    fun sendMessageForInfoAction(request: MqttMessageInfoAction, topic : String) {
        val json = Json { encodeDefaults = true }
        val jsonMessage = json.encodeToString(MqttMessageInfoAction.serializer(), request)

        println(jsonMessage)
        mqttClient.publish(
            topic,
            MqttMessage(jsonMessage.toByteArray())
        )

    }

    fun waitForResponse(topic: String): MqttMessageActionResponse? {
        val latch = java.util.concurrent.CountDownLatch(1)
        var receivedMessage: MqttMessageActionResponse? = null

        mqttClient.subscribe(topic) { _, mqttMessage ->
            val json = mqttMessage.payload.toString(Charsets.UTF_8)
            val message = Json.decodeFromString<MqttMessageActionResponse>(json)
            if (message.type == 2) { // Vérifie le type attendu
                receivedMessage = message
                latch.countDown()
            }
        }


        if (latch.await(10, java.util.concurrent.TimeUnit.SECONDS)) {
            return receivedMessage
        }
        return null
    }

}
