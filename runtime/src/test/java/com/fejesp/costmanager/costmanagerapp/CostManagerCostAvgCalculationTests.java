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
public class CostManagerCostAvgCalculationTests {

    @Autowired
    TestDataHelper testDataHelper;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    public void init() {
        testDataHelper.clearDb();
    }

    @Test
    void avgOfCostsTest() {
        /**
         * day 1 - 200
         * day 2 - 100
         * day 3 - 200
         * day 4 - 100
         *
         * Avg: overall 600 spend on 4 days => 150 / day
         */
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusDays(1));
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusDays(2));
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusDays(2));
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusDays(3));

        assertThat(getAvgOfCostsForUser("TestUser_1")).isEqualTo(150.0);
    }

    @Test
    void avgOfCostsEmptyDaysTest() {
        /**
         * day 1 - 200
         * day 2 - 0
         * day 3 - 200
         * day 4 - 0
         * day 5 - 100
         *
         * Avg: overall 500 spend on 5 days => 100 / day
         */
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusDays(2));
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusDays(2));
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusDays(4));

        assertThat(getAvgOfCostsForUser("TestUser_1")).isEqualTo(100.0);
    }

    @Test
    void avgOfCostsMultipleUsersTest() {
        /**
         * day 1 - 200
         * day 2 - 0
         * day 3 - 100
         *
         * Avg: overall 300 spend on 3 days => 100 / day
         */
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusDays(2));
        testDataHelper.insertTestCostForUser("TestUser_2", LocalDateTime.now().minusDays(1));
        testDataHelper.insertTestCostForUser("TestUser_3", LocalDateTime.now().minusDays(2));

        assertThat(getAvgOfCostsForUser("TestUser_1")).isEqualTo(100.0);
    }

    private WebTestClient.ResponseSpec getRequest(final String userId) {
        return webTestClient.post().uri(builder -> builder.path("/avgOfCosts/{userId}").build(userId))
                            .exchange();
    }

    private Double getAvgOfCostsForUser(final String userId) {
        return getRequest(userId)
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<Double>() {
            })
            .returnResult()
            .getResponseBody();
    }

}
