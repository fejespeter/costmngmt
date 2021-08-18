package com.fejesp.costmanager.costmanagerapp.controller;

import com.fejesp.costmanager.costmanagerapp.UserCostRequestParamDTO;
import com.fejesp.costmanager.costmanagerapp.service.CostCalculationsService;
import com.fejesp.costmanager.costmanagerapp.service.UserCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class CostCalculationsController {

    private final CostCalculationsService costCalculationsService;
    private final UserCostService userCostService;

    @Autowired
    public CostCalculationsController(CostCalculationsService costCalculationsService,
                                      UserCostService userCostService) {
        this.costCalculationsService = costCalculationsService;
        this.userCostService = userCostService;
    }

    @PostMapping(path = "/sumOfCosts")
    public ResponseEntity<Double> getSumOfCosts(@RequestBody UserCostRequestParamDTO costRequestParamDTO) {
        if (userCostService.validateUserCostRequestParamDTO(costRequestParamDTO)) {
            return ResponseEntity.ok(costCalculationsService.calculateSumOfCosts(costRequestParamDTO));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/avgOfCosts/{userId}")
    public ResponseEntity<Double> getAvgOfCosts(@PathVariable String userId) {
        if (StringUtils.hasText(userId)) {
            return ResponseEntity.ok(costCalculationsService.calculateAvgOfCosts(userId));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
