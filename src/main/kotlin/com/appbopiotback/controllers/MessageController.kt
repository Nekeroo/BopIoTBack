package com.appbopiotback.controllers

import com.appbopiotback.models.JsonMessage
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import com.appbopiotback.mqtt.sender.MqttSender


@Path("/message-controller")
class MessageController(private val mqttSender: MqttSender) {

    @POST
    @Path("/mqtt")
    @Consumes(MediaType.APPLICATION_JSON)
    fun sendMessage(request: JsonMessage): Response {
        println(request)
        mqttSender.sendSomeMessage(request)
        return Response.status(200).entity("Message envoyé : ${request.message}").build()
    }

}
