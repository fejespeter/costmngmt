package com.fejesp.costmanager.costmanagerapp;

import java.time.LocalDateTime;

import com.fejesp.costmanager.costmanagerapp.repository.model.Cost;
import com.fejesp.costmanager.costmanagerapp.testhelper.TestDataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
public class CostManagerStoreCostTests {

    @Autowired
    TestDataHelper testDataHelper;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    public void init() {
        testDataHelper.clearDb();
    }

    @Test
    void registerValidCostForUser() {
        Cost c = new Cost();
        c.setUserId("TestUser_1");
        c.setCost(100.0);
        c.setDate(LocalDateTime.now());

        Cost returned = storeCost(c);
        assertThat(returned.getId()).isNotNull();
    }

    @Test
    void registerInvalidCostForUser() {
        Cost c = new Cost();
        storeCostAndExceptStatusBadRequest(c);
        c.setUserId("TestUser_1");
        storeCostAndExceptStatusBadRequest(c);
        c.setDate(LocalDateTime.now());
        storeCostAndExceptStatusBadRequest(c);
        c.setCost(-100.0);
        storeCostAndExceptStatusBadRequest(c);
        c.setCost(100.0);
        Cost returned = storeCost(c);
        assertThat(returned.getId()).isNotNull();
    }

    @Test
    void registerMultipleCostForUser() {
        storeCost(createCost("TestUser_1"));
        assertThat(testDataHelper.getAllCostOfUser("TestUser_1").size()).isEqualTo(1);
        storeCost(createCost("TestUser_1"));
        assertThat(testDataHelper.getAllCostOfUser("TestUser_1").size()).isEqualTo(2);
    }

    @Test
    void registerMultipleCostForDifferentUser() {
        storeCost(createCost("TestUser_1"));
        storeCost(createCost("TestUser_1"));
        storeCost(createCost("TestUser_2"));
        assertThat(testDataHelper.getAllCostOfUser("TestUser_1").size()).isEqualTo(2);
        assertThat(testDataHelper.getAllCostOfUser("TestUser_2").size()).isEqualTo(1);
    }

    private Cost createCost(String userId) {
        Cost c = new Cost();
        c.setUserId(userId);
        c.setDate(LocalDateTime.now());
        c.setCost(100.0);
        return c;
    }

    private void storeCostAndExceptStatusBadRequest(Cost cost) {
        getRequest(cost).expectStatus().isBadRequest();
    }

    private WebTestClient.ResponseSpec getRequest(Cost c) {
        return webTestClient.put().uri("/storeCost")
                            .body(Mono.just(c), Cost.class)
                            .exchange();
    }

    private Cost storeCost(Cost c) {
        return getRequest(c)
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<Cost>() {
            })
            .returnResult()
            .getResponseBody();

    }

}
