package com.fejesp.costmanager.costmanagerapp.service;

import java.util.List;

import com.fejesp.costmanager.costmanagerapp.UserCostDTO;
import com.fejesp.costmanager.costmanagerapp.UserCostRequestParamDTO;
import com.fejesp.costmanager.costmanagerapp.mapper.CostMapper;
import com.fejesp.costmanager.costmanagerapp.repository.CostRepository;
import com.fejesp.costmanager.costmanagerapp.repository.model.Cost;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserCostService {

    private final CostRepository costRepository;
    private final CostMapper costMapper = Mappers.getMapper(CostMapper.class);

    @Autowired
    public UserCostService(CostRepository costRepository) {
        this.costRepository = costRepository;
    }

    public Cost storeCost(UserCostDTO userCostDTO) {
        Cost cost = new Cost();
        cost.setCost(userCostDTO.getCost());
        cost.setUserId(userCostDTO.getUserId());
        cost.setDate(userCostDTO.getDate());
        costRepository.save(cost);
        return cost;
    }

    public List<UserCostDTO> findCosts(UserCostRequestParamDTO costRequestParamDTO) {
        return costMapper.costListToUserCostDTOList(
            costRepository.findAllByUserIdAndDateBetween(costRequestParamDTO.getUserId(), costRequestParamDTO.getStartDate(),
                                                         costRequestParamDTO.getEndDate()));
    }

    public boolean validateUserCostRequestParamDTO(UserCostRequestParamDTO costRequestParamDTO) {
        if (!StringUtils.hasText(costRequestParamDTO.getUserId())
            || costRequestParamDTO.getStartDate() == null
            || costRequestParamDTO.getEndDate() == null) {
            return false;
        }
        if (costRequestParamDTO.getEndDate().isBefore(costRequestParamDTO.getStartDate())) {
            return false;
        }
        return true;
    }

    public boolean validateUserCostDTO(UserCostDTO userCostDTO) {
        if (userCostDTO == null) {
            return false;
        }
        if (!StringUtils.hasText(userCostDTO.getUserId()) || userCostDTO.getDate() == null || userCostDTO.getCost() == null) {
            return false;
        }
        if (userCostDTO.getCost() < 0) {
            return false;
        }
        return true;
    }
}
