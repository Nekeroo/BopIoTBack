package com.appbopiotback

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import jakarta.ws.rs.core.MediaType

@QuarkusTest
class MessageControllerTest {

    @Test
    fun testSendMessageForActionEndpoint() {
        val requestBody = """
            {
                "message": "Commande traitée"
            }
        """.trimIndent()

        given()
            .contentType(MediaType.APPLICATION_JSON) // Spécifie que le contenu est du JSON
            .body(requestBody) // Corps de la requête JSON
            .`when`()
            .post("/hello/mqtt") // Appelle le POST à /hello/mqtt
            .then()
            .statusCode(200) // Vérifie que le code HTTP est 200
            .body(containsString("Message envoyé")) // Vérifie que la réponse contient "Message envoyé"
    }
}
