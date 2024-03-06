package com.deste.gateway.filters;

import com.deste.gateway.domain.knowledgegraph.Key;
import com.deste.gateway.domain.knowledgegraph.KeyRepository;
import com.deste.gateway.domain.knowledgegraph.KnowledgeGraph;
import com.deste.gateway.domain.knowledgegraph.KnowledgeGraphRepository;
import com.deste.gateway.domain.user.User;
import com.deste.gateway.domain.user.UserRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"httpbin=http://localhost:${wiremock.server.port}"})
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class AuthFilterTest {

    @Autowired
    protected WebTestClient webClient;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private KeyRepository keyRepository;

    @Autowired
    private KnowledgeGraphRepository knowledgeGraphRepository;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/white"))
                .willReturn(WireMock.aResponse()
                        .withBody("White")
                        .withStatus(200)));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void loginWithExistingUser() {
        userRepo.save(User.builder()
                .email("mario@rossi.it")
                .build());

        webClient.get().uri("/white")
                .header("Authorization", "Bearer mario@rossi.it")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("White");
    }

    @Test
    void loginWithExistingKey() {
        User marioRossi = userRepo.save(User.builder()
                .email("mario@rossi.it")
                .build());
        KnowledgeGraph knowledgeGraph = knowledgeGraphRepository.save(KnowledgeGraph.builder().user(marioRossi).build());
        keyRepository.save(Key.builder().name("keyName").knowledgeGraph(knowledgeGraph).build());

        webClient.get().uri("/white")
                .header("Authorization", "Key keyName")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("White");
    }

    @Test
    void loginWithNonExistingUser() {
        webClient.get().uri("/white")
                .header("Authorization", "Bearer test")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void loginWithNonExistingKey() {
        webClient.get().uri("/white")
                .header("Authorization", "Key test")
                .exchange()
                .expectStatus().isUnauthorized();
    }

}