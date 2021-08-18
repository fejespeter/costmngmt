package com.fejesp.costmanager.costmanagerapp.mapper;

import com.fejesp.costmanager.costmanagerapp.UserCostDTO;
import com.fejesp.costmanager.costmanagerapp.repository.model.Cost;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-08-18T22:09:05+0200",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 1.8.0_91 (Oracle Corporation)"
)
public class CostMapperImpl implements CostMapper {

    @Override
    public UserCostDTO costToUserCostDTO(Cost cost) {
        if ( cost == null ) {
            return null;
        }

        UserCostDTO userCostDTO = new UserCostDTO();

        userCostDTO.setId( cost.getId() );
        userCostDTO.setUserId( cost.getUserId() );
        userCostDTO.setCost( cost.getCost() );
        userCostDTO.setDate( cost.getDate() );

        return userCostDTO;
    }

    @Override
    public List<UserCostDTO> costListToUserCostDTOList(List<Cost> cost) {
        if ( cost == null ) {
            return null;
        }

        List<UserCostDTO> list = new ArrayList<UserCostDTO>( cost.size() );
        for ( Cost cost1 : cost ) {
            list.add( costToUserCostDTO( cost1 ) );
        }

        return list;
    }
}
