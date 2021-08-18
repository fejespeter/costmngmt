package com.fejesp.costmanager.costmanagerapp.mapper;

import java.util.List;

import com.fejesp.costmanager.costmanagerapp.UserCostDTO;
import com.fejesp.costmanager.costmanagerapp.repository.model.Cost;
import org.mapstruct.Mapper;

@Mapper
public interface CostMapper {

    UserCostDTO costToUserCostDTO(Cost cost);

    List<UserCostDTO> costListToUserCostDTOList(List<Cost> cost);

}
