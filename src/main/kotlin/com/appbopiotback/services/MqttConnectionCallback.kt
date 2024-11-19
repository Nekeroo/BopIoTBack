package com.appbopiotback.services

import java.lang.Exception

interface MqttConnectionCallback {
    fun onSuccess()
    fun onFailure(exception: Exception)
    fun connectionLost(cause : Throwable)
}