package com.appbopiotback

import io.vertx.mutiny.core.http.HttpClientResponse
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import mqtt.sender.MqttSender

data class MessageRequest(
    val message: String
)

@Path("/hello")
class ExampleResource(private val mqttSender: MqttSender) {

    @POST
    @Path("/mqtt")
    @Consumes(MediaType.APPLICATION_JSON)
    fun hello(request: MessageRequest): Response {
        println(request)
        val message = request.message
        mqttSender.sendSomeMessage(message)
        return Response.status(200).entity("Message envoyé : $message").build()
    }

}
