package com.fejesp.costmanager.costmanagerapp.controller;

import java.util.List;

import com.fejesp.costmanager.costmanagerapp.UserCostDTO;
import com.fejesp.costmanager.costmanagerapp.UserCostRequestParamDTO;
import com.fejesp.costmanager.costmanagerapp.repository.model.Cost;
import com.fejesp.costmanager.costmanagerapp.service.UserCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller to handle requests to store and query user costs.
 */
@CrossOrigin
@RestController
public class UserCostController {

    private final UserCostService userCostService;

    @Autowired
    public UserCostController(UserCostService userCostService) {
        this.userCostService = userCostService;
    }

    @PostMapping(path = "/getCostsOfUser")
    public ResponseEntity<List<UserCostDTO>> getUserCost(@RequestBody UserCostRequestParamDTO costRequestParamDTO) {
        if (userCostService.validateUserCostRequestParamDTO(costRequestParamDTO)) {
            return ResponseEntity.ok(userCostService.findCosts(costRequestParamDTO));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/storeCost")
    public ResponseEntity<Cost> storeUserCost(@RequestBody UserCostDTO userCostDTO) {
        if (userCostService.validateUserCostDTO(userCostDTO)) {
            return ResponseEntity.ok(userCostService.storeCost(userCostDTO));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
