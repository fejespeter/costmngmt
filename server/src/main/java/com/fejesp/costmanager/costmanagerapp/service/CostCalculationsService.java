package com.fejesp.costmanager.costmanagerapp.service;

import java.time.temporal.ChronoUnit;

import com.fejesp.costmanager.costmanagerapp.UserCostRequestParamDTO;
import com.fejesp.costmanager.costmanagerapp.repository.CostRepository;
import com.fejesp.costmanager.costmanagerapp.repository.model.Cost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CostCalculationsService {

    private final CostRepository costRepository;

    @Autowired
    public CostCalculationsService(CostRepository costRepository) {
        this.costRepository = costRepository;
    }

    public Double calculateSumOfCosts(UserCostRequestParamDTO costRequestParamDTO) {
        return costRepository.findAllByUserIdAndDateBetween(costRequestParamDTO.getUserId(), costRequestParamDTO.getStartDate(),
                                                            costRequestParamDTO.getEndDate()).stream().mapToDouble(Cost::getCost).sum();
    }

    public Double calculateAvgOfCosts(final String userId) {
        double sumOfCosts = costRepository.findAllByUserId(userId).stream().mapToDouble(Cost::getCost).sum();

        long nrOfDays = ChronoUnit.DAYS.between(costRepository.findTopByUserIdOrderByDateAsc(userId).getDate(),
                                                costRepository.findTopByUserIdOrderByDateDesc(userId).getDate()) + 2 ;

        return sumOfCosts / nrOfDays;
    }
}
