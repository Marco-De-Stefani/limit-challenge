package com.deste.gateway.filters;

import com.deste.gateway.domain.knowledgegraph.KeyService;
import com.deste.gateway.domain.user.UserService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"httpbin=http://localhost:${wiremock.server.port}"})
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class RateLimitFilterTest {
    @Autowired
    protected WebTestClient webClient;

    @MockBean
    private UserService userService;

    @MockBean
    private KeyService keyService;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/white"))
                .willReturn(WireMock.aResponse()
                        .withHeader("X-API-Group", "api_white")
                        .withHeader("X-API-Cost", "1")
                        .withBody("White")
                        .withStatus(200)));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void whenLimitReachedThenReturn402ForUser() {
        when(userService.isValidUser("mario@rossi.it")).thenReturn(true);
        when(userService.isUserLimitReached("mario@rossi.it", "api_white")).thenReturn(true);

        webClient.get().uri("/white")
                .header("Authorization", "Bearer mario@rossi.it")
                .exchange()
                .expectStatus().isEqualTo(402);
    }

    @Test
    void whenLimitReachedThenReturn402ForKey() {
        when(keyService.isValidKey("keyName")).thenReturn(true);
        when(keyService.isKeyLimitReached("keyName", "api_white")).thenReturn(true);

        webClient.get().uri("/white")
                .header("Authorization", "Key keyName")
                .exchange()
                .expectStatus().isEqualTo(402);
    }

    @Test
    void returnCorrectHeadersForKeyWhenLimitNotReached() {
        when(keyService.isValidKey("keyName")).thenReturn(true);
        when(keyService.isKeyLimitReached("keyName", "api_white")).thenReturn(false);
        when(keyService.getConsumedFor("keyName", "api_white")).thenReturn("1");
        when(keyService.getTotalFor("keyName", "api_white")).thenReturn("10");

        webClient.get().uri("/white")
                .header("Authorization", "Key keyName")
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .valueEquals("X-API-Group-Consumed", "1").expectHeader()
                .valueEquals("X-API-Group-Key-Limit", "10").expectHeader()
                .valueEquals("X-API-Group", "api_white").expectHeader()
                .valueEquals("X-API-Cost", "1");
    }

    @Test
    void returnCorrectHeadersForUserWhenLimitNotReached() {
        when(userService.isValidUser("email")).thenReturn(true);
        when(userService.isUserLimitReached("email", "api_white")).thenReturn(false);
        when(userService.getConsumedFor("email", "api_white")).thenReturn("2");
        when(userService.getTotalFor("email", "api_white")).thenReturn("10");

        webClient.get().uri("/white")
                .header("Authorization", "Bearer email")
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .valueEquals("X-API-Group-Consumed", "2").expectHeader()
                .valueEquals("X-API-Group-User-Limit", "10").expectHeader()
                .valueEquals("X-API-Group", "api_white").expectHeader()
                .valueEquals("X-API-Cost", "1");
    }
}