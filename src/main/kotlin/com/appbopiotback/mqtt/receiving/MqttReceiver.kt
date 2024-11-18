package com.appbopiotback.mqtt.receiving

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming
import java.util.logging.Logger

@ApplicationScoped
class MqttReceiver {

    private val logger = Logger.getLogger(MqttReceiver::class.java.name)

    @Incoming("BopIoT-in")
    fun receiveCommand(byteArray: ByteArray) {
        logger.info("Receiving command : ${String(byteArray)} ")
    }

}