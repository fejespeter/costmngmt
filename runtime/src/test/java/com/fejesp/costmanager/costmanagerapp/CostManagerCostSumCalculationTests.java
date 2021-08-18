package com.fejesp.costmanager.costmanagerapp;

import java.time.LocalDateTime;
import java.util.List;

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
public class CostManagerCostSumCalculationTests {

    @Autowired
    TestDataHelper testDataHelper;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    public void init() {
        testDataHelper.clearDb();
    }

    @Test
    void sumInvalidFilterTest() {
        Cost c = testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());

        UserCostRequestParamDTO queryParam = new UserCostRequestParamDTO();
        getSumOfCostForUserExceptBadRequest(queryParam);

        queryParam.setUserId("TestUser_1");
        getSumOfCostForUserExceptBadRequest(queryParam);

        queryParam.setStartDate(LocalDateTime.now().minusMonths(1));
        getSumOfCostForUserExceptBadRequest(queryParam);

        queryParam.setEndDate(LocalDateTime.now().minusMonths(3));
        getSumOfCostForUserExceptBadRequest(queryParam);

        queryParam.setEndDate(LocalDateTime.now());

        Double costs = getSumOfCostsForUser(queryParam);
        assertThat(costs).isEqualTo(c.getCost());
    }

    @Test
    void sumOfCostsTest() {
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusDays(1));
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusDays(2));
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusDays(3));


        Double costs = getSumOfCostsForUser(UserCostRequestParamDTO.builder()
                                                                   .userId("TestUser_1")
                                                                   .startDate(LocalDateTime.now().minusMonths(1))
                                                                   .endDate(LocalDateTime.now().plusMonths(1)).build());
        assertThat(costs).isEqualTo(400.0);
    }


    @Test
    void sumCostUserFilterTest() {
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_2", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_3", LocalDateTime.now());

        Double costs = getSumOfCostsForUser(UserCostRequestParamDTO.builder()
                                                                   .userId("TestUser_1")
                                                                   .startDate(LocalDateTime.now().minusMonths(1))
                                                                   .endDate(LocalDateTime.now().plusMonths(1)).build());
        assertThat(costs).isEqualTo(200.0);
    }

    @Test
    void sumCostDateFilterTest() {
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusMonths(1));
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusMonths(1));
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusMonths(3));

        Double costs = getSumOfCostsForUser(UserCostRequestParamDTO.builder()
                                                                   .userId("TestUser_1")
                                                                   .startDate(LocalDateTime.now().minusMonths(2))
                                                                   .endDate(LocalDateTime.now().plusMonths(1)).build());
        assertThat(costs).isEqualTo(300.0);
    }

    private void getSumOfCostForUserExceptBadRequest(UserCostRequestParamDTO requestParam) {
        getRequest(requestParam)
            .expectStatus().isBadRequest();
    }

    private WebTestClient.ResponseSpec getRequest(UserCostRequestParamDTO requestParam) {
        return webTestClient.post().uri("/sumOfCosts")
                            .body(Mono.just(requestParam), UserCostRequestParamDTO.class)
                            .exchange();
    }

    private Double getSumOfCostsForUser(UserCostRequestParamDTO requestParam) {
        return getRequest(requestParam)
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<Double>() {
            })
            .returnResult()
            .getResponseBody();
    }

}
