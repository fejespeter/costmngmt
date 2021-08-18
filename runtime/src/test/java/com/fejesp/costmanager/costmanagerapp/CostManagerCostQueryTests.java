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
public class CostManagerCostQueryTests {

    @Autowired
    TestDataHelper testDataHelper;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    public void init() {
        testDataHelper.clearDb();
    }

    @Test
    void queryDateFilterTest() {
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusMonths(1));
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusMonths(2).plusDays(10));
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now().minusMonths(3));

        List<Cost> costs = getAllCostForUser(UserCostRequestParamDTO.builder()
                                                                    .userId("TestUser_1")
                                                                    .startDate(LocalDateTime.now().minusMonths(2))
                                                                    .endDate(LocalDateTime.now().plusMonths(1)).build());

        assertThat(costs).hasSize(3);
    }

    @Test
    void queryUserIdFilterTest() {
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_2", LocalDateTime.now());
        testDataHelper.insertTestCostForUser("TestUser_3", LocalDateTime.now());


        List<Cost> costs = getAllCostForUser(UserCostRequestParamDTO.builder()
                                                                    .userId("TestUser_1")
                                                                    .startDate(LocalDateTime.now().minusMonths(1))
                                                                    .endDate(LocalDateTime.now().plusMonths(1)).build());

        assertThat(costs).hasSize(2);
    }

    @Test
    void queryInvalidFilterTest() {
        testDataHelper.insertTestCostForUser("TestUser_1", LocalDateTime.now());

        UserCostRequestParamDTO queryParam = new UserCostRequestParamDTO();
        getAllCostForUserExceptBadRequest(queryParam);

        queryParam.setUserId("TestUser_1");
        getAllCostForUserExceptBadRequest(queryParam);

        queryParam.setStartDate(LocalDateTime.now().minusMonths(1));
        getAllCostForUserExceptBadRequest(queryParam);

        queryParam.setEndDate(LocalDateTime.now().minusMonths(3));
        getAllCostForUserExceptBadRequest(queryParam);

        queryParam.setEndDate(LocalDateTime.now());

        List<Cost> costs = getAllCostForUser(queryParam);
        assertThat(costs).hasSize(1);
    }

    private List<Cost> getAllCostForUser(UserCostRequestParamDTO requestParam) {
        return getResponse(requestParam)
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<Cost>>() {
            })
            .returnResult()
            .getResponseBody();
    }

    private void getAllCostForUserExceptBadRequest(UserCostRequestParamDTO requestParam) {
        getResponse(requestParam)
            .expectStatus().isBadRequest();
    }

    private WebTestClient.ResponseSpec getResponse(UserCostRequestParamDTO requestParam) {
        return webTestClient.post()
                            .uri("/getCostsOfUser")
                            .body(Mono.just(requestParam), UserCostRequestParamDTO.class)
                            .exchange();
    }

}
