package com.appbopiotback.controllers

import com.appbopiotback.models.input.*
import com.appbopiotback.services.MqttConnectionCallback
import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.concurrent.ConcurrentLinkedQueue

@Singleton
class MessageController {

    private var mqttClient: MqttClient

    private var callback = object : MqttConnectionCallback {
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

    var messageQueue : ConcurrentLinkedQueue<MqttMessageActionResponse> = ConcurrentLinkedQueue()

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

    fun <T> sendMessage(request: T, topic: String, serializer : KSerializer<T>) {
        val json = Json { encodeDefaults = true }
        val jsonMessage = json.encodeToString(serializer, request)

        println(jsonMessage)
        mqttClient.publish(
            topic,
            MqttMessage(jsonMessage.toByteArray())
        )
    }

    fun subscribeOnTopic(topic: String, onMessageReceived: (String) -> Unit) {
        mqttClient.subscribe(topic) { _, mqttMessage ->
            val jsonMessage = mqttMessage.payload.toString(Charsets.UTF_8)
            onMessageReceived(jsonMessage)
        }
    }

}