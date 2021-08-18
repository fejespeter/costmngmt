package com.fejesp.costmanager.costmanagerapp.testhelper;

import java.time.LocalDateTime;
import java.util.List;

import com.fejesp.costmanager.costmanagerapp.repository.CostRepository;
import com.fejesp.costmanager.costmanagerapp.repository.model.Cost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDataHelper {

    @Autowired
    CostRepository costRepository;


    public Cost insertTestCost(Cost cost) {
        costRepository.save(cost);
        return cost;
    }

    public Cost insertTestCostForUser(final String userId, final LocalDateTime date) {
        Cost cost = new Cost();
        cost.setUserId(userId);
        cost.setDate(date);
        cost.setCost(100.0);
        insertTestCost(cost);
        return cost;
    }


    public List<Cost> getAllCostOfUser(final String userId) {
        return costRepository.findAllByUserId(userId);
    }

    private Cost createTestCost() {
        Cost cost = new Cost();
        cost.setCost(100.0);
        cost.setDate(LocalDateTime.now());
        cost.setUserId("TestUser");
        return cost;
    }

    public void clearDb() {
        costRepository.deleteAll();
    }
}
